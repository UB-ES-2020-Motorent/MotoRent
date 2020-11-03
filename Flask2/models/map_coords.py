from db import db

class MapCoordsModel(db.Model):
    """
    Object DB SQL Model: Map coordinates
    """

    __tablename__ = 'map_coords'
    id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False, autoincrement=True)
    from_latitude = db.Column(db.Float(), nullable=False)
    from_longitude = db.Column(db.Float(), nullable=False)
    to_latitude = db.Column(db.Float(), nullable=False)
    to_longitude = db.Column(db.Float(), nullable=False)

    def __init__(self, from_latitude, from_longitude, to_latitude, to_longitude):
        self.from_latitude = from_latitude
        self.from_longitude = from_longitude
        self.to_latitude = to_latitude
        self.to_longitude = to_longitude

    def json(self):
        """
        Converts Map coords to JSON and returns it
        Return: dict
        """
        return {
            'from_latitude': self.from_latitude,
            'from_longitude': self.from_longitude,
            'to_latitude': self.to_latitude,
            'to_longitude': self.to_longitude,
        }

    def save_to_db(self):
        """
        Adds map coords into the database
        """
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        """
        Deletes map coords from database
        """
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_origin(cls, from_latitude, from_longitude):
        """
        Finds a pair of coordinates
        Param: origin coordinates
        Return: MapCoordsModel
        """
        return MapCoordsModel.query.filter_by(from_latitude=from_latitude, from_longitude=from_longitude).first()

    @classmethod
    def find_by_destiny(cls, to_latitude, to_longitude):
        """
        Finds a pair of coordinates
        Param: origin coordinates
        Return: MapCoordsModel
        """
        return MapCoordsModel.query.filter_by(to_latitude=to_latitude, to_longitude=to_longitude).first()

    @classmethod
    def all_map_coords(cls):
        """
        Finds all MapCoordsModels and returns them
        Return: all MapCoordsModels
        """
        return MapCoordsModel.query.all()
