package com.android.alcoholpriceapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ProductInfo holds important information about a certain product such as the
 * store it's held at, the price of the product at that store, and the distance
 * the store is from the users current GPS location.
 * 
 */
public class ProductInfo implements Parcelable {
	/** Used to do a store search in the database. */
	private int storeID;
	private String storeName;
	private double price;
	private double dist;

	/**
	 * Constructor that takes all data and creates an object from them
	 * 
	 * @param storeID
	 * 			ID of the store (for use with SQL database)
	 * @param storeName
	 * 			Name of the store that the price corresponds to
	 * @param price
	 * 			Price of the product at this store
	 * @param dist
	 * 			Distance between the phone and this store
	 */
	public ProductInfo(int storeID, String storeName, double price, double dist) {
		this.storeID = storeID;
		this.storeName = storeName;
		this.price = price;
		this.dist = dist;
	}

	/**
	 * Constuctor used to create an object from a parcel. The order that
	 * data is put into a parcel is the order that it must be taken out.
	 * 
	 * @param in
	 * 			The parcel to create the object from
	 */
	private ProductInfo(Parcel in) {
		//read data back in same order
		
		storeID = in.readInt();
		dist = in.readDouble();
		price = in.readDouble();
		storeName = in.readString();
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
	
	/*
	 * Parcelable methods
	 */
	
	public static final Parcelable.Creator<ProductInfo> CREATOR = new Parcelable.Creator<ProductInfo>() {
		public ProductInfo createFromParcel(Parcel in) {
			//we just need to read each field back from the parcel
			return new ProductInfo(in);
		}
		
		public ProductInfo[] newArray(int size) {
			return new ProductInfo[size];
		}
		
	};
	
	@Override
	public int describeContents() {
		// most likely wont be using this
		return 0;
	}

	/**
	 * Method used to create a parcel from the object. The order that data is
	 * put into the parcel is the order that data must be taken out.
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		//write all data to parcel
		
		out.writeInt(storeID);
		out.writeDouble(dist);
		out.writeDouble(price);
		out.writeString(storeName);
	}
}
