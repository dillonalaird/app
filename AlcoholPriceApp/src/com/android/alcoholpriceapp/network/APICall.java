package com.android.alcoholpriceapp.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * APICall represents an Asynchronous Task that can do HTTP requests to 
 * access data from easyuniv.com's database.
 */
public class APICall extends AsyncTask<String, Integer, Response> {
	
	private final String PARENT_URL = "easyuniv.com/API";
	private Context context;
	
	public APICall(Context context) {
		this.context = context;
	}

	/**
	 * called when you call execute()
	 * 
	 * @param params
	 * 		[0]   - the type of request (GET, POST, PUT, DELETE)
	 * 		[1]   - the model to do request on (product)
	 * 		other - explained above call to each function below
	 */
	@Override
	protected Response doInBackground(String... params) {
		String rawResponse = null;
		
		if (params[0] == "GET") {
			if(params[1] == "PRODUCT") {
				/** [2] - name of alcohol [3] - size of alcohol */
				rawResponse = getRequest(params[1].toLowerCase(), params[2], params[3]);
			} else if (params[1] == "STORE") {
				/** [2] - id of store */
				rawResponse = getRequest(params[1].toLowerCase(), params[2]);
			} else if (params[1] == "TYPE") {
				/** [2] - id of size [3] - id of type */
				rawResponse = getRequest(params[1].toLowerCase(), params[2], params[3]);
			} else if (params[1] == "TYPES") {
				/** no additional parameters */
				rawResponse = getRequest(params[1].toLowerCase());
			} else if (params[1] == "SIZES") {
				/** no additional parameters */
				rawResponse = getRequest(params[1].toLowerCase());
			}
		} else if (params[0] == "POST") {
			/*
			 *  not yet implemented
			 * POST requests are for changing data
			 */
		} else if (params[0] == "PUT") {
			/*
			 * not yet implemented
			 * PUT requests are for adding to the database
			 */
		} else if (params[0] == "DELETE") {
			/*
			 * not yet implemented
			 * DELETE requests are for deleting(no surprise) from the database
			 */
		} else {
			//improper request type
		}
		Response res = new Response(rawResponse);
		return res;
	}
	
	private String getRequest(String function, String... params) {
		String extention = "/" + function;
		for(String p : params) {
			extention += "/" + p;
		}
		URI uri = null;
		try{
			uri = new URI("http", PARENT_URL, extention, null);
		} catch (URISyntaxException e) {
			// URI string constructed violates RFC 2396
			Log.e("APICall, making URI", e.getMessage());
		}
		
		if(uri != null) {
			BufferedReader in = null;
			String data = null;
			
			HttpGet request = new HttpGet(uri.toString());
			DefaultHttpClient client = new DefaultHttpClient();
			try {
				HttpResponse response = client.execute(request);
				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String l = "";
				String nl = System.getProperty("line.separator");
				while((l = in.readLine()) != null) {
					sb.append(l + nl);
				}
				in.close();
				data = sb.toString();
			} catch(ClientProtocolException e) {
				// Error in HTTP protocol from DefaultHttpClient.execute(request)
				Log.e("DefaultHttpClient.execute", e.getMessage());
			} catch(IOException e) {
				// Could be from a couple classes
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder
					.setTitle("Network Error")
					.setMessage("Network Connection Interruption")
					.setPositiveButton("Okay", null)
					.show();
			} finally {
				if(in != null) {
					try {
						in.close();
					} catch(Exception e) {
						//do something?
					}
				}
			}
			return data;
		}
		return null;
	}
}
