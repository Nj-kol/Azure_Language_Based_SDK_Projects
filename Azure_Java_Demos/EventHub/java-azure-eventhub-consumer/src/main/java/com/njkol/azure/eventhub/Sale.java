package com.njkol.azure.eventhub;

import java.io.Serializable;
import java.util.Objects;

public class Sale implements Serializable {

	private static final long serialVersionUID = 1L;

	private String seller_id;
	private String product;
	private int quantity;
	private double product_price;
	private long sale_ts;
	
	public Sale() {
		
	}
	
	public Sale(String seller_id, String product, int quantity, double product_price, long sale_ts) {
		super();
		this.seller_id = seller_id;
		this.product = product;
		this.quantity = quantity;
		this.product_price = product_price;
		this.sale_ts = sale_ts;
	}
	
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getProduct_price() {
		return product_price;
	}
	public void setProduct_price(float product_price) {
		this.product_price = product_price;
	}
	public long getSale_ts() {
		return sale_ts;
	}
	public void setSale_ts(long sale_ts) {
		this.sale_ts = sale_ts;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		long temp;
		temp = Double.doubleToLongBits(product_price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + quantity;
		result = prime * result + (int) (sale_ts ^ (sale_ts >>> 32));
		result = prime * result + ((seller_id == null) ? 0 : seller_id.hashCode());
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
		Sale other = (Sale) obj;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (Double.doubleToLongBits(product_price) != Double.doubleToLongBits(other.product_price))
			return false;
		if (quantity != other.quantity)
			return false;
		if (sale_ts != other.sale_ts)
			return false;
		if (seller_id == null) {
			if (other.seller_id != null)
				return false;
		} else if (!seller_id.equals(other.seller_id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Sale [seller_id=" + seller_id + ", product=" + product + ", quantity=" + quantity + ", product_price="
				+ product_price + ", sale_ts=" + sale_ts + "]";
	}

}
