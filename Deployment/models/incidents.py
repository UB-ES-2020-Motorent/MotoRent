from db import db


class IncidentsModel(db.Model):
    """
    Object DB SQL Model: Incidents
    """

    __tablename__ = "incidents"
    id = db.Column(db.Integer(), primary_key=True, unique=True, nullable=False, autoincrement=True)
    user_id = db.Column(db.Integer(), nullable=False, unique=False)
    moto_id = db.Column(db.Integer(), nullable=False, unique=False)
    comment = db.Column(db.String(), nullable=False, unique=False)

    def __init__(self, id, user_id, moto_id, comment):
        self.id = id
        self.user_id = user_id
        self. moto_id = moto_id
        self.comment = comment

    def json(self):
        return {
            'id': self.id,
            'user_id': self.user_id,
            'moto_id': self.moto_id,
            'comment': self.comment
        }

    def save_to_db(self):
        """
        Adds incident to DB
        :return:
        """
        db.session.add(self)
        db.session.commit()

    def delete_from_db(self):
        """
        Deletes incident to db
        :return:
        """
        db.session.delete(self)
        db.session.commit()

    @classmethod
    def find_by_id(cls, incident_id):
        """
        Find a incident by id
        :param incident_id: id of the incident required
        :return: IncidentsModel
        """

        return IncidentsModel.query.filter_by(id=incident_id).first()

    @classmethod
    def all_incidents(cls):
        """
        Finds all incidents and returns them
        :return: List of IncidentsModel
        """
        return IncidentsModel.query.all()
    
