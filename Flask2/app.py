from flask import Flask
from flask_migrate import Migrate
from flask_restful import Api

from resources.users import Users, UsersList
from resources.moto_coords import MotoCoords, MotoCoordsList

from db import db


def create_app():
    app = Flask(__name__)

    app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://postgres:MotoRent@localhost:5432/motorent_api'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

    with app.app_context():
        db.init_app(app)
        db.create_all()
        migrate = Migrate(app, db)
        api = Api(app)

        api.add_resource(Users, '/user/<string:name>', '/user')
        api.add_resource(UsersList, '/users')

        api.add_resource(MotoCoords, '/motocoord/<string:moto_id>', '/motocoord')
        api.add_resource(MotoCoordsList, '/motocoords')

    return app


app = create_app()


@app.route('/')
def hello_world():
    return 'MotoRent Database'


if __name__ == '__main__':
    app.run()
