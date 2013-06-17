package com.android.alcoholpriceapp.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Type represents a certain type of alcohol and size such as fifth of vodka or
 * pint of whisky. Type holds a list of products of that specified type of
 * alcohol and size. For instance Type of fifth of vodka might hold Smirnoff,
 * Grey Goose, all vodka's that are fifths.
 */
public class Type implements Parcelable {
	/** The type of alcohol. */
	private String type;
	/** The size of the alcohol. */
	private String size;
	/** The List of products of the specified type and size. */
	private List<String> products;
	
	/**
	 * Basic constructor, constructs an empty list of products.
	 */
	public Type() {
		products = new ArrayList<String>();
	}
	
	private Type(Parcel in) {
		this();
		
		in.readStringList(products);
		type = in.readString();
		size = in.readString();
		
	}
	
	/**
	 * Takes a JSONArray of the product info for this type and turns it into
	 * a list of products. Also takes the alcohol type and size.
	 * 
	 * @param productInfo
	 * 		JSONArray containing the product info (checked by Response).
	 * @param type
	 * 		Type of the alcohol.
	 * @param size
	 * 		Size of the alcohol.
	 */
	public Type(JSONObject productInfo, String size, String type) {
		this();
		this.type = type;
		this.size = size;
		
		try {
			parseData(productInfo);
		} catch(JSONException e) {
			// Response now handles the parse error
			// TODO: if data is in incorrect format it can throw an error...
		}
	}
	
	private void parseData(JSONObject dataObj) throws JSONException {
		for (int i = 0; i < dataObj.getJSONArray("alcohols").length(); i++)
			products.add(dataObj.getJSONArray("alcohols").getString(i));
	}

	/*
	 * GETTER METHODS
	 */
	
	public String getAlcoholType() {
		return type;
	}
	
	public String getSize() {
		return size;
	}
	
	public ArrayList<String> getProducts() {
		return (ArrayList<String>) products;
	}
	
	/*
	 * SETTER METHODS
	 */
	
	public void setAlcoholType(String type) {
		this.type = type;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel out, int flags) {
		out.writeStringList(products);
		out.writeString(type);
		out.writeString(size);
	}
	
	public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
		public Type createFromParcel(Parcel in) {
			return new Type(in);
		}
		
		public Type[] newArray(int size) {
			return new Type[size];
		}
	};
}
