package com.android.alcoholpriceapp.util;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.alcoholpriceapp.pages.SearchPage;

public class SplashScreen extends Activity {
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		
		try {
			Thread.sleep(2000); // sleep 2 sec
			Intent intent = new Intent(this, SearchPage.class);
			startActivity(intent);
		} catch(Exception e) {
			
		}
	}
	
	public class SetUp implements Runnable {
		Thread t;
		
		
		@Override
		public void run() {
			SizeTypeUtility.INSTANCE.setContext(SplashScreen.this);
			SizeTypeUtility.INSTANCE.updateSizes();
			SizeTypeUtility.INSTANCE.updateTypes();
		}
		
	}
}
