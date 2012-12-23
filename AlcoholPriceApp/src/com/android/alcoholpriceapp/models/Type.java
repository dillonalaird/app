package com.android.alcoholpriceapp.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class Type {
	private String alcoholType;
	private String size;
	private List<Product> products;
	
	public Type() {
		products = new ArrayList<Product>();
	}
	
	public Type(JSONArray typeInfo, String alcoholType, String size) {
		this();
		this.alcoholType = alcoholType;
		this.size = size;
		
		try {
			parseData(typeInfo);
		} catch(JSONException e) {
			// Response now handles the parse error
			// TODO: if data is in incorrect format it can throw an error...
		}
	}
	
	private void parseData(JSONArray dataObj) throws JSONException {
		
	}

	/*
	 * GETTER METHODS
	 */
	
	public String getAlcoholType() {
		return alcoholType;
	}
	
	public String getSize() {
		return size;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	/*
	 * SETTER METHODS
	 */
	
	public void setAlcoholType(String alcoholType) {
		this.alcoholType = alcoholType;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
}
