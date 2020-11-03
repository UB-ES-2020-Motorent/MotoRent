from flask_restful import Resource, reqparse
from models.users import UsersModel

parser = reqparse.RequestParser()
parser.add_argument('name', type=str, required=True, help="Account username, This field cannot be left blank")
parser.add_argument('surname', type=str, required=True, help="Account surname, This field cannot be left blank")
parser.add_argument('mail', type=str, required=True, help="Account mail, This field cannot be left blank")
parser.add_argument('google_token', type=str, required=True, help="Associated token, This field cannot be left blank")
parser.add_argument('role', type=str, required=True, help="Account role, This field cannot be left blank")
parser.add_argument('admin_code', type=str, required=False, help="Account code for admins for special post")

admin_parser = reqparse.RequestParser()
admin_parser.add_argument('admin_code', type=str, required=False, help="Account code for admins for special post")


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

        data = admin_parser.parse_args()

        user = UsersModel.find_by_username(name=name)

        if user:
            if not data['admin_code']:
                return {'user': user.json(0)}, 200
            else:
                if data['admin_code'] != 'admin_secret_code':
                    return {'message': "Wrong admin code"}, 400
                else:
                    return {'user': user.json(1)}, 200
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

        if not data['mail']:
            return {'message': {
                "mail": "Mail cant be empty"
            }}, 400

        if not data['google_token']:
            return {'message': {
                "google_token": "Google token cant be empty"
            }}, 400

        if UsersModel.find_by_username(name=data['name']):
            return {'message': "Account with name [{}] already exists".format(data['name'])}, 409

        user = UsersModel(name=data['name'], surname=data['surname'], google_token=data['google_token'],
                          role=data['role'], mail=data['mail'])

        if data['role'] != '0':
            if not data['admin_code']:
                return {"message": "You need admin code to create an user with this role"}, 400
            else:
                if data['admin_code'] != 'admin_secret_code':
                    return {"message": "Wrong admin code"}, 400
                else:
                    try:
                        user.save_to_db()
                        return {'user': UsersModel.find_by_username(user.name).json(1)}, 201
                    except:
                        return {"message": "Error Description"}, 500
        else:
            try:
                user.save_to_db()
                return {'user': UsersModel.find_by_username(user.name).json(0)}, 201
            except:
                return {"message": "Error Description"}, 500

    def put(self, name):
        """
        PUT method
        Modifies/Adds a user
        Param: name (OPTIONAL)
        Return: dict (user created)
        """
        data = parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to modify user data"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
                if not data['name']:
                    return {'message': {
                        "name": "Name cant be empty"
                    }}, 400

                if not data['surname']:
                    return {'message': {
                        "surname": "Surname cant be empty"
                    }}, 400
                if not data['mail']:
                    return {'message': {
                        "mail": "Mail cant be empty"
                    }}, 400
                if not data['google_token']:
                    return {'message': {
                        "google_token": "Google token cant be empty"
                    }}, 400
                if not data['role']:
                    return {'message': {
                        "role": "Role cant be empty"
                    }}, 400

                user = UsersModel.find_by_id(name)

                if not user:
                    user = UsersModel(name=data['name'], surname=data['surname'], google_token=data['google_token'],
                                      role=data['role'], mail=data['mail'])
                    try:
                        user.save_to_db()
                        return {'user': UsersModel.find_by_username(user.name).json(1)}, 201
                    except:
                        return {"message": "Error Description"}, 500

                user.name = data["name"]
                user.surname = data["surname"]
                user.mail = data["mail"]
                user.google_token = data["google_token"]
                user.role = data["role"]

                try:
                    user.save_to_db()
                    return {'user': UsersModel.find_by_id(user.id).json(1)}, 200
                except:
                    return {"message": "Error Description"}, 500


    def delete(self, name):
        """
        DELETE method
        Removes an account
        Param: string username
        Return: dict (message ok / message)
        """

        data = admin_parser.parse_args()

        user = UsersModel.find_by_username(name=name)

        if not data['admin_code']:
            return {"message": "You need an admin code to delete an user"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
                if user:
                    try:
                        user.delete_from_db()
                        return {'message': "User with username [{}] and all associated info deleted".format(
                            name)}, 200
                    except:
                        return {"message": "Error Description"}, 500
                else:
                    return {'message': "User with username [{}] Not found".format(name)}, 404


class UsersList(Resource):
    """
    API Restful methods for UsersList
    """
    def get(self):
        """
        GET method
        Return: dict (users)
        """
        data = admin_parser.parse_args()

        users = UsersModel.all_users()

        if not data['admin_code']:
            return {'users': [user.json(0) for user in users]}, 200
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {'message': "Wrong admin code"}, 400
            else:
                return {'users': [user.json(1) for user in users]}, 200
