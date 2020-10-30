from flask_restful import Resource, reqparse
from models.users import UsersModel

parser = reqparse.RequestParser()
parser.add_argument('name', type=str, required=True, help="Account username, This field cannot be left blank")
parser.add_argument('surname', type=str, required=True, help="Account surname, This field cannot be left blank")
parser.add_argument('password', type=str, required=True, help="Account password, This field cannot be left blank")
parser.add_argument('role', type=str, required=True, help="Account role, This field cannot be left blank")


class Users(Resource):
    """
    API Restful methods for Accounts
    """

    def get(self, name):
        """
        GET method
        Gets an account by username
        Param: string username
        Return: dict (account ok / message)
        """

        user = UsersModel.find_by_username(name=name)
        if user:
            return {'user': user.json()}, 200
        else:
            return {'message': "User with username [{}] Not found".format(name)}, 404

    def post(self):
        """
        POST method
        Adds a new account
        Return: dict (account created / message)
        """
        data = parser.parse_args()

        if not data['name']:
            return {'message': {
                "name": "Name cant be empty"
            }}, 400

        if not data['password']:
            return {'message': {
                "password": "Password cant be empty"
            }}, 400

        if UsersModel.find_by_username(name=data['name']):
            return {'message': "Account with name [{}] already exists".format(data['name'])}, 409

        user = UsersModel(name=data['name'],surname=data['surname'],password=data['password'],role=data['role'])

        try:
            user.save_to_db()
            return {'user': UsersModel.find_by_username(user.name).json()}, 201
        except:
            return {"message": "Error Description"}, 500

    def delete(self, name):
        """
        DELETE method
        Removes an account
        Param: string username
        Return: dict (message ok / message)
        """

        user = UsersModel.find_by_username(name=name)
        if user:
            try:
                user.delete_from_db()
                return {'message': "User with username [{}] and all associated orders deleted".format(name)}, 200
            except:
                return {"message": "Error Description"}, 500
        else:
            return {'message': "User with username [{}] Not found".format(name)}, 404


class UsersList(Resource):
    """
    API Restful methods for AccountsList
    """

    def get(self):
        """
        GET method
        Return: dict (accounts)
        """
        users = UsersModel.all_users()
        return {'users': [user.json() for user in users]}, 200