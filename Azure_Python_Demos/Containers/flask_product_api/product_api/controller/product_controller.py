
import logging
from flask import Flask,Blueprint, request, jsonify
from ..model.product import Product,ProductSchema
from ..repository.product_repo import ProductRepo

logger = logging.getLogger(__name__)

api = Blueprint('main', __name__)

# Init schema
product_schema = ProductSchema()
products_schema = ProductSchema(many=True)

# Init repository
repo = ProductRepo()

# Create a Product
@api.route('/product', methods=['POST'])
def add_product():
  """
  post endpoint
  ---
  tags:
    - Add a Product
  parameters:
    - in: body
      name: product
      description: The product to create.
      schema:
        $ref: '#/definitions/Product'   
  responses:
    200:
      description: The product inserted in the database
      schema:
        $ref: '#/definitions/Product'
  """
  name = request.json['name']
  description = request.json['description']
  price = request.json['price']
  qty = request.json['qty']
  new_product = Product(name, description, price, qty)
  repo.add_product(new_product)
  return product_schema.jsonify(new_product)

# Get All Products
@api.route('/product', methods=['GET'])
def get_products():
  """Returns all the stored products
  ---
  tags:
    - Get all products
  responses:
      200:
          description: Retrieves all products
          schema:
              $ref: '#/definitions/Product'
  """
  logger.info('Got request for fetching all products')
  all_products = repo.get_all_products()
  result = products_schema.dump(all_products)
  return jsonify(result)


# Update a Product
@api.route('/product/<id>', methods=['PUT'])
def update_product(id):
  """
  Put endpoint
  ---
  tags:
    - Update product
  parameters:
    - name: id
      in: path
      type: string
      required: true
      description: The ID of the product to be updated
    - in: body
      name: product
      description: The product to create
      schema:
        $ref: '#/definitions/Product'   
  responses:
    200:
      description: The product inserted in the database
      schema:
        $ref: '#/definitions/Product'
  """
  logger.debug(f'Got request for updating product ID {id}')
  name = request.json['name']
  description = request.json['description']
  price = request.json['price']
  qty = request.json['qty']
  updated_product = Product(name, description, price, qty)
  result = repo.update_product(id,updated_product)
  return product_schema.jsonify(result)

# Delete Product
@api.route('/product/<id>', methods=['DELETE'])
def delete_product(id):
  """
  Delete endpoint
  ---
  tags:
    - Delete a product
  parameters:
    - name: id
      in: path
      type: string
      required: true
      description: The ID of the product to be deleted
  responses:
    200:
      description: The product deleted from the database
      schema:
        $ref: '#/definitions/Product'
  """
  repo.delete_product(id)
  return product_schema.jsonify('{"status" : "Success"}')
