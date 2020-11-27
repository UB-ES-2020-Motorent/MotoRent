from flask_restful import Resource, reqparse
from models.bank_data import BankDataModel
from models.users import UsersModel

parser = reqparse.RequestParser()
parser.add_argument('id_bank_data', type=str, required=False, help="Account bank data id, This field cannot be left "
                                                                   "blank")
parser.add_argument('user_id', type=str, required=False, help="Associated id, This field cannot be left blank")
parser.add_argument('card_number', type=str, required=False, help="Card number, This field cannot be left blank")
parser.add_argument('card_owner', type=str, required=False, help="Card owner, This field cannot be left blank")
parser.add_argument('card_cvv', type=str, required=False, help="Card CVV, This field cannot be left blank")
parser.add_argument('card_expiration', type=str, required=False, help="Card expiration date,"
                                                                      " This field cannot be left blank")
parser.add_argument('view_all', type=str, required=False, help="True for all cards, False for only default card")


class BankData(Resource):
    """
    API Restful methods for BankData
    """

    def get(self):
        """
        GET method
        Gets bank data by user id, bank data id or card number
        Param: string user id, bank data id or card number
        Return: dict (account ok / message)
        """

        data = parser.parse_args()

        arguments = sum([1 if value else 0 for value in data.values()])

        if (arguments > 2 and data['view_all']) or (arguments > 1 and not data['view_all']):
            return {'message': "Please filter by only one feature"}, 400
        if (arguments <= 1 and data['view_all']) or (arguments <= 0 and not data['view_all']):
            return {'message': "Please filter by at least one feature: user id, bank data id or card number"}, 400

        if data['user_id']:
            if str(data['view_all']).lower() == 'true':
                bank_datas = BankDataModel.find_by_user_id(user_id=data['user_id'], view_all=True)
            else:
                bank_datas = BankDataModel.find_by_user_id(user_id=data['user_id'], view_all=False)
            if bank_datas:
                return {'BankData': [bank_data.json() for bank_data in bank_datas]}, 200
            else:
                return {'message': "Data for {} [{}] Not found".format('user with id', data['user_id'])}, 404
        elif data['id_bank_data']:
            bank_data = BankDataModel.find_by_id_bank_data(id_bank_data=data['id_bank_data'])
            if bank_data:
                return {'BankData': bank_data.json()}, 200
            else:
                return {'message': "Data with {} [{}] Not found".format('ID bank data', data['id_bank_data'])}, 404
        elif data['card_number']:
            bank_datas = BankDataModel.find_by_card_number(card_number=data['card_number'])
            if bank_datas:
                return {'BankData': [bank_data.json() for bank_data in bank_datas]}, 200
            else:
                return {'message': "Data with {} [{}] Not found".format('card number', data['card_number'])}, 404
        else:
            return {'message': "Please filter by a valid feature: user id, bank data id or card number"}, 400


    def post(self):
        """
        POST method
        Adds new bank data
        Return: dict (account created / message)
        """
        data = parser.parse_args()

        if not data['user_id']:
            return {'message': {
                "user_id": "User id cant be empty"
            }}, 400

        if not data['card_number']:
            return {'message': {
                "card_number": "Card number cant be empty"
            }}, 400

        if not data['card_owner']:
            return {'message': {
                "card_owner": "Card owner cant be empty"
            }}, 400

        if not data['card_cvv']:
            return {'message': {
                "card_cvv": "Card CVV cant be empty"
            }}, 400

        if not data['card_expiration']:
            return {'message': {
                "card_expiration": "Card expiration date cant be empty"
            }}, 400

        if BankDataModel.find_by_user_id_and_card_number(data['user_id'], data['card_number']):
            return {'message': "Data with user id [{}] and card number [{}] already exists".format(
                data['user_id'], data['card_number'])}, 409

        user = UsersModel.find_by_id(data['user_id'])

        if user:
            bdata = BankDataModel(user_id=data['user_id'], card_number=data['card_number'], card_owner=data['card_owner'],
                                 card_cvv=data['card_cvv'], card_expiration=data['card_expiration'])
            try:
                bdata.save_to_db()
                try:
                    if not user.id_bank_data:
                        user.id_bank_data = bdata.id_bank_data
                    user.save_to_db()
                except:
                    return {'BankData': "Bank data was created, but could not update user card preferences"}, 201
                return {'message': BankDataModel.find_by_user_id_and_card_number(bdata.user_id, bdata.card_number).json()}, 201
            except:
                return {"message": "Error Description"}, 500
        else:
            return {'message': "User with id [{}] does not exist".format(data['user_id'])}, 409

    def put(self, id_bank_data):
        """
        PUT method
        Modifies data bank
        Param: id bank data
        Return: dict (user created)
        """
        data = parser.parse_args()

        bdata = BankDataModel.find_by_id_bank_data(id_bank_data=id_bank_data)

        if data['id_bank_data']:
            bdata.id_bank_data = data["id_bank_data"]

        if data['user_id']:
            bdata.user_id = data["user_id"]

        if data['card_number']:
            bdata.card_number = data["card_number"]

        if data['card_owner']:
            bdata.card_owner = data["card_owner"]

        if data['card_cvv']:
            bdata.card_cvv = data["card_cvv"]

        if data['card_expiration']:
            bdata.card_expiration = data["card_expiration"]

        try:
            bdata.save_to_db()
            return {'BankData': BankDataModel.find_by_id_bank_data(id_bank_data).json()}, 200
        except:
            return {"message": "Error Description"}, 500

    def delete(self, id_bank_data):
        """
        DELETE method
        Removes bank data
        Param: string bank data ip
        Return: dict (message ok / message)
        """

        bdata = BankDataModel.find_by_id_bank_data(id_bank_data=id_bank_data)

        if bdata:
            try:
                bdata.delete_from_db()
                return {'message': "Data with bank data id [{}] and all associated info deleted".format(id_bank_data)
                        }, 200
            except:
                return {"message": "Error Description"}, 500
        else:
            return {'message': "Data with bank data id [{}] Not found".format(id_bank_data)}, 404


class BankDataList(Resource):
    """
    API Restful methods for BankDataList
    """
    def get(self):
        """
        GET method
        Return: dict (bank datas)
        """
        bank_datas = BankDataModel.all_bank_data()

        return {'All BankData': [data.json() for data in bank_datas]}, 200
