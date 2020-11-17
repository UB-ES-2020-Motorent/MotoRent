from db import db

class MotosModel(db.Model):
    """
    Object DB SQL Model: Motos
    """
    __tablename__ = 'motos'
    id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False,autoincrement=True)
    license_number = db.Column(db.String(), nullable=False)
    battery = db.Column(db.Integer(), nullable=False)
    available = db.Column(db.Boolean(), nullable=False)
    latitude = db.Column(db.Float(), nullable=False)
    longitude = db.Column(db.Float(), nullable=False)

    def __init__(self, license_number, battery, latitude, longitude):
        self.license_number = license_number
        self.battery = battery
        self.available = battery > 15.0
        self.latitude = latitude
        self.longitude = longitude

    def json(self):
        """
        Converts Motos to JSON and returns it
        Return: dict
        """
        return {
            'id': self.id,
            'license_number': self.license_number,
            'battery': self.battery,
            'available': self.available,
            'latitude': self.latitude,
            'longitude': self.longitude
        }
    def save_to_db(self):
        """
        Adds a moto into the database
        """
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        """
        Deletes a moto from database
        """
        db.session.delete(self)
        db.session.commit()

    def set_available(self, available):
        self.available = available
        db.session.commit()

    @classmethod
    def find_by_id(cls, id):
        """
        Finds an user by id
        Param: number id
        Return: MotosModel
        """
        return MotosModel.query.filter_by(id=id).first()

    @classmethod
    def all_motos(cls):
        """
        Finds all MotosModel and returns them
        Return: all MotosModels
        """

        return MotosModel.query.all()

    @classmethod
    def get_available_motos(cls, available):
        """
        Finds availiable MotosModel and returns them
        Return: all available MotosModels
        """
        return MotosModel.query.filter_by(available=available).all()

    @classmethod
    def find_by_license_number(cls, license_number):
        """
        Finds a moto by license_number
        Param: number license_number
        Return: MotosModel
        """
        return MotosModel.query.filter_by(license_number=license_number).first()




