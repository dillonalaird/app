package com.android.alcoholpriceapp.pages;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.R.layout;
import com.android.alcoholpriceapp.R.menu;

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
        
        final TextView mTextView = (TextView) findViewById(R.id.storeNameText);
        
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        mTextView.setText(Integer.toString(id));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}