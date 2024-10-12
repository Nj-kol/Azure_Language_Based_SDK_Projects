import os

basedir = os.path.abspath(os.path.dirname(__file__))

class Config:
    SECRET_KEY = os.getenv('SECRET_KEY', 'my_precious_secret_key')
    DEBUG = False

class DevelopmentConfig(Config):
    DEBUG = True
    # SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://root:asd@nj-kol:33006/sample'
    # SQLALCHEMY_DATABASE_URI = 'mssql+pyodbc://sa:%40mssql2017@nj-kol:31433/sample?driver=ODBC+Driver+17+for+SQL+Server'
    SQLALCHEMY_DATABASE_URI = 'mssql+pyodbc://njkol:%40Somnath88@njkolazuresql.database.windows.net:1433/njkoldemos?driver=ODBC+Driver+17+for+SQL+Server'
    SQLALCHEMY_TRACK_MODIFICATIONS = False

class TestingConfig(Config):
    DEBUG = True
    TESTING = True
    SQLALCHEMY_DATABASE_URI = 'sqlite:////Users/nilanjan1.sarkar/Documents/Softwares/Sqlite/data/sample_app.db'
    PRESERVE_CONTEXT_ON_EXCEPTION = False
    SQLALCHEMY_TRACK_MODIFICATIONS = False

class ProductionConfig(Config):
    DEBUG = False
    SQLALCHEMY_DATABASE_URI = 'mssql+pyodbc://njkol:%40Somnath88@njkolsingledb.database.windows.net:1433/njkolsqlpool?driver=ODBC+Driver+17+for+SQL+Server'
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    
config_by_name = dict(
    dev=DevelopmentConfig,
    test=TestingConfig,
    prod=ProductionConfig
)

key = Config.SECRET_KEY
