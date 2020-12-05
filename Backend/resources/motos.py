from flask_restful import Resource, reqparse
from models.motos import MotosModel

parser = reqparse.RequestParser()
parser.add_argument('id',
                    type=int
                    )
parser.add_argument('license_number',
                    type=str
                    )
parser.add_argument('battery',
                    type=int
                    )
parser.add_argument('available',
                    type=str
                    )
parser.add_argument('license_number',
                    type=str
                    )
parser.add_argument('latitude',
                    type=float
                    )
parser.add_argument('longitude',
                    type=float
                    )
class Motos(Resource):
    """
    API Restful methods for Accounts
    """
    def get(self, id):
        """
        GET method
        Gets an moto by id
        Param: int id
        Return: dict (moto ok / message)
        """

        moto = MotosModel.find_by_id(id=id)
        if moto:
            return {'moto': moto.json()}, 200
        else:
            return {'message': "Moto with id [{}] Not found.".format(id)}, 404


    def post(self):
        """
        POST method
        Adds a new account
        Return: dict (account created / message)
        """
        data = parser.parse_args()

        if not data['license_number']:
            return {'message': {
                "license_number": "Name cant be empty"
            }}, 400

        if not data['battery']:
            return {'message': {
                "battery": "Battery cant be empty"
            }}, 400

        if not data['longitude']:
            return {'message': {
                "longitude": "Longitude cant be empty"
            }}, 400

        if not data['latitude']:
            return {'message': {
                "latitude": "Latitude cant be empty"
            }}, 400

        if MotosModel.find_by_license_number(license_number=data['license_number']):
            return {'message': "Account with license number [{}] already exists".format(data['license_number'])}, 409

        moto = MotosModel(license_number=data['license_number'],
                          battery=data['battery'],
                          latitude=data['latitude'],
                          longitude=data['longitude']
                          )
        try:
            moto.save_to_db()
            return {'moto': MotosModel.find_by_id(moto.id).json()}, 201
        except:
            return {"message": "Something went wrong"}, 500

    def put(self, id):
        """
        PUT method
        Modifies a motos
        Param: id
        Return: dict (moto updated)
        """
        data = parser.parse_args()

        if not data['available']:
            return {'message': {
                "available": "Specify true or false"
            }}, 400

        moto = MotosModel.find_by_id(id=id)

        try:
            moto.available = str_to_bool(data['available'])
        except ValueError:
            return {'message': {
                "available": "Specify a boolean value, true or false"
            }}, 400

        try:
            moto.save_to_db()
            return {'user': MotosModel.find_by_id(id).json()}, 200
        except:
            return {"message": "Error Description"}, 500

    def delete(self, id):
        """
        DELETE method
        Removes a moto
        Param: int id
        Return: dict (message ok / message)
        """
        moto = MotosModel.find_by_id(id=id)
        if moto:
            try:
                moto.delete_from_db()
                return {'message': "Moto with id [{}] deleted.".format(id)}, 200
            except:
                return {"message": "Something went wrong."}, 500
        else:
            return {'message': "Moto with id [{}] Not found".format(id)}, 404


class MotosList(Resource):
    """
    API Restful methods for MotosList
    """

    def get(self):
        """
        GET method
        Return: dict (accounts)
        """
        data = parser.parse_args()
        print(data)
        if data['available']:
            available = str_to_bool(data['available'])
            motos = MotosModel.get_available_motos(available=available)
            return {'motos': [moto.json() for moto in motos]}, 200
        if data['license_number']:
            moto = MotosModel.find_by_license_number(license_number=data['license_number'])
            if moto is None:
                return {'motos': [],
                        'message': "Moto with license number [{}] Not found".format(data['license_number'])}, 404
        motos = MotosModel.all_motos()
        return {'motos': [moto.json() for moto in motos]}, 200


def str_to_bool(s):
    if s.lower() == 'true':
         return True
    elif s.lower() == 'false':
         return False
    else:
         raise ValueError