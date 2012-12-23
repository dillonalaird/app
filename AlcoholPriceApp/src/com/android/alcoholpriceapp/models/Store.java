package com.android.alcoholpriceapp.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.alcoholpriceapp.gps.GPSTracker;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Store implements Parcelable {
	
	private String name;
	private String address;
	/** current location of the phone */
	private Location location;
	/** distance from phone to store */
	private double dist;
	
	private ArrayList<PriceInfo> priceInfos;
	
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
	
	private Store() {
		priceInfos = new ArrayList<PriceInfo>();
	}

	private Store(Parcel in) {
		this();
		
		name = in.readString();
		address = in.readString();
		location = in.readParcelable(null);
		in.readTypedList(priceInfos, PriceInfo.CREATOR);
	}
	
	private void parseData(JSONObject dataObj) throws JSONException {
		this.name = dataObj.getString("name");
		this.address = dataObj.getString("address");
		String[] coordinates = dataObj.getString("gps").split(",");
		dist = GPSTracker.calculateGPSDistance(
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
	
	/**
	 * wont ever use this
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
