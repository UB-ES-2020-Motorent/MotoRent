from db import db
from datetime import datetime, timedelta
from sqlalchemy import ForeignKey

class RentalsModel(db.Model):
    """
    Object DB SQL Model: Rentals
    """

    __tablename__ = 'rentals'

    id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False, autoincrement=True)
    moto_id = db.Column(db.Integer(), nullable=False)
    user_id = db.Column(db.Integer(), nullable=False)
    active = db.Column(db.Boolean(), nullable=False)
    book_hour = db.Column(db.String(), nullable=False)
    finish_book_hour = db.Column(db.String(), nullable=True)
    finish_rental_hour = db.Column(db.String(), nullable=True)

    def __init__(self, moto_id, user_id, book_hour, finish_book_hour=None, finish_rental_hour=None):
        self.moto_id = moto_id
        self.user_id = user_id
        self.active = (finish_rental_hour is None)
        self.book_hour = book_hour
        self.finish_book_hour = finish_book_hour
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
    def update_finish_book_hour(self, finish_book_hour):
        """
        Updates finish_book_hour and set active false
        :param finish_book_hour:
        :return:
        """
        self.finish_book_hour = finish_book_hour
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
    def find_by_user_id(cls, user_id):
        """
        Finds a Rental by id
        Param: number id
        Return: RentalModel
        """
        return RentalsModel.query.filter_by(user_id=user_id).all()

    @classmethod
    def find_by_moto_id(cls, moto_id):
        """
        Finds a Rental by id
        Param: number id
        Return: RentalModel
        """
        return RentalsModel.query.filter_by(moto_id=moto_id).all()

    @classmethod
    def find_active_rental_by_user_id(cls, user_id):
        """
        Finds a Rental by id
        Param: number id
        Return: RentalModel
        """
        return RentalsModel.query.filter_by(user_id=user_id, active=True).first()

    @classmethod
    def all_rentals(cls):
        """
        Finds all RentalModels and returns them
        Return: all RentalsModels
        """
        return RentalsModel.query.all()

    @classmethod
    def find_duration_by_id(cls, id):
        """
        Finds a Rental by id
        Param: number id
        Return: RentalModel
        """
        rental = RentalsModel.query.filter_by(id=id).first()
        if rental:
            start_rental = datetime.strptime(rental.book_hour, '%Y-%m-%dT%H:%M:%S.%f')
            finish_rental = datetime.strptime(rental.finish_rental_hour, '%Y-%m-%dT%H:%M:%S.%f')
            total_time = (finish_rental - start_rental).total_seconds() / 60.0
            return total_time
        else:
            return 0


def add_15_minutes_srting_datetime(date):
    date_time = datetime.strptime(date, '%Y-%m-%dT%H:%M:%S.%f')
    date_time_finish = date_time + timedelta(minutes=15)
    return date_time_finish.isoformat()