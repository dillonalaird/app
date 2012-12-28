package com.android.alcoholpriceapp.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.menu.MenuControl;
import com.android.alcoholpriceapp.models.Type;
import com.android.alcoholpriceapp.network.APICall;
import com.android.alcoholpriceapp.network.Response;
import com.android.alcoholpriceapp.util.SizeTypeUtility;

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
		ArrayAdapter<String> tAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				SizeTypeUtility.INSTANCE.getTypeList());
		tAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		alcoholTypeSpinner.setAdapter(tAdapter);
		alcoholTypeSpinner.setOnItemSelectedListener(new TypeSpinnerActivity());

		ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				SizeTypeUtility.INSTANCE.getSizeList());
		sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sizeSpinner.setAdapter(sAdapter);
		sizeSpinner.setOnItemSelectedListener(new SizeSpinnerActivity());
	}

	private OnClickListener onBrowseClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("BrowsePage", "entered onClick");
			if (checkInput(selectedType, selectedSize)) {
				progressDialog = ProgressDialog.show(BrowsePage.this, "Please wait...",
						"Retrieving data...", true, true);
				
				Response res = null;
				final APICall browseTask = new APICall(BrowsePage.this);
				try {
					Log.d("onClick", selectedSize + " " + selectedType);
					res = browseTask.execute("GET", "TYPE", selectedSize, selectedType).get();
					Log.d("onClick", res.getData().toString());
				} catch (Exception e) {
					// not network error, this is an exception thrown by AsyncTask's execute method
					// TODO: AsyncTask execute exception
					Log.d("BrowsePage", "error on execute");
				}
				
				progressDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						if (browseTask != null) browseTask.cancel(true);
					}
				});
				
				if (res.getSuccess()) {
					Parcelable type = new Type(res.getData(), selectedSize, selectedType);
					Intent intent = new Intent(BrowsePage.this, TypePage.class);
					intent.putExtra("Type", type);
					progressDialog.cancel();
					startActivity(intent);
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
		if (type.equals("0")) {
			Toast.makeText(this, "Please select an alcohol type", Toast.LENGTH_SHORT).show();
			return false;
		} else if (size.equals("0")) {
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
    		selectedSize = SizeTypeUtility.INSTANCE.convertSize(size) + "";
    	}
    	
    	public void onNothingSelected(AdapterView<?> parent) {
    		
    	}
    }
    
    private class TypeSpinnerActivity extends Activity implements OnItemSelectedListener {
    	public void onItemSelected(AdapterView<?> parent, View view,
    			int pos, long id) {
    		String type = parent.getItemAtPosition(pos).toString();
    		selectedType = SizeTypeUtility.INSTANCE.convertType(type) + "";
    	}
    	
    	public void onNothingSelected(AdapterView<?> parent) {
    		
    	}
    }
}