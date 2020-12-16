import unittest
from app import app
from db import db
from models.users import UsersModel
from models.motos import MotosModel
from models.rentals import RentalsModel
from datetime import datetime

class BaseTestClass(unittest.TestCase):
    def setUp(self):
        self.app = app
        self.client = self.app.test_client()
        # Crea un contexto de aplicación
        with self.app.app_context():
            # Crea las tablas de la base de datos
            db.create_all()
            self.init_users()
            self.init_motos()
            self.init_rentals()

    def tearDown(self):
        with self.app.app_context():
            # Elimina todas las tablas de la base de datos
            db.session.remove()
            db.drop_all()

    def init_users(self):
        user1 = UsersModel(mail='joanmartin123@gmail.com',
                           google_token='juy65rfty76Hg65FVytfGGDD63ccxeDFg',
                           role=0)
        user1.country = 'España'
        user1.id_bank_data = 1234567890123456
        user1.name = 'Joan'
        user1.surname = 'Martin'
        user1.national_id_document = '23432123P'
        user1.save_to_db()
        user2 = UsersModel(mail='joanmartin456@gmail.com',
                           google_token='jug65FVytfGGDD63ccxeDFg',
                           role=0)
        user2.country = 'España'
        user2.id_bank_data = 6543217890123456
        user2.name = 'Juan'
        user2.surname = 'Martínez'
        user2.national_id_document = '23845467M'
        user2.save_to_db()

    def init_motos(self):
        moto1 = MotosModel('1234AAA', 99, 42.1, 2.1)
        moto2 = MotosModel('4321AAA', 10, 42.0, 2.1)
        moto3 = MotosModel('5678AAA', 9, 42.1, 2.0)
        moto4 = MotosModel('8765AAA', 32, 42.0, 2.0)
        moto1.save_to_db()
        moto2.save_to_db()
        moto3.save_to_db()
        moto4.save_to_db()

    def init_rentals(self):
        rental1 = RentalsModel(1, 1, datetime.now().isoformat())
        rental1.save_to_db()
