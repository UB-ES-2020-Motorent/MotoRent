from flask import Flask
from flask_migrate import Migrate
from flask_restful import Api
from decouple import config as config_decouple
from config import config


from resources.users import Users, UsersList
from db import db

def create_app(enviroment):
    app = Flask(__name__)

    app.config.from_object(enviroment)

    with app.app_context():
        db.init_app(app)
        db.create_all()
        #app.config['SQLALCHEMY_DATABASE_URI'] = "postgresql://postgres:MotoRent@localhost:5432/motorent_api"
        #app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
        migrate = Migrate(app, db, compare_type=True)
        api = Api(app)
        api.add_resource(Users, '/user/<string:name>', '/user')
        api.add_resource(UsersList, '/users')

    return app

enviroment = config['development']
if config_decouple('PRODUCTION', default=False):
    enviroment = config['production']

app = create_app(enviroment)

@app.route('/')
def hello_world():
    return 'Hello World!'

if __name__ == '__main__':
    app.run()
