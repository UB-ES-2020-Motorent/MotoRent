from db import db


class UsersModel(db.Model):
    """
    Object DB SQL Model: Accounts
    """

    __tablename__ = 'users'
    id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False,autoincrement=True)
    name = db.Column(db.String(), nullable=False)
    surname = db.Column(db.String(), nullable=True)
    mail = db.Column(db.String(), nullable=False)
    google_token = db.Column(db.String(), nullable=False)
    role = db.Column(db.Integer(), nullable=False)

    def __init__(self, name, surname, mail, google_token, role=0):
        self.name = name
        self.surname = surname
        self.mail = mail
        self.google_token = google_token
        self.role = role

    def json(self, role):
        """
        Converts Users to JSON and returns it
        Return: dict
        """
        if role == 1:
            return {
                'id': self.id,
                'name': self.name,
                'surname': self.surname,
                'mail': self.mail,
                'google_token': self.google_token,
                'role': self.role
            }
        else:
            return {
                'name': self.name,
                'surname': self.surname
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
    def find_by_username(cls, name):
        """
        Finds an user by username
        Param: number id
        Return: UsertModel
        """
        return UsersModel.query.filter_by(name=name).first()

    @classmethod
    def find_by_id(cls, user_id):
        """
        Finds an Artist by ID
        Param: number id
        Return: ArtistModel
        """
        return UsersModel.query.filter_by(id=user_id).first()

    @classmethod
    def all_users(cls):
        """
        Finds all AccountsModels and returns them
        Return: all AccountsModels
        """
        return UsersModel.query.all()