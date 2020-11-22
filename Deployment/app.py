from flask import Flask, render_template
from flask_cors import CORS
from flask_migrate import Migrate
from flask_restful import Api

from decouple import config as config_decouple
from config import config

from resources.users import Users, UsersList
from resources.motos import Motos, MotosList
from resources.map_coords import MapCoords, MapCoordsList
from resources.rentals import Rentals, RentalsList
from resources.bank_data import BankData, BankDataList

from db import db, init_db

app = Flask(__name__)
environment = config['development']
if config_decouple('PRODUCTION', cast=bool, default=False):
    environment = config['production']
app.config.from_object(environment)

init_db(app)

api = Api(app)

CORS(app, resources={r'/*': {'origins': '*'}})

migrate = Migrate(app, db)
db.init_app(app)

@app.route('/')
def hello_world():
    return 'MotoRent Database - Deployed'


api.add_resource(Users, '/user/<string:user_id>', '/user')
api.add_resource(UsersList, '/users')

api.add_resource(MotosList, '/motos')
api.add_resource(Motos, '/moto', '/moto/<int:id>', '/moto/<string:license_number>')

api.add_resource(MapCoords, '/mapcoord')
api.add_resource(MapCoordsList, '/mapcoords')

api.add_resource(Rentals, '/rental', '/rental/<int:id>')
api.add_resource(RentalsList, '/rentals')

api.add_resource(BankData, '/bankdata', '/bankdata/<string:id_bank_data>')
api.add_resource(BankDataList, '/bankdatas')

if __name__ == '__main__':
    app.run(port=5000, debug=True)
