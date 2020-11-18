from flask_sqlalchemy import SQLAlchemy
from flask import current_app

db = SQLAlchemy()
secret_key = ""


def init_db(app):
    with app.app_context():
        secret_key = current_app.secret_key