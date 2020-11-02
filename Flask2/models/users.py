from db import db

def get_user_roles(user):
    return ['admin'] if user.is_admin == 1 else ['user']

class UsersModel(db.Model):
    """
    Object DB SQL Model: Accounts
    """

    __tablename__ = 'users'
    id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False,autoincrement=True)
    name = db.Column(db.String(), nullable=False)
    surname = db.Column(db.String(), nullable=True)
    password = db.Column(db.String(), nullable=False)
    role = db.Column(db.Integer(), nullable=False)


    def __init__(self, name, surname,password,role=0):
        self.name = name
        self.surname = surname
        self.password = password
        self.role = role

    def json(self):
        """
        Converts Users to JSON and returns it
        Return: dict
        """
        return {
            'name': self.name,
            'surname': self.surname,
            'password': self.password,
            'role': self.role
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
    def all_users(cls):
        """
        Finds all AccountsModels and returns them
        Return: all AccountsModels
        """
        return UsersModel.query.all()