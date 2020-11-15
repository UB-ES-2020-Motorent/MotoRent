from db import db
from sqlalchemy import ForeignKey


class BankDataModel(db.Model):
    """
    Object DB SQL Model: Bank Data
    """

    __tablename__ = 'bank_data'

    id_bank_data = db.Column(db.Integer(), primary_key=True, nullable=False, unique=True, autoincrement=True)
    user_id = db.Column(db.Integer(), ForeignKey('users.id'), nullable=False)
    card_number = db.Column(db.String(), nullable=False)
    card_owner = db.Column(db.String(), nullable=False)
    card_cvv = db.Column(db.Integer(), nullable=False)
    card_expiration = db.Column(db.String(), nullable=False)

    def __init__(self, user_id, card_number, card_owner, card_cvv, card_expiration):
        self.user_id = user_id
        self.card_number = card_number
        self.card_owner = card_owner
        self.card_cvv = card_cvv
        self.card_expiration = card_expiration

    def json(self):
        """
        Converts bank data to JSON and returns it
        Return: dict
        """
        return {
            'user_id': self.user_id,
            'card_number': self.card_number,
            'card_owner': self.card_owner,
            'card_cvv': self.card_cvv,
            'card_expiration': self.card_expiration,
        }

    def save_to_db(self):
        """
        Adds an user into the database
        """
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        """
        Deletes an user from database
        """
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_user_id(cls, user_id):
        """
        Finds bank data by user id
        Param: number user id
        Return: BankDataModel
        """
        return BankDataModel.query.filter_by(user_id=user_id).all()

    @classmethod
    def find_by_id_bank_data(cls, id_bank_data):
        """
        Finds bank data by bank data id
        Param: number bank data id
        Return: BankDataModel
        """
        return BankDataModel.query.filter_by(id_bank_data=id_bank_data).first()

    @classmethod
    def find_by_card_number(cls, card_number):
        """
        Finds bank data by card number
        Param: number card number
        Return: BankDataModel
        """
        return BankDataModel.query.filter_by(card_number=card_number).all()

    @classmethod
    def find_by_user_id_and_card_number(cls, user_id, card_number):
        """
        Finds bank data by user id and card number
        Param: number user id and card number
        Return: BankDataModel
        """
        return BankDataModel.query.filter_by(user_id=user_id, card_number=card_number).first()

    @classmethod
    def all_bank_data(cls):
        """
        Finds all AccountsModels and returns them
        Return: all BankDataModels
        """
        return BankDataModel.query.all()