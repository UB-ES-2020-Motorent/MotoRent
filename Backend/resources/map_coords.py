from flask_restful import Resource, reqparse
from models.map_coords import MapCoordsModel
from models.users import auth
from flask import g

parser = reqparse.RequestParser()
parser.add_argument('from_latitude', type=str, required=False, help="Origin latitude, This field cannot be left blank")
parser.add_argument('from_longitude', type=str, required=False, help="Origin longitude, This field cannot be left blank")
parser.add_argument('to_latitude', type=str, required=False, help="Destiny latitude, This field cannot be left blank")
parser.add_argument('to_longitude', type=str, required=False, help="Destiny longitude, This field cannot be left blank")


class MapCoords(Resource):
    """
    API Restful methods for MapsCoords
    """

    def get(self):
        """
        GET method
        Gets pair of coordinates form origin coordinates
        Return: dict (account ok / message)
        """

        data = parser.parse_args()

        if not data['from_latitude']:
            return {'message': {
                "from_latitude": "Origin latitude cant be empty"
            }}, 400

        if not data['from_longitude']:
            return {'message': {
                "from_longitude": "Origin longitude cant be empty"
            }}, 400

        coords = MapCoordsModel.find_by_origin(from_latitude=data['from_latitude'],
                                               from_longitude=data['from_longitude'])

        if coords:
            return {'coords': coords.json()}, 200
        else:
            return {'message': "Coords with origin Lat[{}] - Long[{}] Not found".format(
                data['from_latitude'], data['from_longitude'])}, 404

    @auth.login_required(role='admin')
    def post(self):
        """
        POST method
        Adds new map coords
        Return: dict (moto coords created / message)
        """
        data = parser.parse_args()

        if not data['from_latitude']:
            return {'message': {
                "from_latitude": "Origin latitude cant be empty"
            }}, 400

        if not data['from_longitude']:
            return {'message': {
                "from_longitude": "Origin longitude cant be empty"
            }}, 400

        if not data['to_latitude']:
            return {'message': {
                "to_latitude": "Destiny latitude cant be empty"
            }}, 400

        if not data['to_longitude']:
            return {'message': {
                "longitude": "Destiny longitude cant be empty"
            }}, 400

        if MapCoordsModel.find_by_origin(from_latitude=data['from_latitude'],
                                         from_longitude=data['from_longitude']):
            return {'message': "Pair of coords with origin Lat[{}] - Long[{}] Already exist".format(
                data['from_latitude'], data['from_longitude'])}, 409

        coords = MapCoordsModel(from_latitude=data['from_latitude'], from_longitude=data['from_longitude'],
                                to_latitude=data['to_latitude'], to_longitude=data['to_longitude'])

        try:
            coords.save_to_db()
            return {'coords': MapCoordsModel.find_by_origin(from_latitude=coords.from_latitude,
                                                            from_longitude=coords.from_longitude).json()}, 201
        except:
            return {"message": "Error Description"}, 500

    @auth.login_required(role='admin')
    def put(self):
        """
        PUT method
        Modifies/Adds map coords
        Return: dict (map coords updated/created)
        """
        data = parser.parse_args()

        if not data['from_latitude']:
            return {'message': {
                "from_latitude": "Origin latitude cant be empty"
            }}, 400

        if not data['from_longitude']:
            return {'message': {
                "from_longitude": "Origin longitude cant be empty"
            }}, 400

        coords = MapCoordsModel.find_by_origin(from_latitude=data['from_latitude'],
                                               from_longitude=data['from_longitude'])

        if data['to_latitude']:
            coords.to_latitude = data["to_latitude"]
        if data['to_longitude']:
            coords.to_longitude = data["to_longitude"]

        try:
            coords.save_to_db()
            return {'coords': MapCoordsModel.find_by_origin(from_latitude=coords.from_latitude,
                                                            from_longitude=coords.from_longitude).json()}, 200
        except:
            return {"message": "Error Description"}, 500

    @auth.login_required(role='admin')
    def delete(self):
        """
        DELETE method
        Removes map coords
        Return: dict (message ok / message)
        """

        data = parser.parse_args()

        if not data['from_latitude']:
            return {'message': {
                "from_latitude": "Origin latitude cant be empty"
            }}, 400

        if not data['from_longitude']:
            return {'message': {
                "from_longitude": "Origin longitude cant be empty"
            }}, 400

        coords = MapCoordsModel.find_by_origin(from_latitude=data['from_latitude'],
                                               from_longitude=data['from_longitude'])

        if coords:
            try:
                coords.delete_from_db()
                return {'message': "Pair of coords with origin Lat[{}] - Long[{}] Deleted".format(
                    data['from_latitude'], data['from_longitude'])}, 200
            except:
                return {"message": "Error Description"}, 500
        else:
            return {'message': "Pair of coords with origin Lat[{}] - Long[{}] Not found".format(
                data['from_latitude'], data['from_longitude'])}, 404


class MapCoordsList(Resource):
    """
    API Restful methods for MotoCoordsList
    """

    def get(self):
        """
        GET method
        Return: dict (moto coords)
        """
        data = parser.parse_args()

        map_coords = MapCoordsModel.all_map_coords()

        return {'map_coords': [pair_coords.json() for pair_coords in map_coords]}, 200
