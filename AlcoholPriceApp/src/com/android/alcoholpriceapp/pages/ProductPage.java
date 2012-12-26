package com.android.alcoholpriceapp.pages;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.listadapters.ProductAdapter;
import com.android.alcoholpriceapp.models.PriceInfo;
import com.android.alcoholpriceapp.models.Product;
import com.android.alcoholpriceapp.network.APICall;
import com.android.alcoholpriceapp.network.Response;
import com.android.alcoholpriceapp.util.GPSUtility;
import com.android.alcoholpriceapp.util.SizeTypeUtility;

/**
 * Product page is the activity that shows different prices for the particular alcohol/size
 * combination. The intent that creates it has a parcelable Product object passed into it 
 * from which ProductPage created it's layout.
 * 
 * @author Troy Cosentino
 */
public class ProductPage extends Activity {
	Product product = null;
	
	private ListView listView;

	/**
	 * Called when the Activity is created. Creates the Product object from the parcel 
	 * passed through the intent. Then creates the adapter and populates the list view.
	 * Finally, sets a listener for when an item in the listview is clicked.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);
        
        Bundle bundle = getIntent().getExtras();
        
        int from = bundle.getInt("from");
        
        switch(from) {
        	case 1: //from search page
        		product = bundle.getParcelable("Product");
        		break;
        	case 2: //from store page
        		//get name and size from intent
        		String alcohol = bundle.getString("name");
        		String size = Integer.toString(SizeTypeUtility.INSTANCE.convertSize(bundle.getString("size")));
        		Response res = null;
				final APICall searchTask = new APICall();
				try {
			    	res = searchTask.execute("GET", "PRODUCT", 
			    			alcohol.toLowerCase().trim(), 
			    			size).get();
				} catch (Exception e) {
					// not network error, this is an exception thrown by AsyncTask's execute method
					// TODO: AsyncTask execute exception
				} 
				if (res.getSuccess()) {
					GPSUtility gps = new GPSUtility(ProductPage.this);
					Location location = gps.promptForGPS();
		    		if (location != null) {
			    		product = new Product(res.getData(), 
			    				alcohol.toLowerCase(Locale.ENGLISH).trim(), 
			    				size, location);
		    		} else {
		    			AlertDialog.Builder builder = new AlertDialog.Builder(ProductPage.this);
			    		builder
			    			.setTitle("Sorry!")
			    			.setMessage("No fucking idea.")
			    			.setPositiveButton("Okay", null)
			    			.show();
		    		}
		    	} else {
		    		AlertDialog.Builder builder = new AlertDialog.Builder(ProductPage.this);
		    		builder
		    			.setTitle("Sorry!")
		    			.setMessage("We could not find that alcohol in our database.")
		    			.setPositiveButton("Okay", null)
		    			.show();
		    	}
        		break;
        }
        
        if(product.getProductInfos() != null) {
	        ProductAdapter adapter = new ProductAdapter(this, R.layout.product_row, product.getProductInfos());
	        
	        listView = (ListView) findViewById(R.id.itemsListView);
	        listView.setAdapter(adapter);
        }
        
        listView.setOnItemClickListener(listviewItemClickListener);
        
        TextView alcoholNameText = (TextView) findViewById(R.id.alcoholNameText);
        TextView alcoholSizeText = (TextView) findViewById(R.id.alcoholSizeText);
        
        alcoholNameText.setText(product.getProductName());
        alcoholSizeText.setText(PriceInfo.convertSize(Integer.parseInt(product.getSize())));
    }
    
    /**
     * The Listener for when an item is clicked. Currently a dummy function that passes 
     * it onto the store page. 
     */
    private OnItemClickListener listviewItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(ProductPage.this, StorePage.class);
			
			//passing ID to do search on other side
			intent.putExtra("ID", product.getProductInfos()
					.get((int)listView.getItemIdAtPosition(arg2)).getStoreID());
			startActivity(intent);
		} 
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}