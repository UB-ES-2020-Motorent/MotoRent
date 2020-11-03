from flask_restful import Resource, reqparse
from models.map_coords import MapCoordsModel

parser = reqparse.RequestParser()
parser.add_argument('from_latitude', type=str, required=True, help="Origin latitude, This field cannot be left blank")
parser.add_argument('from_longitude', type=str, required=True, help="Origin longitude, This field cannot be left blank")
parser.add_argument('to_latitude', type=str, required=False, help="Destiny latitude, This field cannot be left blank")
parser.add_argument('to_longitude', type=str, required=False, help="Destiny longitude, This field cannot be left blank")
parser.add_argument('admin_code', type=str, required=False, help="Account code for admins for special post")

admin_parser = reqparse.RequestParser()
admin_parser.add_argument('admin_code', type=str, required=False, help="Account code for admins for special post")


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

        data = admin_parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to see map coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:

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

    def post(self):
        """
        POST method
        Adds new map coords
        Return: dict (moto coords created / message)
        """
        data = parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to add map coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
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

                coords_to_change = MapCoordsModel.find_by_destiny(to_latitude=data['to_latitude'], to_longitude=data['to_longitude'])
                if coords_to_change:
                    coords_to_change.to_latitude = data['from_latitude']
                    coords_to_change.to_longitude = data['from_longitude']

                try:
                    coords.save_to_db()
                    if coords_to_change:
                        coords_to_change.save_to_db()
                    return {'coords': MapCoordsModel.find_by_origin(from_latitude=coords.from_latitude,
                                                                    from_longitude=coords.from_longitude).json()}, 201
                except:
                    return {"message": "Error Description"}, 500

    def put(self):
        """
        PUT method
        Modifies/Adds map coords
        Return: dict (map coords updated/created)
        """
        data = parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to modify map coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
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

                coords = MapCoordsModel.find_by_origin(from_latitude=data['from_latitude'],
                                                       from_longitude=data['from_longitude'])

                if not coords:
                    coords = MapCoordsModel(from_latitude=data['from_latitude'], from_longitude=data['from_longitude'],
                                            to_latitude=data['to_latitude'], to_longitude=data['to_longitude'])

                    coords_to_change = MapCoordsModel.find_by_destiny(to_latitude=data['to_latitude'],
                                                                      to_longitude=data['to_longitude'])
                    if coords_to_change:
                        coords_to_change.to_latitude = data['from_latitude']
                        coords_to_change.to_longitude = data['from_longitude']

                    try:
                        coords.save_to_db()
                        if coords_to_change:
                            coords_to_change.save_to_db()
                        return {'coords': MapCoordsModel.find_by_origin(from_latitude=coords.from_latitude,
                                                                        from_longitude=coords.from_longitude).json()}, 201
                    except:
                        return {"message": "Error Description"}, 500

                coords_to_change_put = MapCoordsModel.find_by_origin(from_latitude=coords.to_latitude,
                                                                     from_longitude=coords.to_longitude)
                if coords_to_change_put:
                    coords_to_change_put.from_latitude=data['to_latitude']
                    coords_to_change_put.from_longitude=data['to_longitude']

                coords.to_latitude = data["to_latitude"]
                coords.to_longitude = data["to_longitude"]

                try:
                    coords.save_to_db()
                    if coords_to_change_put:
                        coords_to_change_put.save_to_db()
                    return {'coords': MapCoordsModel.find_by_origin(from_latitude=coords.from_latitude,
                                                                    from_longitude=coords.from_longitude).json()}, 201
                except:
                    return {"message": "Error Description"}, 500

    def delete(self):
        """
        DELETE method
        Removes map coords
        Return: dict (message ok / message)
        """

        data = parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to delete map coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
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
                        coords_to_change = MapCoordsModel.find_by_destiny(to_latitude=data['from_latitude'],
                                                                          to_longitude=data['from_longitude'])
                        if coords_to_change:
                            coords_to_change.to_latitude = coords.to_latitude
                            coords_to_change.to_longitude = coords.to_longitude
                            coords_to_change.save_to_db()

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
        data = admin_parser.parse_args()

        map_coords = MapCoordsModel.all_map_coords()

        if not data['admin_code']:
            return {"message": "You need an admin code to see map coords"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {'message': "Wrong admin code"}, 400
            else:
                return {'map_coords': [pair_coords.json() for pair_coords in map_coords]}, 200
