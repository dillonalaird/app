package com.android.alcoholpriceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.alcoholpriceapp.menu.MenuControl;

/**
 * The browse page for the Alcohol Price Application. Allows the user to browse
 * alcohols by type and size.
 */
public class Browse extends Activity {

	/** The spinner containing all of the alcohol size information. */
	private Spinner sizeSpinner;
	/** The spinner containing all of the alcohol type information. */
	private Spinner alcoholTypeSpinner;
	/** Browse button that initiates the browse process. */
	private Button browseButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse);

		findAllViewsById();
		initSpinner();
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

	public void addListenerOnButton() {
		browseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// This is where we need to send the HTTP request to the server
			}
		});
	}

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
}