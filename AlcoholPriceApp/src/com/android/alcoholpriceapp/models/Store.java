package com.android.alcoholpriceapp.models;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.alcoholpriceapp.util.GPSUtility;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Store represents a store carrying alcohol products. Store holds a list of
 * PriceInfo objects that hold the name, size and price of each alcohol the
 * store carries. For example, Safeway could hold 12 packs of Coors Light for
 * $10.99 and a fifth of Smirnoff for $19.99.
 */
public class Store implements Parcelable {
	
	/** Name of the store */
	private String name;
	/** Address of the store */
	private String address;
	/** Current location of the phone */
	private Location location;
	/** Distance from phone to store */
	private double dist;
	/** List of PriceInfo's for different alcohol's at this store */
	private ArrayList<PriceInfo> priceInfos;
	
	/**
	 * Default constructor. Used in constructor taking parcel to create the
	 * array list.
	 */
	private Store() {
		priceInfos = new ArrayList<PriceInfo>();
	}

	/**
	 * Constructor taking in a parcel. The order that data is put into the parcel
	 * is the order that data needs to be taken back out
	 * 
	 * @param in
	 * 		The parcel to create the object from
	 */
	private Store(Parcel in) {
		this();
		
		name = in.readString();
		address = in.readString();
		location = in.readParcelable(null);
		in.readTypedList(priceInfos, PriceInfo.CREATOR);
	}
	
	/**
	 * Takes a JSONObject of the store info for this store, and will turn it
	 * into the proper array list. Also takes the current location.
	 * 
	 * @param location
	 * 		Current location of the phone
	 * @param storeInfo
	 * 		JSONObject containing the store info
	 */
	public Store(Location location, JSONObject storeInfo) {
		this();
		
		this.location = location;
		
		try {
			parseData(storeInfo);
		} catch (JSONException e) {
			// Response now handles the parse error
			// TODO: if data is in incorrect format it can throw an error...
		}
	}
	
	/**
	 * Parses the data field of a JSONObject. If this field is not found or any
	 * of the fields assumed to be contained in this field a JSONException is
	 * thrown
	 * 
	 * @param dataObj
	 * @throws JSONException
	 */
	private void parseData(JSONObject dataObj) throws JSONException {
		this.name = dataObj.getString("name");
		this.address = dataObj.getString("address");
		String[] coordinates = dataObj.getString("gps").split(",");
		dist = GPSUtility.calculateGPSDistance(
				Double.parseDouble(coordinates[0]), 
				Double.parseDouble(coordinates[1]), 
				location.getLatitude(), 
				location.getLongitude());
		
		for (int i = 0; i < dataObj.getJSONArray("prices").length(); i++) {
			JSONObject dataField = dataObj.getJSONArray("prices").getJSONObject(i);
			int alcID = Integer.parseInt(dataField.getString("alcID"));
			String alcoholName = dataField.getString("alcoholName");
			int alcSize = dataField.getInt("alcoholSize");
			double price = Double.parseDouble(dataField.getString("price"));
			
			priceInfos.add(new PriceInfo(alcID, alcoholName, alcSize, price));
		}
	}
	
	/*
	 * GETTER METHODS
	 */
	
	public ArrayList<PriceInfo> getPriceInfos() {
		return priceInfos;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public double getDist() {
		return dist;
	}
	
	/*
	 * Check http://developer.android.com/reference/android/os/Parcelable.html
	 * for details. These are all methods for Parcelable.
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Creates the parcel when called. The order data is put into the parcel
	 * is the order that data must be pulled back out
	 */
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(name);
		out.writeString(address);
		out.writeParcelable(location, 0);
		out.writeTypedList(priceInfos);
	}
	
	public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
		public Store createFromParcel(Parcel in) {
			//we just need to read each field back from the parcel
			return new Store(in);
		}
		
		public Store[] newArray(int size) {
			return new Store[size];
		}
		
	};
}
