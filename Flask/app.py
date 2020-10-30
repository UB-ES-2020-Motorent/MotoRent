from flask import Flask
from flask_restful import Api
from flask_migrate import Migrate
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy

from resources.accounts import Accounts, AccountsList
from resources.login import Login

app = Flask(__name__)
db = SQLAlchemy(app)
app.config.from_object(__name__)
api = Api(app)

CORS(app, resources={r'/*': {'origins': '*'}})

app.config['SQLALCHEMY_DATABASE_URI'] = "postgresql://postgres:MotoRent@localhost:5432/motorent_api"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
migrate = Migrate(app, db)
db.init_app(app)

api.add_resource(Accounts, '/account/<string:username>', '/account')
api.add_resource(AccountsList, '/accounts')

api.add_resource(Login, '/login')

@app.route('/')
def hello_world():
    return 'Hello World!'

if __name__ == '__main__':
    app.run(port=5000, debug=True, threaded=False)
