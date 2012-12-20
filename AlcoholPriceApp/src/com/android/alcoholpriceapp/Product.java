package com.android.alcoholpriceapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Product takes in a JSON string and grab each item in the "data" field and
 * stores it in a ProducInfo object. It then stores a list of ProductInfo 
 * objects that represents the different prices or store locations the product
 * might have. For example, Coors Light might be $9.99 at Safeway 1.2 miles
 * away and $10.99 at QFC 1.0 miles away.
 */
public class Product {
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
	public Product(String tmp) {
		productInfos = new ArrayList<ProductInfo>();
		Log.v("entered Product with", tmp);
		try {
			JSONObject dataObj = new JSONObject(jsonStr);
			String status = dataObj.getJSONObject("result").getString("status");
			
			// handles error 400, no search results found
			if (status.equals("400"))
				noSearchResultsFound();
			else
				parseData(dataObj);
		} catch (JSONException je) {
			// This should already be checked by checkRequest in Search
			// since it'll throw the same error if there's something wrong
			// with the JSON. The only other reason an exception would
			// be thrown is if one of the fields was named incorrectly
			// and parseData couldn't find it.
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
	 * 			GPS coordinates.
	 * @return the distance between GPSCoord and the users current GPS coordinates.
	 */
	private double calculateGPSDistance(String GPSCoord) {
		return 0.0;
	}
	
	private void noSearchResultsFound() {
		// make it so the product page just has a result stating no results
		// found
	}
}
