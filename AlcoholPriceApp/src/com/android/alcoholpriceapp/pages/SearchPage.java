package com.android.alcoholpriceapp.pages;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.menu.MenuControl;
import com.android.alcoholpriceapp.models.Product;
import com.android.alcoholpriceapp.network.APICall;
import com.android.alcoholpriceapp.network.Response;
import com.android.alcoholpriceapp.util.GPSUtility;
import com.android.alcoholpriceapp.util.SizeTypeUtility;

/**
 * The search page for the Alcohol Price Application. Allows the user to search
 * alcohol by product name and size.
 */
public class SearchPage extends Activity {

	/** Text area for user to enter product name query. */
	private EditText searchEditText;
	/** The spinner containing all of the alcohol size information. */
	private Spinner sizeSpinner;
	/** Search button initiates the search process. */
	private Button searchButton;
	/** The size of the alcohol the user has selected. */
	private String selectedSize;
	/** A waiting dialog that pops up to notify the user the app is currently
	 * conducting the requested search.
	 */
	private ProgressDialog progressDialog;

	/**
	 * Creates the options menu to display on this activity.
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    /**
     * Runs MenuControl when a certain menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return MenuControl.selectMenuItem(item, this);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		Log.d("SearchPage", "called onCreate");
		
		// This is so if there's a connection error with updateSizes or
		// updateTypes, an AlertDialog box will pop up in this instance
		// of SearchPage
		SizeTypeUtility.INSTANCE.setContext(this);
		
		findAllViewsById();
		initSpinner();
		
		searchButton.setOnClickListener(onSearchClickListener);
	}

	/**
	 * Finds all the views by their id listed in R.java. This is used to grab
	 * different items such as a button or spinner.
	 */
	private void findAllViewsById() {
		searchEditText = (EditText) findViewById(R.id.search_query);
		sizeSpinner = (Spinner) findViewById(R.id.size);
		searchButton = (Button) findViewById(R.id.search_button);
	}

	/**
	 * Sets up the spinners by finding the items listed in them and then adding
	 * those items to the spinner.
	 */
	private void initSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				SizeTypeUtility.INSTANCE.getSizeList());
		adapter.setDropDownViewResource(android.R.layout
				.simple_spinner_dropdown_item);
		sizeSpinner.setAdapter(adapter);
		sizeSpinner.setOnItemSelectedListener(new SizeSpinnerActivity());
	}
	
	/**
	 * onSearchClickListener is a listener applied to the search button. 
	 * Mainly used to clean up the onCreate function
	 */
	private OnClickListener onSearchClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String alcohol = searchEditText.getText().toString();
			if (checkInput(alcohol, selectedSize)) {
				progressDialog = ProgressDialog.show(SearchPage.this, "Please wait...",
		    			"Retrieving data...", true, true);
		    	
		    	//create a response object
				Response res = null;
				final APICall searchTask = new APICall(SearchPage.this);
				try {
			    	res = searchTask.execute("GET", "PRODUCT", 
			    			alcohol.toLowerCase().trim(), 
			    			selectedSize).get();
				} catch (Exception e) {
					// not network error, this is an exception thrown by 
					// AsyncTask's execute method
					// TODO: AsyncTask execute exception
				} 
				
				progressDialog.setOnCancelListener(new OnCancelListener() {
		    		@Override
		    		public void onCancel(DialogInterface dialog) {
		    			if (searchTask != null) searchTask.cancel(true);
		    		}
		    	});
		    	
		    	if (res.getSuccess()) {
					GPSUtility gps = new GPSUtility(SearchPage.this);
					Location location = gps.promptForGPS();
		    		if (location != null) {
			    		Parcelable product = new Product(res.getData(), 
			    				alcohol.toLowerCase(Locale.ENGLISH).trim(), 
			    				selectedSize, location);
			    		
			    		Intent intent = new Intent(SearchPage.this, ProductPage.class);
			    		intent.putExtra("Product", product);
			    		intent.putExtra("from", 1);
			    		progressDialog.cancel(); //so when you hit back it isn't there
			    		startActivity(intent);
		    		} else {
		    			AlertDialog.Builder builder = new AlertDialog.Builder(SearchPage.this);
			    		builder
			    			.setTitle("Sorry!")
			    			.setMessage("No fucking idea.")
			    			.setPositiveButton("Okay", null)
			    			.show();
			    		progressDialog.cancel();
		    		}
		    	} else {
		    		AlertDialog.Builder builder = new AlertDialog.Builder(SearchPage.this);
		    		builder
		    			.setTitle("Sorry!")
		    			.setMessage("We could not find that alcohol in our database.")
		    			.setPositiveButton("Okay", null)
		    			.show();
		    		progressDialog.cancel();
		    	}
			} //no else needed because check input makes the toasts
		}
		
	};
	
	/**
	 * Checks the input to make sure the user has actually put in the product 
	 * name and size data. 
	 * 
	 * @param alcohol
	 * 		The alcohol product name the user is trying to search for.
	 * @param size
	 * 		The size of the alcohol the user is trying to search for.
	 * @return true if the user hasn inputed an alcohol type and size, and
	 * false otherwise
	 */
	private boolean checkInput(String alcohol, String size) {
		
		if (alcohol == null) {
			Toast.makeText(this, "Please type in an alcohol product name", 
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (size.equals("0")) {
			Toast.makeText(this, "Please select an alcohol size", 
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
    
    /**
     * SizeSpinnerActivity allows us to set selectedSize to the current item 
     * selected on the sizeSpinner.
     */
    private class SizeSpinnerActivity extends Activity implements 
    				OnItemSelectedListener {
    	public void onItemSelected(AdapterView<?> parent, View view,
    			int pos, long id) {
    		
    		// Converts the sizes to String representations of ints so the database
    		// can parse them easier
    		String size = parent.getItemAtPosition(pos).toString();
    		selectedSize = SizeTypeUtility.INSTANCE.convertSize(size) + "";
    	}
    	
    	public void onNothingSelected(AdapterView<?> parent) {
    		// do nothing
    	}
    }
}











