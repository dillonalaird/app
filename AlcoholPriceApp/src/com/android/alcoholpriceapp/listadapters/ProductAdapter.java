package com.android.alcoholpriceapp.listadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.alcoholpriceapp.ProductInfo;
import com.android.alcoholpriceapp.R;

public class ProductAdapter extends ArrayAdapter<ProductInfo> {

	Context context;
	int layoutResourceId;
	ArrayList<ProductInfo> data = null;
	
	public ProductAdapter(Context context, int layoutResourceId, ArrayList<ProductInfo> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
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
		
		ProductInfo product = data.get(position);
		holder.distanceText.setText(Double.toString(product.getDist()) + " away");
		holder.priceText.setText("$" + Double.toString(product.getPrice()));
		holder.storeText.setText(product.getStoreName());
		
		return row;
	}
	
	//ProductHolder is used to hold the textview objects
	//so that we do not have to find them each time
	static class ProductHolder {
		TextView priceText;
		TextView storeText;
		TextView distanceText;
	}
	
}
