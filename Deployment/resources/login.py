from flask_restful import Resource, reqparse
from models.users import UsersModel

parser = reqparse.RequestParser()
parser.add_argument('mail', type=str, required=True, help="Account mail, This field cannot be left blank")
parser.add_argument('google_token', type=str, required=True, help="Account google token, This field cannot be left blank")


class Login(Resource):
    """
    API Restful methods for Login
    """

    def post(self):
        """
        POST method
        Login, Generates a token if is all okay
        Return: dict
        """
        data = parser.parse_args()

        if not data['mail']:
            return {'message': {
                "mail": "Mail cant be empty"
            }}, 400

        if not data['google_token']:
            return {'message': {
                "google_token": "Google token cant be empty"
            }}, 400

        user = UsersModel.find_by_mail(mail=data['mail'])

        if not user:
            return {'message': "Account with mail [{}] Not found".format(data['mail'])}, 404

        valid_password = user.verify_password(data['google_token'])

        if not valid_password:
            return {'message': "Invalid google token"}, 400

        token = user.generate_auth_token()
        return {'token': token.decode('ascii')}, 200