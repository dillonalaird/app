package com.android.alcoholpriceapp.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;


public class APICall extends AsyncTask<String, Integer, Response> {

	/**
	 * called when you call execute()
	 * 
	 * @param params
	 * 			[0] - the type of request (GET, POST, PUT, DELETE)
	 * 			[1] - the model to do request on (product)
	 * 		  other - explained above call to each function below
	 */
	@Override
	protected Response doInBackground(String... params) {
		String rawResponse = null;
		
		if (params[0] == "GET") {
			if(params[1] == "PRODUCT") {
				/** [1] - name of alcohol [2] - size of alcohol */
				rawResponse = getProduct(params[2], params[3]);
			}
		} else if (params[0] == "POST") {
			/* not yet implemented
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
	
	private String getProduct(	String name, String size) {
		BufferedReader in = null;
		String data = null;
		
		HttpGet request = new HttpGet("http://easyuniv.com/API/product/" + name + "/" + size);
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("line.separator");
			while((l = in.readLine()) != null) {
				sb.append(l + nl);
			}
			in.close();
			data = sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
			// TODO: add some sort of network error dialog box
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
}
