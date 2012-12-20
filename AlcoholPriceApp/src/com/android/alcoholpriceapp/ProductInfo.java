package com.android.alcoholpriceapp;

/**
 * ProductInfo holds important information about a certain product such as the
 * store it's held at, the price of the product at that store, and the distance
 * the store is from the users current GPS location.
 * 
 */
public class ProductInfo {
	/** Used to do a store search in the database. */
	private int storeID;
	private String storeName;
	private double price;
	private double dist;

	public ProductInfo(int storeID, String storeName, double price, double dist) {
		this.storeID = storeID;
		this.storeName = storeName;
		this.price = price;
		this.dist = dist;
	}
	
	/*
	 * GETTER METHODS
	 */
	
	public int getStoreID() {
		return storeID;
	}
	
	public String getStoreName() {
		return storeName;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double getDist() {
		return dist;
	}
	
	/*
	 * SETTER METHODS
	 */
	
	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
	
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setDist(double dist) {
		this.dist = dist;
	}
}
