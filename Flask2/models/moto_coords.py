from db import db

class MotoCoordsModel(db.Model):
    """
    Object DB SQL Model: Moto coordinates
    """

    __tablename__ = 'moto_coords'
    moto_id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False)
    latitude = db.Column(db.Float(), nullable=False)
    longitude = db.Column(db.Float(), nullable=False)
    last_latitudes = db.Column(db.Float(), nullable=True)
    last_longitudes = db.Column(db.Float(), nullable=True)

    def __init__(self, moto_id, latitude, longitude):
        self.moto_id = moto_id
        self.latitude = latitude
        self.longitude = longitude
        self.last_latitudes = 0
        self.last_longitudes = 0

    def json(self, role):
        """
        Converts Moto coords to JSON and returns it
        Return: dict
        """
        if role == 1 or role == 2:
            return {
                'moto_id': self.moto_id,
                'latitude': self.latitude,
                'longitude': self.longitude,
                'last_latitudes': self.last_latitudes,
                'last_longitudes': self.last_longitudes
            }
        else:
            return {
                'latitude': self.latitude,
                'longitude': self.longitude
            }

    def save_to_db(self):
        """
        Adds moto coords into the database
        """
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        """
        Deletes moto coords from database
        """
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_moto_id(cls, moto_id):
        """
        Finds a moto by id
        Param: moto id
        Return: MotoCoordsModel
        """
        return MotoCoordsModel.query.filter_by(moto_id=moto_id).first()

    @classmethod
    def all_moto_coords(cls):
        """
        Finds all MotoCoordsModels and returns them
        Return: all MotoCoordsModels
        """
        return MotoCoordsModel.query.all()
