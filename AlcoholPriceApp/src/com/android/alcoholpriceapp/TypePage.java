package com.android.alcoholpriceapp;

import com.android.alcoholpriceapp.menu.MenuControl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TypePage extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.type_page);
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