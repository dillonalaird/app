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

/**
 * ProductAdapter represents an ArrayAdapter, which is essentially an array of
 * arbitrary objects, in our case, Strings representing the type of the alcohol.
 * Each of these Strings will be displayed in the UI by calling getView. There's
 * a good description of a ProductAdapter here 
 * 
 * http://stackoverflow.com/questions/4300661/please-explain-array-adapters-and-their-purpose-even-better
 */
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
