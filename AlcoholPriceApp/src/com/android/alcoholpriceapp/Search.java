package com.android.alcoholpriceapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		findAllViewsById();
		initSpinner();
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

	public void addListenerOnButton() {
		searchButton.setOnClickListener(new OnClickListener() {
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