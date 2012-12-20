package com.android.alcoholpriceapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Product represents an alcohol product. It takes in a JSON string and grab 
 * each item in the "data" field and stores it in a ProducInfo object. It then 
 * stores a list of ProductInfo objects that represents the different prices 
 * or store locations the product might have. For example, Coors Light might be
 * $9.99 at Safeway 1.2 miles away and $10.99 at QFC 1.0 miles away.
 */
public class Product implements Parcelable {
	private int mData;
	private Location location;
	/** Product name of this alcohol. */
	private String productName;
	/** Size of this alcohol. */
	private String size;
	/** The list of ProductInfo's for the given product. */
	private ArrayList<ProductInfo> productInfos;
	// Test string
	String jsonStr = "{\"result\":{\"status\":200,\"message\":\"OK\"},\"data\":[{\"storeID\":2,\"store_name\":\"safeway\",\"store_gps\":\"still dont know\",\"price\":3.99},{\"storeID\":1,\"store_name\":\"Hammy\",\"store_gps\":\"don't know\",\"price\":4.99}]}";

	/**
	 * Takes a JSON string and parses its data field turning it into an array
	 * of ProducInfo's. Note any exception thrown here is caught and not 
	 * thrown again. It's assumed that the JSON string is correctly formated.
	 * 
	 * @param jsonStr
	 * 			The JSON string being parsed for it's data field.
	 */
	public Product(String jsonStr, String productName, String size, Location location) {
		this.location = location;
		productInfos = new ArrayList<ProductInfo>();
		this.productName = productName;
		this.size = size;
		
		try {
			parseData(new JSONObject(jsonStr));
		} catch (JSONException e) {
			// This should already be checked by Response. The only other reason
			// an exception would be thrown is if one of the fields was named 
			// incorrectly and parseData couldn't find it.
		}
	}
	
	/**
	 * Parses the data field of a JSONObject. If this field is not found or any
	 * of the fields assumed to be contained in this field, {storeID, store_name,
	 * price, store_gps} a JSONException is thrown.
	 * 
	 * @param dataObj
	 * 			The JSONObject containing a data field.
	 * @throws JSONException
	 * 			If it can't find the data field, or any of data's fields.
	 */
	private void parseData(JSONObject dataObj) throws JSONException {
		JSONArray data = dataObj.getJSONArray("data");
		for (int i = 0; i < data.length(); i++) {
			JSONObject dataField = data.getJSONObject(i);
			int storeID = Integer.parseInt(dataField.getString("storeID"));
			String storeName = dataField.getString("store_name");
			double price = Double.parseDouble(dataField.getString("price"));
			double dist = calculateGPSDistance(dataField.getString("store_gps"));
			
			productInfos.add(new ProductInfo(storeID, storeName, price, dist));
		}
	}
	
	/**
	 * Calculates the distance between GPSCoord and the users current GPS
	 * coordinates.
	 * 
	 * @param GPSCoord
	 * 			GPS coordinates. Assumes that GPSCoord follows the following
	 * format "latitude, longitude"
	 * @return the distance between GPSCoord and the users current GPS coordinates.
	 */
	private double calculateGPSDistance(String GPSCoord) {
		double lat1 = location.getLatitude();
		double long1 = location.getLongitude();
		String[] coordinates = GPSCoord.split(",");
		double lat2 = Double.parseDouble(coordinates[0]);
		double long2 = Double.parseDouble(coordinates[1]);
		
		// Check http://www.smokycogs.com/blog/finding-the-distance-between-two-gps-coordinates/
		// for details.
		lat1 = degToRad(lat1);
		long1 = degToRad(long1);
		lat2 = degToRad(lat2);
		long2 = degToRad(long2);
		
		double earthRadius = 6371; // km
		double deltaLat = lat2 - lat1;
		double deltaLong = long2 - long1;
		double a = Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0) + 
				Math.cos(lat1) * Math.cos(lat2) *
				Math.sin(deltaLong / 2.0) * Math.sin(deltaLong / 2.0);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;
		return distance;
	}
	
	private double radToDeg(double radians) {
		return radians * (180 / Math.PI);
	}
	
	private double degToRad(double degrees) {
		return degrees * (Math.PI / 180);
	}
	
	/*
	 * GETTER METHODS
	 */
	
	public String getProductName() {
		return productName;
	}
	
	public String getSize() {
		return size;
	}
	
	/*
	 * Check http://developer.android.com/reference/android/os/Parcelable.html
	 * for details. These are all methods for Parcelable.
	 */
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(mData);
	}
	
	public static final Parcelable.Creator<Product> CREATOR 
			= new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}
		
		public Product[] newArray(int size) {
			return new Product[size];
		}
		
	};
	
	private Product(Parcel in) {
		mData = in.readInt();
	}
	
	/**
	 *  productInfos getter 
	 */
	public ArrayList<ProductInfo> getProductInfos() {
		return productInfos;
	}
}










