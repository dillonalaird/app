package com.android.alcoholpriceapp.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.alcoholpriceapp.pages.SearchPage;

public class SplashScreen extends Activity {
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(0x7f030004);
		
		try {
			(new Thread(new SetUp())).start(); // execute SetUp in another thread
			Thread.sleep(2000); // sleep 2 sec in this thread
			Intent intent = new Intent(this, SearchPage.class);
			startActivity(intent);
		} catch(Exception e) {
			
		}
	}
	
	public class SetUp implements Runnable {
		@Override
		public void run() {
			SizeTypeUtility.INSTANCE.setContext(SplashScreen.this);
			SizeTypeUtility.INSTANCE.updateSizes();
			SizeTypeUtility.INSTANCE.updateTypes();
		}
		
	}
}
