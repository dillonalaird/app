package com.android.alcoholpriceapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.widget.Toast;

public class GetSearchData extends AsyncTask<String, Integer, String> {

	//first argument is the name
	//second argument is the size
	@Override
	protected String doInBackground(String... params) {
		BufferedReader in = null;
		String data = null;
		
		HttpGet request = new HttpGet("http://easyuniv.com/API/alc/" + params[0] + "/" + params[1]);
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
