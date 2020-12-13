from flask_restful import Resource, reqparse
from models.payments import PaymentsModel
from models.users import auth
from flask import g

parser = reqparse.RequestParser()
parser.add_argument('id_payment', type=str, required=False, help="Payment id, This field cannot be left blank")
parser.add_argument('id_rental', type=str, required=False, help="Associated rental id, This field cannot be left blank")
parser.add_argument('id_bank_data', type=str, required=False, help="Associated bank data id, This field cannot be "
                                                                   "left blank")
parser.add_argument('payment_import', type=str, required=False, help="Payment import, This field cannot be left blank")
parser.add_argument('payment_date', type=str, required=False, help="Payment date, This field cannot be left blank")
parser.add_argument('user_id', type=str, required=False, help="User id, This field cannot be left blank")
parser.add_argument('moto_id', type=str, required=False, help="Moto id, This field cannot be left blank")


class Payment(Resource):
    """
    API Restful methods for Users
    """

    @auth.login_required(role='admin')
    def get(self, id_payment):
        """
        GET method
        Gets a payment by id
        Param: string id
        Return: dict (account ok / message)
        """

        data = parser.parse_args()

        payment = PaymentsModel.find_by_id(id_payment)

        if payment:
            return {'payment': payment.json()}, 200
        else:
            return {'message': "Payment with id [{}] Not found".format(data['id_payment'])}, 404

    def post(self):
        """
        POST method
        Adds a new payment
        Return: dict (payment created / message)
        """
        data = parser.parse_args()

        if not data['id_rental']:
            return {'message': {
                "id_rental": "Rental id cant be empty"
            }}, 400

        if not data['id_bank_data']:
            return {'message': {
                "id_bank_data": "Bank data id cant be empty"
            }}, 400

        if not data['payment_import']:
            return {'message': {
                "payment_import": "Import cant be empty"
            }}, 400

        if PaymentsModel.find_by_rental_id(data['id_rental']):
            return {'message': "Payment with rental id [{}] already exists".format(data['id_rental'])}, 409

        payment = PaymentsModel(id_rental=data['id_rental'], id_bank_data=data['id_bank_data'], payment_import=data['payment_import'])

        try:
            if data['payment_date']:
                payment.payment_date = data['payment_date']
            payment.save_to_db()
            return {'payment': PaymentsModel.find_by_rental_id(data['id_rental']).json()}, 201
        except:
            return {"message": "Error Description"}, 500

    @auth.login_required(role='admin')
    def put(self, id_payment):
        """
        PUT method
        Modifies a payment date
        Return: dict (payment modified)
        """
        data = parser.parse_args()

        if not data['payment_date']:
            return {'message': {
                "payment_date": "Specify import date"
            }}, 400

        payment = PaymentsModel.find_by_id(id_payment)

        if payment:
            payment.payment_date = data['payment_date']
            try:
                payment.save_to_db()
                return {'payment': PaymentsModel.find_by_id(id_payment).json()}, 200
            except:
                return {"message": "Error Description"}, 500
        else:
            return {'message': "Payment with id [{}] Not found".format(id_payment)}, 404

    @auth.login_required(role='admin')
    def delete(self, id_payment):
        """
        DELETE method
        Removes an account
        Param: string id
        Return: dict (message ok / message)
        """
        payment = PaymentsModel.find_by_id(id_payment)
        if payment:
            try:
                payment.delete_from_db()
                return {'message': "Payment with id [{}] deleted.".format(id_payment)}, 200
            except:
                return {"message": "Something went wrong."}, 500
        else:
            return {'message': "Payment with id [{}] Not found".format(id_payment)}, 404


class PaymentsList(Resource):
    """
    API Restful methods for PaymentsList
    """

    @auth.login_required(role='admin')
    def get(self):
        """
        GET method
        Return: dict (payments)
        """
        data = parser.parse_args()

        if data['user_id']:
            payments = PaymentsModel.find_by_user_id(data['user_id'])
            return {'payments': [payment.json() for payment in payments]}, 200

        if data['moto_id']:
            payments = PaymentsModel.find_by_moto_id(data['moto_id'])
            return {'payments': [payment.json() for payment in payments]}, 200

        payments = PaymentsModel.all_payments()
        return {'payments': [payment.json() for payment in payments]}, 200