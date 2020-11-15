from db import db
from datetime import datetime, timedelta
from sqlalchemy import ForeignKey

class RentalsModel(db.Model):
    """
    Object DB SQL Model: Rentals
    """

    __tablename__ = 'rentals'

    id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False, autoincrement=True)
    moto_id = db.Column(db.Integer(), ForeignKey('moto.id'), nullable=False)
    user_id = db.Column(db.Integer(), ForeignKey('users.id'), nullable=False)
    active = db.Column(db.Boolean(), nullable=False)
    book_hour = db.Column(db.String(), nullable=False)
    finish_book_hour = db.Column(db.String(), nullable=False)
    finish_rental_hour = db.Column(db.String(), nullable=True)

    def __init__(self, moto_id, user_id, book_hour, finish_rental_hour=None):
        self.moto_id = moto_id
        self.user_id = user_id
        self.active = (finish_rental_hour is None)
        self.book_hour = book_hour
        self.finish_book_hour = self.__add_15_minutes_srting_datetime(book_hour)
        self.finish_rental_hour = finish_rental_hour

    def json(self):
        """
        Converts Rentals to JSON and returns it
        Return: dict
        """
        return {
            'id' : self.id,
            'user_id': self.user_id,
            'moto_id': self.moto_id,
            'active': self.active,
            'book_hour': self.book_hour,
            'finish_book_hour': self.finish_book_hour,
            'finish_rental_hour': self.finish_rental_hour
        }

    def save_to_db(self):
        """
        Adds a rental into the database
        """

        db.session.add(self)
        db.session.commit()


    def delete_from_db(self):
        """
        Deletes a rental into fron database
        """
        db.session.delete(self)
        db.session.commit()

    def update_finish_rent_hour(self, finish_rental_hour):
        """
        Updates finish_rental_hour and set active false
        :param finish_rental_hour:
        :return:
        """
        self.finish_rental_hour = finish_rental_hour
        self.active = False
        db.session.commit()

    @classmethod
    def find_by_id(cls, id):
        """
        Finds a Rental by id
        Param: number id
        Return: RentalModel
        """
        return RentalsModel.query.filter_by(id=id).first()

    @classmethod
    def all_rentals(cls):
        """
        Finds all RentalModels and returns them
        Return: all RentalsModels
        """
        return RentalsModel.query.all()

    def __add_15_minutes_srting_datetime(self, date):
        date_time = datetime.strptime(date, '%Y-%m-%dT%H:%M:%S.%f')
        date_time_finish = date_time + timedelta(minutes=15)
        return date_time_finish.isoformat()
