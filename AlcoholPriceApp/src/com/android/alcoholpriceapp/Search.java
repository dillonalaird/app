package com.android.alcoholpriceapp;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
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

import com.android.alcoholpriceapp.menu.MenuControl;

/**
 * The search page for the Alcohol Price Application. Allows the user to search
 * alcohol by product name and size.
 */
public class Search extends Activity {

	/** Text area for user to enter product name query. */
	private EditText searchEditText;
	/** The spinner containing all of the alcohol size information. */
	private Spinner sizeSpinner;
	/** Search button initiates the search process. */
	private Button searchButton;
	/** A waiting dialog that pops up to notify the user the app is currently
	 * conducting the requested search.
	 */
	private ProgressDialog progressDialog;
	/** The size of the alcohol the user has selected. */
	private String selectedSize;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		findAllViewsById();
		initSpinner();
		// This is so we can grab the item currently selected in the spinner
		sizeSpinner.setOnItemSelectedListener(new SpinnerActivity());
		
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alcohol = searchEditText.getText().toString();
				if (checkInput(alcohol, selectedSize))
					performSearch(alcohol.toLowerCase().trim(), selectedSize);
			}
		});
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
		// Check http://developer.android.com/guide/topics/ui/controls/spinner.html
		// for details on this method
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.size_of_alcohol, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sizeSpinner.setAdapter(adapter);
		sizeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	
	/**
	 * Checks the input to make sure the user has actually put in the product name
	 * and size data.
	 * @param alcohol
	 * 			The alcohol product name the user is trying to search for.
	 * @param size
	 * 			The size of the alcohol the user is trying to search for.
	 * @return
	 */
	private boolean checkInput(String alcohol, String size) {
		if (alcohol == null) {
			Toast.makeText(this, "Please type in an alcohol product name",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (size.equals("Select Size of Alcohol")) {
			Toast.makeText(this, "Please select an alcohol size",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

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
    
    /**
     * Performs the search when the search button is clicked. Pops up a progress
     * dialog that can be canceled using the back button.
     * 
     * @param alcohol
     * 			The alcohol product name the user is trying to search for.
     * @param size
     * 			The size of the alcohol the user is trying to search for.
     */
    private void performSearch(String alcohol, String size) {
    	progressDialog = ProgressDialog.show(Search.this, "Please wait...",
    			"Retrieving data...", true, true);

    	String data = null;
    	final GetSearchData searchTask = new GetSearchData();
    	try {
			data = searchTask.execute(alcohol, size).get();
		} catch (Exception e) {
			// not network error, this is an exception thrown by AsyncTask's execute method
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder
//				.setTitle("Network Error")
//				.setMessage("A problem with the network connection has occured")
//				.setPositiveButton("Okay", null)
//				.show();
		} 

    	progressDialog.setOnCancelListener(new OnCancelListener() {
    		@Override
    		public void onCancel(DialogInterface dialog) {
    			if (searchTask != null) searchTask.cancel(true);
    		}
    	});

    	Log.v("data", data);
    	Product product = new Product(data);

    	Intent intent = new Intent(this, ProductPage.class);
    	intent.putExtra("data", data);
    	startActivity(intent);
    }
    
    /**
     * SpinnerActivity allows us to set selectedSize to the current item selected
     * on the sizeSpinner.
     */
    public class SpinnerActivity extends Activity implements OnItemSelectedListener {
    	public void onItemSelected(AdapterView<?> parent, View view,
    			int pos, long id) {
    		
    		// Converts the sizes to String representations of ints so the database
    		// can parse them easier
    		String size = parent.getItemAtPosition(pos).toString();
    		if (size.equals("Single"))
    			size = "0";
    		else if (size.equals("40 oz"))
    			size = "1";
    		else if (size.equals("6 pack"))
    			size = "2";
    		else if (size.equals("12 pack"))
    			size = "3";
    		else if (size.equals("18 pack"))
    			size = "4";
    		else if (size.equals("24 pack"))
    			size = "5";
    		else if (size.equals("16 oz (pint)"))
    			size = "6";
    		else if (size.equals("750mL (fifth)"))
    			size = "7";
    		else // if (size.equals("1.5L (half gallon)"))
    			size = "8";
    		selectedSize = size;
    	}
    	
    	public void onNothingSelected(AdapterView<?> parent) {
    		// do nothing
    	}
    }
}











