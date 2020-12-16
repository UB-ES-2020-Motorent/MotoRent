from flask_restful import Resource, reqparse
from models.rentals import RentalsModel
from datetime import datetime, timedelta
from models.motos import MotosModel



parser = reqparse.RequestParser()
parser.add_argument('moto_id', type=int, required=False, help='Moto id of rented moto.')
parser.add_argument('user_id', type=int, required=False, help='User id of rented moto.')
parser.add_argument('end_rental', type=str, required=False, help='end_rental=True to end rental,'
                                                                  'end_rental=False to start counting time of rental')
parser.add_argument('latitude', type=float, required=False, help='Moto latitude of rented moto.')
parser.add_argument('longitude', type=float, required=False, help='Moto longitude of rented moto.')


class Rentals(Resource):
    """
    API Restful methods for Rentals
    """

    def get(self, id):
        """
        GET method
        Gets a rental by id
        Param: int id
        Return: dict (account ok / message)
        """

        rental = RentalsModel.find_by_id(id=id)

        if rental:
            return {'rental': rental.json()}, 200
        else:
            return {'message': 'Rental with id [{}] not found'.format(id)}, 404

    def post(self):
        """
        POST method
        Adds a new rental. This method is used for initialize a rental.
        Return: dict (rental created / message)
        """
        data = parser.parse_args()
        if not data['moto_id']:
            return {'message': {
                "moto_id": "Moto_id cant be empty"
            }}, 400
        if not data['user_id']:
            return {'message': {
                "user_id": "User_id cant be empty"
            }}, 400

        activerental = RentalsModel.find_active_rental_by_user_id(user_id=data['user_id'])
        if activerental:
            return {'message': "User with id [{}] already has an ongoing rental".format(data['user_id'])}, 409

        rental = RentalsModel(moto_id=data['moto_id'],
                              user_id=data['user_id'],
                              book_hour=datetime.now().isoformat())
        moto = MotosModel.find_by_id(data['moto_id'])
        try:
            rental.save_to_db()
            moto.set_available(False)
            return {'rental': RentalsModel.find_by_id(rental.id).json()}, 201

        except:
            return {'message': 'Internal server error'}, 500

    def put(self, id):
        """
        PUT method
        Updates a rental. This method is used for finish a rental.
        :param id:
        :return: dict (rental updated / message)
        """
        data = parser.parse_args()
        rental = RentalsModel.find_by_id(id)
        moto = MotosModel.find_by_id(rental.moto_id)
        if rental is None:
            return {'message': 'This rental doesn t exist'}, 404
        if data['end_rental'] is not None:
            if str_to_bool(data['end_rental']):
                if rental.finish_rental_hour is not None:
                    return {'message': 'The rental is already finsished.'}, 409
                if not data['latitude'] or not data['longitude']:
                    return {'message': 'latitude and longitude parameters needed for updating moto coordinates'}, 409
                try:
                    rental.update_finish_rent_hour(datetime.now().isoformat())
                    moto.set_available(True)
                    moto.update_coords(data['latitude'], data['longitude'])
                    new_rental = RentalsModel.find_by_id(rental.id)
                    return {'rental': new_rental.json()}, 200
                except:
                    return {'message': 'Internal server error'}, 500
            else:
                if rental.finish_book_hour is not None:
                    return {'message': 'The book is already finsished.'}, 409
                try:
                    max_finish_book_hour = datetime.strptime(rental.book_hour, '%Y-%m-%dT%H:%M:%S.%f') + timedelta(minutes=15)
                    if max_finish_book_hour > datetime.now():
                        rental.update_finish_book_hour(datetime.now().isoformat())
                        new_rental = RentalsModel.find_by_id(rental.id)
                        return {'rental': new_rental.json()}, 200
                    else:
                        rental.update_finish_book_hour(max_finish_book_hour.isoformat())
                        new_rental = RentalsModel.find_by_id(rental.id)
                        return {'rental': new_rental.json()}, 200
                except:
                    return {'message': 'Internal server error'}, 500
        else:
            return {'message': 'Bad request, you must pass end_rental param'}, 400

    def delete(self, id):
        """
        DELETE method
        Removes a rental
        Param: int id
        Return: dict (message ok / message)
        """
        rental = RentalsModel.find_by_id(id=id)
        if rental:
            try:
                rental.delete_from_db()
                return {'message': "Rental with id [{}] and all associated info deleted".format(id)}, 200
            except:
                return {'message': "Internal server error"}, 500
        else:
            return {'message': "Rental with id [{}] Not found".format(id)}, 404

class ActiveRentals(Resource):
    """
    API Restful methods for ActiveRentals
    """
    def get(self, user_id):
        """
        GET method
        Gets active rental by user id
        Param: int user id
        Return: dict (account ok / message)
        """

        rental = RentalsModel.find_active_rental_by_user_id(user_id=user_id)

        if rental:
            return {'rental': rental.json()}, 200
        else:
            return {'message': 'User with id [{}] has no active rentals'.format(user_id)}, 404

class RentalsList(Resource):
    """
    API Restful methods for RentalsList
    """
    def get(self):
        """
        GET method
        Return: dict (rentals)
        """
        try:
            rentals = RentalsModel.all_rentals()
            return {'rentals': [rental.json() for rental in rentals]}, 200
        except Exception as e:
            return {'message': 'Internal server error.\n' + str(e)}, 500


def str_to_bool(s):
    print(s)
    if s == 'True':
        return True
    elif s == 'False':
         return False
    else:
         raise ValueError