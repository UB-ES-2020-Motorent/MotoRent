from flask_restful import Resource, reqparse
from models.accounts import AccountsModel

parser = reqparse.RequestParser()
parser.add_argument('username', type=str, required=True, help="Account username, This field cannot be left blank")
parser.add_argument('password', type=str, required=True, help="Account password, This field cannot be left blank")


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

        if not data['username']:
            return {'message': {
                "username": "Username cant be empty"
            }}, 400

        if not data['password']:
            return {'message': {
                "password": "Password cant be empty"
            }}, 400

        account = AccountsModel.find_by_username(username=data['username'])

        if not account:
            return {'message': "Account with username [{}] Not found".format(data['username'])}, 404

        valid_password = account.verify_password(data['password'])

        if not valid_password:
            return {'message': "Invalid password"}, 400

        token = account.generate_auth_token()
        return {'token': token.decode('ascii')}, 200