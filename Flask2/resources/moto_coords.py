from flask_restful import Resource, reqparse
from models.moto_coords import MotoCoordsModel

parser = reqparse.RequestParser()
parser.add_argument('moto_id', type=str, required=False, help="Moto id, This field cannot be left blank")
parser.add_argument('latitude', type=str, required=True, help="Moto latitude, This field cannot be left blank")
parser.add_argument('longitude', type=str, required=True, help="Moto longitude, This field cannot be left blank")
parser.add_argument('admin_code', type=str, required=False, help="Account code for admins for special post")

admin_parser = reqparse.RequestParser()
admin_parser.add_argument('admin_code', type=str, required=False, help="Account code for admins for special post")


class MotoCoords(Resource):
    """
    API Restful methods for MotoCoords
    """

    def get(self, moto_id):
        """
        GET method
        Gets a moto coords by id
        Param: string moto_id
        Return: dict (account ok / message)
        """

        data = admin_parser.parse_args()

        moto = MotoCoordsModel.find_by_moto_id(moto_id=moto_id)

        if moto:
            if not data['admin_code']:
                return {'moto': moto.json(0)}, 200
            else:
                if data['admin_code'] != 'admin_secret_code':
                    return {'message': "Wrong admin code"}, 400
                else:
                    return {'moto': moto.json(1)}, 200
        else:
            return {'message': "User with username [{}] Not found".format(moto_id)}, 404

    def post(self):
        """
        POST method
        Adds new moto coords
        Return: dict (moto coords created / message)
        """
        data = parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to add moto coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
                if not data['moto_id']:
                    return {'message': {
                        "moto_id": "Moto id cant be empty"
                    }}, 400

                if not data['latitude']:
                    return {'message': {
                        "latitude": "Latitude cant be empty"
                    }}, 400

                if not data['longitude']:
                    return {'message': {
                        "longitude": "Longitude token cant be empty"
                    }}, 400

                if MotoCoordsModel.find_by_moto_id(moto_id=data['moto_id']):
                    return {'message': "Moto with id [{}] already exists".format(data['moto_id'])}, 409

                moto = MotoCoordsModel(moto_id=data['moto_id'], latitude=data['latitude'], longitude=data['longitude'])

                try:
                    moto.save_to_db()
                    return {'moto': MotoCoordsModel.find_by_moto_id(moto.moto_id).json(1)}, 201
                except:
                    return {"message": "Error Description"}, 500

    def put(self, moto_id):
        """
        PUT method
        Modifies/Adds moto coords
        Param: moto_id
        Return: dict (moto coords updated/created)
        """
        data = parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to modify moto coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
                if not data['latitude']:
                    return {'message': {
                        "latitude": "Latitude cant be empty"
                    }}, 400
                if not data['longitude']:
                    return {'message': {
                        "longitude": "Longitude cant be empty"
                    }}, 400

                moto = MotoCoordsModel.find_by_moto_id(moto_id)

                if not moto:
                    moto = MotoCoordsModel(moto_id=moto_id, latitude=data['latitude'], longitude=data['longitude'])
                    try:
                        moto.save_to_db()
                        return {'moto': MotoCoordsModel.find_by_moto_id(moto.moto_id).json(1)}, 201
                    except:
                        return {"message": "Error Description"}, 500

                moto.latitude = data["latitude"]
                moto.longitude = data["longitude"]

                try:
                    moto.save_to_db()
                    return {'moto': MotoCoordsModel.find_by_moto_id(moto.moto_id).json(1)}, 200
                except:
                    return {"message": "Error Description"}, 500

    def delete(self, moto_id):
        """
        DELETE method
        Removes moto coords
        Param: string moto id
        Return: dict (message ok / message)
        """

        data = admin_parser.parse_args()

        moto = MotoCoordsModel.find_by_moto_id(moto_id=moto_id)

        if not data['admin_code']:
            return {"message": "You need an admin code to delete moto coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
                if moto:
                    try:
                        moto.delete_from_db()
                        return {'message': "Coords of moto with id [{}] and all associated info deleted".format(
                            moto_id)}, 200
                    except:
                        return {"message": "Error Description"}, 500
                else:
                    return {'message': "Moto with id [{}] Not found".format(moto_id)}, 404


class MotoCoordsList(Resource):
    """
    API Restful methods for MotoCoordsList
    """
    def get(self):
        """
        GET method
        Return: dict (moto coords)
        """
        data = admin_parser.parse_args()

        motos_coords = MotoCoordsModel.all_moto_coords()

        if not data['admin_code']:
            return {'motos_coords': [moto_coords.json(0) for moto_coords in motos_coords]}, 200
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {'message': "Wrong admin code"}, 400
            else:
                return {'motos_coords': [moto_coords.json(1) for moto_coords in motos_coords]}, 200
