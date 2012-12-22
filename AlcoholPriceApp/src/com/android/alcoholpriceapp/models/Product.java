package com.android.alcoholpriceapp.models;

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
	/** Current location of the phone */
	private Location location;
	/** Product name of this alcohol. */
	private String productName;
	/** Size of this alcohol. */
	private String size;
	/** The list of ProductInfo's for the given product. */
	private ArrayList<PriceInfo> productInfos;
	
	/**
	 * Takes a JSON array of the product info for this product, and will turn it
	 * into the proper array list. Also takes the name and size of the product,
	 * as well as the current location.
	 * 
	 * @param productInfo
	 *				JSON array containing product info (parsed by response)
	 * @param productName
	 * 				name of the alcohol
	 * @param size        
	 * 				size of the alcohol
	 * @param location    
	 * 				current location of the phone
	 */
	public Product(JSONArray productInfo, String productName, String size, Location location) {
		this.location = location;
		productInfos = new ArrayList<PriceInfo>();
		this.productName = productName;
		this.size = size;
		
		try {
			parseData(productInfo);
		} catch (JSONException e) {
			// Response now handles the parse error
			// TODO: if data is in incorrect format it can throw an error...
		}
	}
	
	/**
	 * Default constructor. Used in constructor taking parcel to create the
	 * array list
	 */
	public Product() {
		productInfos = new ArrayList<PriceInfo>();
	}
	
	/**
	 * Constructor taking in a parcel. The order that data is put into the parcel
	 * is the order that data needs to be taken back out
	 * 
	 * @param in - the parcel to create the object from
	 */
	private Product(Parcel in) {
		this();
		
		in.readTypedList(productInfos, PriceInfo.CREATOR);
		productName = in.readString();
		size = in.readString();
		location = in.readParcelable(null);
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
	private void parseData(JSONArray dataObj) throws JSONException {
		for (int i = 0; i < dataObj.length(); i++) {
			JSONObject dataField = dataObj.getJSONObject(i);
			int storeID = Integer.parseInt(dataField.getString("storeID"));
			String storeName = dataField.getString("store_name");
			double price = Double.parseDouble(dataField.getString("price"));
			double dist = calculateGPSDistance(dataField.getString("store_gps"));
			
			productInfos.add(new PriceInfo(storeID, storeName, price, dist));
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
	
	/**
	 * Creates the parcel when called. The order data is put into the parcel
	 * is the order that data must be pulled back out
	 */
	public void writeToParcel(Parcel out, int flags) {
		//we just need to write each field into
		//the parcel
		
		out.writeTypedList(productInfos);
		out.writeString(productName);
		out.writeString(size);
		out.writeParcelable(location, 0);
	}
	
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			//we just need to read each field back from the parcel
			return new Product(in);
		}
		
		public Product[] newArray(int size) {
			return new Product[size];
		}
		
	};
	
	/**
	 *  productInfos getter 
	 */
	public ArrayList<PriceInfo> getProductInfos() {
		return productInfos;
	}
}