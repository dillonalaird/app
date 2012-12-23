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
import com.android.alcoholpriceapp.listadapters.ProductAdapter.ProductHolder;
import com.android.alcoholpriceapp.models.PriceInfo;

public class StoreAdapter extends ArrayAdapter<PriceInfo> {
	
	Context context;
	int layoutResourceId;
	ArrayList<PriceInfo> data = null;
	
	/**
	 * Constructor for the adapter. Takes the context, layout to use for each row,
	 * and an array list of data to put into the rows
	 * 
	 * @param context
	 * 			The context that the list view will be in
	 * @param layoutResourceId
	 * 			The layout to apply to each row
	 * @param data
	 * 			A list of data to fill the rows with
	 */
	public StoreAdapter(Context context, int layoutResourceId, ArrayList<PriceInfo> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	/**
	 * pretty sure this is called when each row is created in the listView
	 * 
	 * The inflator takes the layout assigned to it and makes it fit the
	 * space that it is filling (im pretty sure)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ProductHolder holder = null;
		
		if(row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ProductHolder();
			holder.priceText = (TextView)row.findViewById(R.id.priceText);
			holder.storeText = (TextView)row.findViewById(R.id.storeText);
			holder.distanceText = (TextView)row.findViewById(R.id.distanceText);
			
			row.setTag(holder);
		} else {
			holder = (ProductHolder)row.getTag();
		}
		
		PriceInfo product = data.get(position);
		holder.distanceText.setText(product.getAlcSize());
		holder.priceText.setText("$" + Double.toString(product.getPrice()));
		holder.storeText.setText(product.getAlcName());
		
		return row;
	}
	
	/**
	 * ProductHolder is a class used to hold the design elements for each row
	 * Using this is supposed to increase performance (can't tell how it does)
	 */
	static class ProductHolder {
		TextView priceText;
		TextView storeText;
		TextView distanceText;
	}

}
