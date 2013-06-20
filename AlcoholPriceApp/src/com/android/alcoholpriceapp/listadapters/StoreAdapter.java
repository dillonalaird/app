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

/**
 * ProductAdapter represents an ArrayAdapter, which is essentially an array of
 * arbitrary objects, in our case, PriceInfo objects. Each of these PriceInfo
 * objects represents a row of important information about the product and can
 * be displayed in the UI by calling getView. There's a good description of a
 * ProductAdapter here 
 * 
 * http://stackoverflow.com/questions/4300661/please-explain-array-adapters-and-their-purpose-even-better
 */
public class StoreAdapter extends ArrayAdapter<PriceInfo> {
	
	/** The current context we're working with */
	private Context context;
	/** An ID by which we can locate the XML layout resource */
	private int layoutResourceId;
	/** The list of PriceInfo objects this ProductAdapter is holding */
	private ArrayList<PriceInfo> data = null;
	
	/**
	 * Constructor for the adapter. Takes the context, layout to use for each 
	 * row, and an array list of data to put into the rows
	 * 
	 * @param context
	 * 		The context that the list view will be in
	 * @param layoutResourceId
	 * 		The layout to apply to each row
	 * @param data
	 * 		A list of data to fill the rows with
	 */
	public StoreAdapter(Context context, int layoutResourceId, 
			ArrayList<PriceInfo> data) {
		// We should test this later but it seems like we're storing
		// the fields twice, once in ArrayAdapter, and once in ProductAdapter
		// e.g. there's a method called getContext we can use instead of 
		// storing context as a private field
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	/**
	 * This is called when each row is created in the listView The inflater 
	 * takes the layout assigned to it and makes it fit the space that it is
	 * filling.
	 * 
	 * @param position
	 * 		The position of the item you want displayed (position in the Array
	 * Adapter)
	 * @param convertView
	 * 		This is here for performance reasons (check comment below on 
	 * ListView)
	 * @param parent
	 * 		The parent of the View that is returned
	 * @return a View object to display information from the PriceInfo in the UI
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Android's ListView uses an Adapter to fill itself with Views. When 
		// the ListView is shown, it starts calling getView() to populate 
		// itself. When the user scrolls a new view should be created, so for 
		// performance the ListView sends the Adapter an old view that it's not
		// used any more in the convertView param. Here we're inflating a view
		// from an XML layout file
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
