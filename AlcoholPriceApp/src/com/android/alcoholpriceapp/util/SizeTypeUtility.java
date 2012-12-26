package com.android.alcoholpriceapp.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.alcoholpriceapp.network.APICall;
import com.android.alcoholpriceapp.network.Response;

public enum SizeTypeUtility {
	INSTANCE;
	private List<Pair> sizeConversion = new ArrayList<Pair>();
	private List<Pair> typeConversion = new ArrayList<Pair>();
	
	public void updateSizes() {
		Response res = null;
		APICall sizeTask = new APICall();
		try {
			res = sizeTask.execute("GET", "SIZES").get();
			JSONArray sizes = res.getData().getJSONArray("sizes");
			sizeConversion.add(new Pair("Select Size of Alcohol", "0"));
			parseData(sizeConversion, sizes);
		} catch (Exception e) {
			// this is bad cause this could be a number of things...
		}
	}
	
	public void updateTypes() {
		Response res = null;
		APICall typesTask = new APICall();
		try {
			res = typesTask.execute("GET", "TYPES").get();
			JSONArray types = res.getData().getJSONArray("types");
			typeConversion.add(new Pair("Select Type of Alcohol", "0"));
			parseData(typeConversion, types);
		} catch (Exception e) {
			// same here
		}
	}
	
	private void parseData(List<Pair> list, JSONArray data) throws JSONException {
		for (int i = 0; i < data.length(); i++) {
			JSONObject dataField = data.getJSONObject(i);
			String text = dataField.getString("text");
			String id = dataField.getString("id");
			list.add(new Pair(text, id));
		}
	}
	
	public List<String> getSizeList() {
		List<String> sizeList = new ArrayList<String>();
		for (Pair pair : sizeConversion) {
			sizeList.add(pair.getText());
		}
		return sizeList;
	}
	
	public List<String> getTypeList() {
		List<String> typeList = new ArrayList<String>();
		for (Pair pair : typeConversion)
			typeList.add(pair.getText());
		return typeList;
	}
	
	public int convertSize(String size) {
		for (Pair pair : sizeConversion)
			if (pair.getText().equals(size))
				return Integer.parseInt(pair.getId());
		return 0; // couldn't find size
	}
	
	public int convertType(String type) {
		for (Pair pair : typeConversion)
			if (pair.getText().equals(type))
				return Integer.parseInt(pair.getId());
		return 0; // coun't find type
	}
	
	public String convertSize(int size) {
		for (Pair pair : sizeConversion)
			if (Integer.parseInt(pair.getId()) == size)
				return pair.getText();
		return null;
	}
	
	public String convertType(int type) {
		for (Pair pair : typeConversion)
			if (Integer.parseInt(pair.getId()) == type)
				return pair.getText();
		return null;
	}
	
	public class Pair {
		private String text;
		private String id;
		
		public Pair(String text, String id) {
			this.text = text;
			this.id = id;
		}
		
		public void setText(String text) { this.text = text; }
		public void setId(String id) { this.id = id; }
		public String getText() { return text; }
		public String getId() { return id; }
	}
}
