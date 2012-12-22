package com.android.alcoholpriceapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ProductInfo holds important information about a certain product such as the
 * store it's held at, the price of the product at that store, and the distance
 * the store is from the users current GPS location.
 * 
 */
public class PriceInfo implements Parcelable {
	/** Used to do a store search in the database. */
	private int storeID;
	private String storeName;
	private double dist;
	
	private int alcID;
	private String alcName;
	private String alcSize;
	
	private double price;

	/**
	 * Constructor that takes all data and creates an object from them.
	 * Used in Product object to make list of PriceInfos
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
	public PriceInfo(int storeID, String storeName, double price, double dist) {
		this.storeID = storeID;
		this.storeName = storeName;
		this.price = price;
		this.dist = dist;
		
		//not used in this case
		alcID = -1;
		alcName = null;
		alcSize = null;
	}
	
	public PriceInfo(int alcID, String alcName, int alcSize, double price) {
		this.alcID = alcID;
		this.alcName = alcName;
		this.alcSize = convertSize(alcSize);
		this.price = price;
		
		storeID = -1;
		storeName = null;
		dist = -1;
	}

	/**
	 * Constuctor used to create an object from a parcel. The order that
	 * data is put into a parcel is the order that it must be taken out.
	 * 
	 * @param in
	 * 			The parcel to create the object from
	 */
	private PriceInfo(Parcel in) {
		//read data back in same order
		
		storeID = in.readInt();
		dist = in.readDouble();
		price = in.readDouble();
		storeName = in.readString();
		alcID = in.readInt();
		alcName = in.readString();
		alcSize = in.readString();
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
	
	public int getAlcID() {
		return alcID;
	}
	
	public String getAlcName() {
		return alcName;
	}
	
	public String getAlcSize() {
		return alcSize;
	}
	
	/**
	 * This function takes the String format of size and turns it into an integer
	 * 
	 * @param size in String format
	 * @return integer format of the size
	 */
	public int convertSize(String size) {
		if (size.equals("Single"))
			return 1;
		else if (size.equals("40 oz"))
			return 2;
		else if (size.equals("6 pack"))
			return 3;
		else if (size.equals("12 pack"))
			return 4;
		else if (size.equals("18 pack"))
			return 5;
		else if (size.equals("24 pack"))
			return 6;
		else if (size.equals("16 oz (pint)"))
			return 7;
		else if (size.equals("750mL (fifth)"))
			return 8;
		else // doesn't match
			return -1;
	}
	
	/**
	 * This function takes the integer format of size and turns it into a String
	 * @param size in integer format
	 * @return String format of size
	 */
	public String convertSize(int size) {
		switch(size) {
			case 1:
				return "Single";
			case 2:
				return "40 oz";
			case 3:
				return "6 pack";
			case 4:
				return "12 pack";
			case 5:
				return "18 pack";
			case 6:
				return "24 pack";
			case 7:
				return "16 oz (pint)";
			case 8:
				return "750mL (fifth)";
			default:
				return null;
		}
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
	
	public void setAlcID(int alcID) {
		this.alcID = alcID;
	}
	
	public void setAlcName(String alcName) {
		this.alcName = alcName;
	}
	
	public void setAlcSize(String alcSize) {
		this.alcSize = alcSize;
	}
	
	/*
	 * Parcelable methods
	 */
	
	public static final Parcelable.Creator<PriceInfo> CREATOR = new Parcelable.Creator<PriceInfo>() {
		public PriceInfo createFromParcel(Parcel in) {
			//we just need to read each field back from the parcel
			return new PriceInfo(in);
		}
		
		public PriceInfo[] newArray(int size) {
			return new PriceInfo[size];
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
		out.writeInt(alcID);
		out.writeString(alcName);
		out.writeString(alcSize);
	}
}
