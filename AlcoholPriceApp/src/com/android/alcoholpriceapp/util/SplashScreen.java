package com.android.alcoholpriceapp.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.pages.SearchPage;

/**
 * Displays an initial splash screen before starting the main SearchPage
 * Activity.
 */
public class SplashScreen extends Activity {
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// Updates the size and type lists before it launches SearchPage
				SizeTypeUtility.INSTANCE.setContext(SplashScreen.this);
				SizeTypeUtility.INSTANCE.updateSizes();
				SizeTypeUtility.INSTANCE.updateTypes();
				startActivity(new Intent(SplashScreen.this, SearchPage.class));
				finish();
			}
		}, 3000); // waits 3000 ms or 3 sec
	}
}
