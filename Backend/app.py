from flask import Flask
from flask_cors import CORS
from flask_migrate import Migrate
from flask_restful import Api

from resources.incidents import Incident, IncidentsList
from resources.users import Users, UsersList
from resources.motos import Motos, MotosList
from resources.map_coords import MapCoords, MapCoordsList
from resources.rentals import Rentals, ActiveRentals, RentalsList
from resources.bank_data import BankData, BankDataList
from resources.payments import Payment, PaymentsList

from db import db, secret_key

app = Flask(__name__)
app.config.from_object(__name__)
api = Api(app)

CORS(app, resources={r'/*': {'origins': '*'}})

app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///data.db"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = secret_key

migrate = Migrate(app, db)
db.init_app(app)


@app.route('/')
def hello_world():
    return 'MotoRent DataBase.\n Testing Travis...'


api.add_resource(Users, '/user/<string:user_id>', '/user')
api.add_resource(UsersList, '/users')

api.add_resource(MotosList, '/motos')
api.add_resource(Motos, '/moto', '/moto/<int:id>', '/moto/<string:license_number>')

api.add_resource(MapCoords, '/mapcoord')
api.add_resource(MapCoordsList, '/mapcoords')

api.add_resource(Rentals, '/rental', '/rental/<int:id>')
api.add_resource(ActiveRentals, '/activerental/<int:user_id>')
api.add_resource(RentalsList, '/rentals')

api.add_resource(BankData, '/bankdata', '/bankdata/<string:id_bank_data>')
api.add_resource(BankDataList, '/bankdatas')

api.add_resource(Incident, '/incident', '/incident/<string:incident_id>')
api.add_resource(IncidentsList, '/incidents')

api.add_resource(Payment, '/payment', '/payment/<string:id_payment>')
api.add_resource(PaymentsList, '/payments')

if __name__ == '__main__':
    app.run(port=5000, debug=True)
