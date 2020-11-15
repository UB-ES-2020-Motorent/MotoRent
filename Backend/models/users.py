from db import db
from sqlalchemy import ForeignKey


class UsersModel(db.Model):
    """
    Object DB SQL Model: Accounts
    """

    __tablename__ = 'users'
    id = db.Column(db.Integer(), ForeignKey('bank_data.user_id'), primary_key=True, unique=True, nullable=False, autoincrement=True)
    id_bank_data = db.Column(db.Integer(), nullable=True, unique=True)
    national_id_document = db.Column(db.String(), nullable=True)
    country = db.Column(db.String(), nullable=True)
    name = db.Column(db.String(), nullable=True)
    surname = db.Column(db.String(), nullable=True)
    mail = db.Column(db.String(), nullable=False, unique=True)
    google_token = db.Column(db.String(), nullable=False, unique=True)
    role = db.Column(db.Integer(), nullable=False)

    def __init__(self, mail, google_token, role):
        self.national_id_document = None
        self.country = None
        self.name = None
        self.surname = None
        self.mail = mail
        self.google_token = google_token
        self.role = role
        self.id_bank_data = None

    def json(self, role):
        """
        Converts Users to JSON and returns it
        Return: dict
        """
        if role == 1:
            return {
                'id': self.id,
                'id_bank_data': self.id_bank_data,
                'national_id_document': self.national_id_document,
                'country': self.country,
                'name': self.name,
                'surname': self.surname,
                'mail': self.mail,
                'google_token': self.google_token,
                'role': self.role
            }
        else:
            return {
                'name': self.name,
                'surname': self.surname,
                'country': self.country
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
    def find_by_id(cls, user_id):
        """
        Finds an Artist by ID
        Param: number id
        Return: UsersModel
        """
        return UsersModel.query.filter_by(id=user_id).first()

    @classmethod
    def find_by_mail(cls, mail):
        """
        Finds an Artist by ID
        Param: number id
        Return: UsersModel
        """
        return UsersModel.query.filter_by(mail=mail).first()

    @classmethod
    def find_by_id_bank_data(cls, id_bank_data):
        """
        Finds an Artist by ID
        Param: number id
        Return: UsersModel
        """
        return UsersModel.query.filter_by(id_bank_data=id_bank_data).first()

    @classmethod
    def find_by_google_token(cls, google_token):
        """
        Finds an Artist by Google token
        Param: number google token
        Return: UsersModel
        """
        return UsersModel.query.filter_by(google_token=google_token).first()

    @classmethod
    def all_users(cls):
        """
        Finds all AccountsModels and returns them
        Return: all AccountsModels
        """
        return UsersModel.query.all()