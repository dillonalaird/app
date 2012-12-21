package com.android.alcoholpriceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class StorePage extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.store_page);
        
        //final TextView mTextView = (TextView) findViewById(R.id.test);
        
        Intent intent = getIntent();
        String message = intent.getStringExtra(ProductPage.EXTRA_MESSAGE);
        //mTextView.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}