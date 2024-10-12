
from ..extensions import db,ma
from ..model.product import Product

class ProductRepo:

    def add_product(self,new_product: Product):
        db.session.add(new_product)
        db.session.commit()

    def get_all_products(self):
      all_products = Product.query.all()
      return all_products

    def update_product(self,id,updated_product: Product):
      product = Product.query.get(id)
      product.name = updated_product.name
      product.description = updated_product.description
      product.price = updated_product.price
      product.qty = updated_product.qty
      db.session.commit()
      return product

    def delete_product(self,id):
      product = Product.query.get(id)
      db.session.delete(product)
      db.session.commit()