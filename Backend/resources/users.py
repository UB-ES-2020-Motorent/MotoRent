from flask_restful import Resource, reqparse
from models.users import UsersModel

parser = reqparse.RequestParser()
parser.add_argument('national_id_document', type=str, required=False, help="Associated national id document, This "
                                                                           "field cannot be left blank")
parser.add_argument('country', type=str, required=False, help="Associated country, This field cannot be left blank")
parser.add_argument('name', type=str, required=False, help="Account name, This field cannot be left blank")
parser.add_argument('surname', type=str, required=False, help="Account surname, This field cannot be left blank")
parser.add_argument('mail', type=str, required=False, help="Account mail, This field cannot be left blank")
parser.add_argument('google_token', type=str, required=False, help="Associated token, This field cannot be left blank")
parser.add_argument('role', type=str, required=False, help="Account role, This field cannot be left blank")
parser.add_argument('id_bank_data', type=str, required=False, help="Account bank data id, This field cannot be left "
                                                                   "blank")
parser.add_argument('admin_code', type=str, required=False, help="Account code for admins for special post")


class Users(Resource):
    """
    API Restful methods for Accounts
    """

    def get(self, user_id):
        """
        GET method
        Gets an account by id
        Param: string id
        Return: dict (account ok / message)
        """

        data = parser.parse_args()

        user = UsersModel.find_by_id(user_id=user_id)

        if user:
            if not data['admin_code']:
                return {'user': user.json(0)}, 200
            else:
                if data['admin_code'] != 'admin_secret_code':
                    return {'message': "Wrong admin code"}, 400
                else:
                    return {'user': user.json(1)}, 200
        else:
            return {'message': "User with id [{}] Not found".format(user_id)}, 404

    def post(self):
        """
        POST method
        Adds a new account
        Return: dict (account created / message)
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

        if not data['role']:
            return {'message': {
                "role": "Role cant be empty"
            }}, 400

        if UsersModel.find_by_mail(data['mail']):
            return {'message': "Account with mail [{}] already exists".format(data['mail'])}, 409

        user = UsersModel(google_token=data['google_token'], mail=data['mail'], role=data['role'])

        if data['role'] != '0':
            if not data['admin_code']:
                return {"message": "You need admin code to create an user with this role"}, 400
            else:
                if data['admin_code'] != 'admin_secret_code':
                    return {"message": "Wrong admin code"}, 400
                else:
                    try:
                        user.save_to_db()
                        return {'user': UsersModel.find_by_mail(user.mail).json(1)}, 201
                    except:
                        return {"message": "Error Description"}, 500
        else:
            try:
                user.save_to_db()
                return {'user': UsersModel.find_by_mail(user.mail).json(0)}, 201
            except:
                return {"message": "Error Description"}, 500

    def put(self, user_id):
        """
        PUT method
        Modifies a user
        Param: id
        Return: dict (user created)
        """
        data = parser.parse_args()

        if not data['admin_code']:
            return {"message": "You need an admin code to modify user data"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
                user = UsersModel.find_by_id(user_id=user_id)

                if data['name']:
                    user.name = data["name"]

                if data['surname']:
                    user.surname = data["surname"]

                if data['national_id_document']:
                    user.national_id_document = data["national_id_document"]

                if data['country']:
                    user.country = data["country"]

                if data['mail']:
                    user.mail = data["mail"]

                if data['google_token']:
                    user.google_token = data["google_token"]

                if data['role']:
                    user.role = data["role"]

                if data['id_bank_data']:
                    user.id_bank_data = data["id_bank_data"]

                try:
                    user.save_to_db()
                    return {'user': UsersModel.find_by_id(user_id).json(1)}, 200
                except:
                    return {"message": "Error Description"}, 500

    def delete(self, user_id):
        """
        DELETE method
        Removes an account
        Param: string id
        Return: dict (message ok / message)
        """

        data = parser.parse_args()

        user = UsersModel.find_by_id(user_id=user_id)

        if not data['admin_code']:
            return {"message": "You need an admin code to delete an user"}, 400
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {"message": "Wrong admin code"}, 400
            else:
                if user:
                    try:
                        user.delete_from_db()
                        return {'message': "User with id [{}] and all associated info deleted".format(
                            user_id)}, 200
                    except:
                        return {"message": "Error Description"}, 500
                else:
                    return {'message': "User with id [{}] Not found".format(user_id)}, 404


class UsersList(Resource):
    """
    API Restful methods for UsersList
    """
    def get(self):
        """
        GET method
        Return: dict (users)
        """
        data = parser.parse_args()

        users = UsersModel.all_users()

        if not data['admin_code']:
            return {'users': [user.json(0) for user in users]}, 200
        else:
            if data['admin_code'] != 'admin_secret_code':
                return {'message': "Wrong admin code"}, 400
            else:
                return {'users': [user.json(1) for user in users]}, 200
