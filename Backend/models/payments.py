from db import db
from models.rentals import RentalsModel


class PaymentsModel(db.Model):
    """
    Object DB SQL Model: Payments
    """

    __tablename__ = 'payments'
    id_payment = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False, autoincrement=True)
    id_rental = db.Column(db.Integer(), nullable=False, unique=True)
    id_bank_data = db.Column(db.Integer(), nullable=False, unique=False)
    payment_import = db.Column(db.Float(), nullable=False, unique=False)
    payment_date = db.Column(db.String(), nullable=True, unique=False)

    def __init__(self, id_rental, id_bank_data, payment_import, payment_date=None):
        self.id_rental = id_rental
        self.id_bank_data = id_bank_data
        self.payment_import = payment_import
        self.payment_date = payment_date

    def json(self):
        """
        Converts Payment to JSON and returns it
        Return: dict
        """
        return {
            'id_payment': self.id_payment,
            'id_rental': self.id_rental,
            'id_bank_data': self.id_bank_data,
            'payment_import': self.payment_import,
            'payment_date': self.payment_date
        }

    def save_to_db(self):
        """
        Adds a payment into the database
        """
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        """
        Deletes a payment from database
        """
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_id(cls, id_payment):
        """
        Finds a Payment by ID
        Param: payment id
        Return: PaymentsModel
        """
        return PaymentsModel.query.filter_by(id_payment=id_payment).first()

    @classmethod
    def find_by_rental_id(cls, id_rental):
        """
        Finds a Payment by its rental ID
        Param: number id
        Return: PaymentsModel
        """
        return PaymentsModel.query.filter_by(id_rental=id_rental).first()

    @classmethod
    def find_by_user_id(cls, user_id):
        """
        Finds all payments from a user by ID
        Param: number id
        Return: PaymentsModel
        """
        all_user_rentals = RentalsModel.find_by_user_id(user_id)
        payments_list = []
        for rental in all_user_rentals:
            payments_list.append(PaymentsModel.query.filter_by(id_rental=rental.id).first())
        return payments_list

    @classmethod
    def find_by_moto_id(cls, moto_id):
        """
        Finds all payments from a user by ID
        Param: number id
        Return: PaymentsModel
        """
        all_moto_rentals = RentalsModel.find_by_moto_id(moto_id)
        payments_list = []
        for rental in all_moto_rentals:
            payments_list.append(PaymentsModel.query.filter_by(id_rental=rental.id).first())
        return payments_list

    @classmethod
    def all_payments(cls):
        """
        Finds all PaymentsModels and returns them
        Return: all PaymentsModel
        """
        return PaymentsModel.query.all()
