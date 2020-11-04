from decouple import config

class Config:
    pass

class ProductionConfig(Config):
    DEBUG = False
    SQLALCHEMY_DATABASE_URI = config('DATABASE_URL', default='localhost')
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SECRET_KEY = config('SECRET_KEY', default='localhost')


class DevelopmentConfig(Config):
    DEBUG = True
    SQLALCHEMY_DATABASE_URI = 'sqlite:///data.db'
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SECRET_KEY = "yfxncvq5bdcmjxl608o5jt0nnff3nwgfqs9hkm"


config = {
    'development': DevelopmentConfig,
    'production': ProductionConfig
}
