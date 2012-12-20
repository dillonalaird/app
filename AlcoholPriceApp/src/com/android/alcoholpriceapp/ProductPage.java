package com.android.alcoholpriceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.alcoholpriceapp.listadapters.ProductAdapter;

public class ProductPage extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);
        
        //Intent parentIntent = getIntent();
        //String searchResponse = parentIntent.getStringExtra("data");
        
        // TODO: this product needs to be gotten from the intent, not created here
        Product product = new Product("this doesnt take real data right now");
        ProductAdapter adapter = new ProductAdapter(this, R.layout.product_row, product.getProductInfos());
        
        //String[] testArray = {"troy", "Dillon", "Shane", "Raj"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testArray);
        
        final ListView listView = (ListView) findViewById(R.id.itemsListView);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO change from toast to change activity
				Intent intent = new Intent(ProductPage.this, StorePage.class);
				intent.putExtra(EXTRA_MESSAGE, listView.getItemAtPosition(arg2).toString());
				startActivity(intent);
				
				Toast.makeText(getBaseContext(), listView.getItemAtPosition(arg2).toString(), 1000).show();
			} 
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}