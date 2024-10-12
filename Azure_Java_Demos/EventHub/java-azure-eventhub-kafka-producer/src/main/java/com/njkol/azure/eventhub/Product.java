package com.njkol.azure.eventhub;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String product;
	private double product_price;
	
	public Product(String product, double d) {
		super();
		this.product = product;
		this.product_price = d;
	}
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public double getProduct_price() {
		return product_price;
	}
	public void setProduct_price(float product_price) {
		this.product_price = product_price;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		long temp;
		temp = Double.doubleToLongBits(product_price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (Double.doubleToLongBits(product_price) != Double.doubleToLongBits(other.product_price))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [product=" + product + ", product_price=" + product_price + "]";
	}
	
	
}
