package com.android.alcoholpriceapp.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.alcoholpriceapp.CustomOnItemSelectedListener;
import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.menu.MenuControl;
import com.android.alcoholpriceapp.network.APICall;
import com.android.alcoholpriceapp.network.Response;
import com.android.alcoholpriceapp.util.GPSUtility;

/**
 * The browse page for the Alcohol Price Application. Allows the user to browse
 * alcohols by type and size.
 */
public class BrowsePage extends Activity {

	/** The spinner containing all of the alcohol size information. */
	private Spinner sizeSpinner;
	/** The spinner containing all of the alcohol type information. */
	private Spinner alcoholTypeSpinner;
	/** Browse button that initiates the browse process. */
	private Button browseButton;
	private String selectedSize;
	private String selectedType;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse);

		findAllViewsById();
		initSpinner();
		sizeSpinner.setOnItemSelectedListener(new SizeSpinnerActivity());
		alcoholTypeSpinner.setOnItemSelectedListener(new TypeSpinnerActivity());
		
		browseButton.setOnClickListener(onBrowseClickListener);
	}

	/**
	 * Finds all the views by their id listed in R.java. This is used to grab
	 * different items such as a button or spinner.
	 */
	private void findAllViewsById() {
		sizeSpinner = (Spinner) findViewById(R.id.size);
		alcoholTypeSpinner = (Spinner) findViewById(R.id.alcohol_type);
		browseButton = (Button) findViewById(R.id.browse_button);
	}

	/**
	 * Sets up the spinners by finding the items listed in them and then adding
	 * those items to the spinner.
	 */
	private void initSpinner() {
		// Check http://developer.android.com/guide/topics/ui/controls/spinner.html
		// for details on this method
		ArrayAdapter<CharSequence> aAdapter = ArrayAdapter.createFromResource(this,
				R.array.types_of_alcohol, android.R.layout.simple_spinner_item);
		aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		alcoholTypeSpinner.setAdapter(aAdapter);
		alcoholTypeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

		ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this,
				R.array.size_of_alcohol, android.R.layout.simple_spinner_item);
		aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sizeSpinner.setAdapter(sAdapter);
		sizeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	private OnClickListener onBrowseClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (checkInput(selectedType, selectedSize)) {
				progressDialog = ProgressDialog.show(BrowsePage.this, "Please wait...",
						"Retrieving data...", true, true);
				
				Response res = null;
				final APICall browseTask = new APICall();
				try {
					
				} catch (Exception e) {
					
				}
				
				progressDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						if (browseTask != null) browseTask.cancel(true);
					}
				});
				
				if (res.getSuccess()) {
					
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(BrowsePage.this);
					builder
						.setTitle("Sorry!")
						.setMessage("We could not find that alcohol in our database")
						.setPositiveButton("Okay", null)
						.show();
					progressDialog.cancel();
				}
			}
		}
	};

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
	 * Checks the input to make sure the user has actually put in the product name
	 * and size data.
	 * @param alcohol
	 * 			The alcohol product name the user is trying to search for.
	 * @param size
	 * 			The size of the alcohol the user is trying to search for.
	 * @return
	 */
	private boolean checkInput(String type, String size) {
		if (type == null) {
			Toast.makeText(this, "Please select an alcohol type", Toast.LENGTH_SHORT).show();
			return false;
		} else if (size.equals("Select Size of Alcohol")) {
			Toast.makeText(this, "Please select an alcohol size", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
    
    private class SizeSpinnerActivity extends Activity implements OnItemSelectedListener {
    	public void onItemSelected(AdapterView<?> parent, View view,
    			int pos, long id) {
    		
    		// Converts the sizes to String representations of ints so the database
    		// can parse them easier
    		String size = parent.getItemAtPosition(pos).toString();
    		if (size.equals("Single"))
    			size = "1";
    		else if (size.equals("40 oz"))
    			size = "2";
    		else if (size.equals("6 pack"))
    			size = "3";
    		else if (size.equals("12 pack"))
    			size = "4";
    		else if (size.equals("18 pack"))
    			size = "5";
    		else if (size.equals("24 pack"))
    			size = "6";
    		else if (size.equals("16 oz (pint)"))
    			size = "7";
    		else if (size.equals("750mL (fifth)"))
    			size = "8";
    		else // if (size.equals("1.5L (half gallon)"))
    			size = "9";
    		selectedSize = size;
    	}
    	
    	public void onNothingSelected(AdapterView<?> parent) {
    		
    	}
    }
    
    private class TypeSpinnerActivity extends Activity implements OnItemSelectedListener {
    	public void onItemSelected(AdapterView<?> parent, View view,
    			int pos, long id) {
    		
    	}
    	
    	public void onNothingSelected(AdapterView<?> parent) {
    		
    	}
    }
}