package com.android.alcoholpriceapp.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Response serves as an intermediary object between getting the initial JSON 
 * response from the server and parsing the data. It checks to see whether a
 * result was returned from the search query by checking the status of the
 * response.
 */
public class Response {
	/** The data contained in the JSON response. */
	private JSONObject data;
	/** The status of the response, basically whether or not it contained any
	 * data.
	 */
	private boolean success;
	
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
			data = dataObj.getJSONObject("data");
			
			if (status.equals("200"))
				this.success = true;
			else
				this.success = false;
		} catch (JSONException e) {
			// This will throw an exception if there's something wrong with the 
			// format of the JSON String. I changed status to false for now so
			// that the string isn't sent into Product which would probably
			// throw the same exception again.
			this.success = false;
			
			// TODO: somehow log what the string was so that we can see what it was
		}
	}
	
	/**
	 * @return whether or not the response was a 200(success).
	 */
	public boolean getSuccess() {
		return success;
	}
	
	/**
	 * Returns the data JSON Array of the response. Now the JSON string parsing does
	 * not need to happen anywhere else
	 * 
	 * @return the data of the response
	 */
	public JSONObject getData() {
		return data;
	}
}
