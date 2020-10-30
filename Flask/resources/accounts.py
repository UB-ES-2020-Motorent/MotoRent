from flask_restful import Resource, reqparse
from models.accounts import AccountsModel

parser = reqparse.RequestParser()
parser.add_argument('username', type=str, required=True, help="Account username, This field cannot be left blank")
parser.add_argument('password', type=str, required=True, help="Account password, This field cannot be left blank")
# admin only by terminal


class Accounts(Resource):
    """
    API Restful methods for Accounts
    """

    def get(self, username):
        """
        GET method
        Gets an account by username
        Param: string username
        Return: dict (account ok / message)
        """

        account = AccountsModel.find_by_username(username=username)
        if account:
            return {'account': account.json()}, 200
        else:
            return {'message': "Account with username [{}] Not found".format(username)}, 404

    def post(self):
        """
        POST method
        Adds a new account
        Return: dict (account created / message)
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

        if AccountsModel.find_by_username(username=data['username']):
            return {'message': "Account with username [{}] already exists".format(data['username'])}, 409

        account = AccountsModel(username=data['username'])
        account.hash_password(data['password'])

        try:
            account.save_to_db()
            return {'account': AccountsModel.find_by_username(account.username).json() }, 201
        except:
            return {"message": "Error Description"}, 500

    def delete(self, username):
        """
        DELETE method
        Removes an account
        Param: string username
        Return: dict (message ok / message)
        """

        account = AccountsModel.find_by_username(username=username)
        if account:
            try:
                account.delete_from_db()
                return {'message': "User with username [{}] and all associated orders deleted".format(username)}, 200
            except:
                return {"message": "Error Description"}, 500
        else:
            return {'message': "User with username [{}] Not found".format(username)}, 404


class AccountsList(Resource):
    """
    API Restful methods for AccountsList
    """

    def get(self):
        """
        GET method
        Return: dict (accounts)
        """
        accounts = AccountsModel.all_accounts()
        return {'accounts': [account.json() for account in accounts]}, 200