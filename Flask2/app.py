from flask import Flask
from flask_migrate import Migrate
from flask_restful import Api



from resources.users import Users, UsersList
from resources.motos import Motos, MotosList
from db import db

def create_app(enviroment):
    app = Flask(__name__)


    with app.app_context():
        db.init_app(app)
        db.create_all()
        migrate = Migrate(app, db, compare_type=True)
        api = Api(app)
        api.add_resource(Users,
                         '/user/<string:name>',
                         '/user')
        api.add_resource(UsersList,
                         '/users')
        api.add_resource(MotosList,
                         '/motos')
        api.add_resource(Motos,
                         '/moto',
                         '/moto/<int:id>',
                         '/moto/<string:license_number>')

    return app


app = create_app(None)

@app.route('/')
def hello_world():
    return 'Hello World!'

if __name__ == '__main__':
    app.run()
