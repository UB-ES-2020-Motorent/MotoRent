import unittest
from app import app
from db import db
from models.users import UsersModel

class BaseTestClass(unittest.TestCase):
    def setUp(self):
        self.app = app
        self.client = self.app.test_client()
        # Crea un contexto de aplicación
        with self.app.app_context():
            # Crea las tablas de la base de datos
            db.create_all()
            user0 = UsersModel(mail='joanmartin123@gmail.com',
                               google_token='juy65rfty76Hg65FVytfGGDD63ccxeDFg',
                               role=0)
            user0.country = 'España'
            user0.id_bank_data = 1234567890123456
            user0.name = 'Joan'
            user0.surname = 'Martin'
            user0.national_id_document = '23432123P'
            user0.save_to_db()
    def tearDown(self):
        with self.app.app_context():
            # Elimina todas las tablas de la base de datos
            db.session.remove()
            db.drop_all()