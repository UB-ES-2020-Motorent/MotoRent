from db import db
from passlib.apps import custom_app_context as pwd_context
from itsdangerous import (TimedJSONWebSignatureSerializer as Serializer, BadSignature, SignatureExpired)
from flask_httpauth import HTTPBasicAuth
from flask import g, current_app

auth = HTTPBasicAuth()


def verify_password(token, password):
    user = AccountsModel.verify_auth_token(token)
    if user:
        g.user = user
        return user


def get_user_roles(user):
    return ['admin'] if user.is_admin == 1 else ['user']


class AccountsModel(db.Model):
    """
    Object DB SQL Model: Accounts
    """

    __tablename__ = 'accounts'
    username = db.Column(db.String(30), primary_key=True, unique=True, nullable=False)
    password = db.Column(db.String(), nullable=False)
    # 0 not admin / 1 is admin
    is_admin = db.Column(db.Integer, nullable=False)


    def __init__(self, username, is_admin=0):
        self.username = username
        self.is_admin = is_admin

    def hash_password(self, password):
        """
        Encrypts and adds a password to user
        """
        self.password = pwd_context.encrypt(password)

    def verify_password(self, password):
        """
        Cheques if password is correct
        """
        return pwd_context.verify(password, self.password)

    def json(self):
        """
        Converts Accounts to JSON and returns it
        Return: dict
        """
        return {
            'username': self.username,
            'is_admin': self.is_admin,
        }

    def save_to_db(self):
        """
        Adds an Account into the database
        """
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        """
        Deletes an Account from database
        """
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_username(cls, username):
        """
        Finds an account by username
        Param: number id
        Return: ArtistModel
        """
        return AccountsModel.query.filter_by(username=username).first()

    @classmethod
    def all_accounts(cls):
        """
        Finds all AccountsModels and returns them
        Return: all AccountsModels
        """
        return AccountsModel.query.all()

    def generate_auth_token(self, expiration=600):
        """
        Return a valid token with username information incrusted
        and expiration defined by and input value
        """
        s = Serializer(current_app.secret_key, expires_in=expiration)
        return s.dumps({'username': self.username})

    @classmethod
    def verify_auth_token(cls, token):
        """
        if input token is valid is going to extract username information
        and return this user
        """
        s = Serializer(current_app.secret_key)
        try:
            data = s.loads(token)
        except SignatureExpired:
            return None  # valid token, but expired
        except BadSignature:
            return None  # invalid token
        user = cls.query.filter_by(username=data['username']).first()
        return user