package com.android.alcoholpriceapp.models;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.alcoholpriceapp.util.GPSUtility;

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
		
		this.name = name;
		this.address = address;
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
