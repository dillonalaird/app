package com.android.alcoholpriceapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Response serves as an intermediary object between getting the initial JSON 
 * response from the server and parsing the data. It checks to see whether a
 * result was returned from the search query by checking the status of the
 * response.
 */
public class Response {
	/** The data contained in the JSON response. */
	private String data;
	/** The status of the response, basically whether or not it contained any
	 * data.
	 */
	private boolean status;
	
	/**
	 * Constructs a Response object that checks the status of the JSON String
	 * response and extracts the data field.
	 * 
	 * @param jsonStr
	 * 			The JSON String containing the status and the data field.
	 */
	public Response(String jsonStr) {
		try {
			JSONObject dataObj = new JSONObject(jsonStr);
			String status = dataObj.getJSONObject("result").getString("status");
			data   = dataObj.getString("data");
			
			if (status.equals("400"))
				this.status = false;
			else
				this.status = true;
		} catch (JSONException e) {
			// This will throw an exception if there's something wrong with the 
			// format of the JSON String. I changed status to false for now so
			// that the string isn't sent into Product which would probably
			// throw the same exception again.
			this.status = false;
		}
	}
	
	/**
	 * Returns the status of the JSON String response indicating whether or not
	 * the String contains any data.
	 * 
	 * @return the status of the JSON String response.
	 */
	public boolean getStatus() {
		return status;
	}
	
	/**
	 * Returns the data contained in the JSON String response. Note you should
	 * check getStatus before you call this method to make sure there's actual
	 * data. 
	 * 
	 * @return the data contained in the JSON String response.
	 */
	public String getData() {
		return data;
	}
}
