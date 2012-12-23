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
import com.android.alcoholpriceapp.gps.GPSTracker;
import com.android.alcoholpriceapp.listadapters.ProductAdapter;
import com.android.alcoholpriceapp.listadapters.StoreAdapter;
import com.android.alcoholpriceapp.models.Store;
import com.android.alcoholpriceapp.network.APICall;
import com.android.alcoholpriceapp.network.Response;

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
    		Location location = getLocation();
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
	
    // TODO: use getLocation from util
    /**
     * Gets the current GPS location. If the user doesn't have GPS enabled pops
     * up an alert dialog allowing the user to access settings and enable GPS.
     * If GPS still isn't enabled, pops up an alert dialog displaying an error.
     * 
     * @return the current GPS location of the user.
     */
    private Location getLocation() {
    	GPSTracker gps = new GPSTracker(this);
    	if (!gps.canGetLocation()) // if gps isn't enabled
    		gps.showSettingsAlert();
    	
    	if (gps.canGetLocation())
    		return gps.getLocation();
    	else {
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder
    			.setTitle("Error")
    			.setMessage("No GPS enabled")
    			.setPositiveButton("Okay", null)
    			.show();
    		return null;
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}