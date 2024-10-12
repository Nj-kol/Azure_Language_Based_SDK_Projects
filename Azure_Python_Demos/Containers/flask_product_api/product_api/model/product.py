
from ..extensions import db,ma
from marshmallow import Schema, fields,post_load

# Product Class/Model
class Product(db.Model):
  id = db.Column(db.Integer, primary_key=True)
  name = db.Column(db.String(100), unique=True)
  description = db.Column(db.String(200))
  price = db.Column(db.Float)
  qty = db.Column(db.Integer)

  def __init__(self, name,description,price,qty):
    self.name = name
    self.description = description
    self.price = price
    self.qty = qty


# Product Schema
class ProductSchema(ma.Schema):
    id = fields.Integer()
    name = fields.Str()
    description = fields.Str()
    price = fields.Float()
    qty = fields.Integer()

    @post_load
    def make_user(self, data, **kwargs):
        return Product(**data)