from flask_restful import Resource, reqparse
from models.incidents import IncidentsModel
import sys

parser = reqparse.RequestParser()
parser.add_argument('moto_id', type=int, required=False, help='Moto id of incident.')
parser.add_argument('user_id', type=int, required=False, help='User id of incident.')
parser.add_argument('comment', type=str, required=False, help='Comment of incident.')



class Incident(Resource):
    """
    API Restful methods for Rentals
    """

    def get(self, incident_id):
        """
        GET method. Gets a Incident by id
        :param incident_id: int
        :return: dict
        """
        incident = IncidentsModel.find_by_id(incident_id=incident_id)

        if incident:
            return {'incident': incident.json()}, 200
        else:
            return {'message': 'Incident with id [{}] not found'.format(id)}, 404

    def post(self):
        """
        POST method. Post a incident in the database.
        :return:
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
        if not data['comment']:
            return {'message': {
                "comment": "comment cant be empty"
            }}, 400

        incident = IncidentsModel(user_id=data['user_id'],
                                  moto_id=data['moto_id'],
                                  comment=data['comment'])
        try:
            incident.save_to_db()
            return {'incident': IncidentsModel.find_by_id(incident.id).json()}, 201
        except Exception as e:
            return {'message': 'Internal server error.\n' + str(e)}, 500

    def delete(self, incident_id):
        """
        DELETE method
        :param id:
        :return:
        """

        incident = IncidentsModel.find_by_id(incident_id=incident_id)
        if incident:
            try:
                incident.delete_from_db()
                return {'message': "Incient with id [{}] and all associated info deleted".format(incident_id)}, 200
            except Exception as e:
                return {'message': 'Interanl server error.\n' + str(e)}, 500
        else:
            return {'message': "Incident with id [{}] Not found".format(incident_id)}, 404

class IncidentsList(Resource):
    """
    API restful methods for incidents list
    """

    def get(self):
        """
        GET method
        :return:
        """
        incidents = IncidentsModel.all_incidents()
        return {'incidents': [incident.json() for incident in incidents]}, 200
