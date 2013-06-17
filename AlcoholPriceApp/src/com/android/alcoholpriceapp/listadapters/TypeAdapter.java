package com.android.alcoholpriceapp.listadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.alcoholpriceapp.R;

public class TypeAdapter extends ArrayAdapter<String> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<String> data;
	
	/**
	 * Constructs a new TypeAdapter object. Takes the context, layout to use 
	 * for each row, and an array list of data to put into the rows.
	 * 
	 * @param context
	 * 		The context that the list view will be in.
	 * @param layoutResourceId
	 * 		The layout to apply to each row.
	 * @param data
	 * 		A list of data to fill in the rows with.
	 */
	public TypeAdapter(Context context, int layoutResourceId, 
			ArrayList<String> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TypeHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new TypeHolder();
			holder.productName = (TextView) row.findViewById(R.id.productText);
			
			row.setTag(holder);
		} else {
			holder = (TypeHolder) row.getTag();
		}
		
		String product = data.get(position);
		holder.productName.setText(product);
		
		return row;
	}
	
	/**
	 * TypeHolder is a class used to hold the design elements for each row. Suppose
	 * to increase performance.
	 */
	private static class TypeHolder {
		TextView productName;
	}
}
