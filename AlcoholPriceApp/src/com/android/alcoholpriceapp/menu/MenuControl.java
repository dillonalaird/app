package com.android.alcoholpriceapp.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.pages.BrowsePage;
import com.android.alcoholpriceapp.pages.SearchPage;

/**
 * This is a basic controller that takes selected menu items and launches their
 * corresponding activities.
 */
public class MenuControl {

	/**
	 * Takes the current activity and a menu item and launches the corresponding
	 * activity.
	 * 
	 * @param item
	 * 			The menu item corresponding to a certain activity.
	 * @param current
	 * 			The current activity running when the menu item is selected.
	 * @return true if the activity has been launched.
	 */
	public static boolean selectMenuItem(MenuItem item, Activity current) {
    	switch (item.getItemId()) {
			case R.id.search:
				launchActivity(current, SearchPage.class);
				return true;
			case R.id.browse:
				launchActivity(current, BrowsePage.class);
				return true;
			default:
				return current.onOptionsItemSelected(item);
    	}
	}

	/**
	 * Launches an activity from a current activity.
	 * 
	 * @param current
	 * 			The current activity.
	 * @param target
	 * 			The target activity being launched.
	 */
    private static void launchActivity(Activity current, Class<?> target) {
    	Intent intent = new Intent(current, target);
    	current.startActivity(intent);
    }
}