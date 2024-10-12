import logging

from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow

from apispec.ext.marshmallow import MarshmallowPlugin
from apispec_webframeworks.flask import FlaskPlugin

from flasgger import APISpec, Schema, Swagger, fields

from .config import config_by_name
from .extensions import db,ma
from .controller import product_controller
from .model.product import ProductSchema

def create_app(test_config=None):
    configure_root_logger()
    app = Flask(__name__,instance_relative_config=True)
    app.config.from_object(config_by_name["dev"])
    db.init_app(app)
    ma.init_app(app)
    register_blueprints(app)
    spec = api_spec()
    template = spec.to_flasgger(
        app,
        definitions=[ProductSchema],
        #paths=[product_controller.get_products,product_controller.add_product,product_controller.update_product,product_controller.delete_product]
    )
    # start Flasgger using a template from apispec
    swagger = Swagger(app, template=template)
    return app

def configure_root_logger():
    # register root logging
    logging.basicConfig(
     filename='product_app.log',
     level=logging.INFO, 
     format= '[%(asctime)s] {%(pathname)s:%(lineno)d} %(levelname)s - %(message)s',
     datefmt='%H:%M:%S')
    logging.getLogger('werkzeug').setLevel(logging.INFO)

def register_blueprints(app):
    app.register_blueprint(product_controller.api)
    return None

def api_spec():
    # Create spec
    spec = APISpec(
        title='My Product store',
        version='1.0.10',
        openapi_version='2.0',
        plugins=[
            FlaskPlugin(),
            MarshmallowPlugin(),
        ],
    )
    return spec