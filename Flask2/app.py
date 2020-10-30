from flask import Flask
from flask_migrate import Migrate
from flask_restful import Api

from resources.users import Users, UsersList
from db import db

app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = "postgresql://postgres:MotoRent@localhost:5432/motorent_api"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
migrate = Migrate(app, db)
api = Api(app)
db.init_app(app)

api.add_resource(Users, '/user/<string:name>', '/user')
api.add_resource(UsersList, '/users')

@app.route('/')
def hello_world():
    return 'Hello World!'

if __name__ == '__main__':
    app.run()
