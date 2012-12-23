package com.android.alcoholpriceapp.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.listadapters.ProductAdapter;
import com.android.alcoholpriceapp.listadapters.StoreAdapter;
import com.android.alcoholpriceapp.models.Store;
import com.android.alcoholpriceapp.network.APICall;
import com.android.alcoholpriceapp.network.Response;
import com.android.alcoholpriceapp.util.GPSUtility;

public class StorePage extends Activity {
	
	private ListView listView;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.store_page);
        
        final TextView mTextView = (TextView) findViewById(R.id.storeNameText);
        
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        //test
        mTextView.setText(Integer.toString(id));
        
        //start from searchpage
        progressDialog = ProgressDialog.show(StorePage.this, "Please wait...",
    			"Retrieving data...", true, true);
    	
    	//create a response object
		Response res = null;
		final APICall searchTask = new APICall();
		try {
	    	res = searchTask.execute("GET", "STORE", Integer.toString(id)).get();
		} catch (Exception e) {
			// not network error, this is an exception thrown by AsyncTask's execute method
			// TODO: AsyncTask execute exception
		} 
		Log.v("debug", "2");
		
		progressDialog.setOnCancelListener(new OnCancelListener() {
    		@Override
    		public void onCancel(DialogInterface dialog) {
    			if (searchTask != null) searchTask.cancel(true);
    		}
    	});
		Log.v("debug", "3");
    	
    	if (res.getSuccess()) {
    		GPSUtility gps = new GPSUtility(StorePage.this);
    		Location location = gps.promptForGPS();
    		if (location != null) {
	    		Store store = new Store(location, res.getData());
	    		
	            StoreAdapter adapter = new StoreAdapter(this, R.layout.product_row, store.getPriceInfos());
	            
	            listView = (ListView) findViewById(R.id.productsListView);
	            listView.setAdapter(adapter);
	            
	            TextView storeNameText = (TextView) findViewById(R.id.storeNameText);
	            TextView storeAddressText = (TextView) findViewById(R.id.storeAddressText);
	            TextView distText = (TextView) findViewById(R.id.storeDistanceText);
	            
	            storeNameText.setText(store.getName());	
	            storeAddressText.setText(store.getAddress());
	            distText.setText(Double.toString(store.getDist()) + " mi");
    		} else {
    			AlertDialog.Builder builder = new AlertDialog.Builder(StorePage.this);
	    		builder
	    			.setTitle("Sorry!")
	    			.setMessage("No fucking idea.")
	    			.setPositiveButton("Okay", null)
	    			.show();
	    		progressDialog.cancel();
    		}
            
	    	progressDialog.cancel(); //so when you hit back it isnt there
    	} else {
    		AlertDialog.Builder builder = new AlertDialog.Builder(StorePage.this);
    		builder
    			.setTitle("Sorry!")
    			.setMessage("We could not find that store in our database.")
    			.setPositiveButton("Okay", null)
    			.show();
    		progressDialog.cancel();
    	}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}