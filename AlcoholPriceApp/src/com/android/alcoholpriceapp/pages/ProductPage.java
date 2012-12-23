package com.android.alcoholpriceapp.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.R.id;
import com.android.alcoholpriceapp.R.layout;
import com.android.alcoholpriceapp.R.menu;
import com.android.alcoholpriceapp.listadapters.ProductAdapter;
import com.android.alcoholpriceapp.models.PriceInfo;
import com.android.alcoholpriceapp.models.Product;

/**
 * Product page is the activity that shows different prices for the particular alcohol/size
 * combination. The intent that creates it has a parcelable Product object passed into it 
 * from which ProductPage created it's layout.
 * 
 * @author Troy Cosentino
 */
public class ProductPage extends Activity {
	Product product = null;
	
	private ListView listView;

	/**
	 * Called when the Activity is created. Creates the Product object from the parcel 
	 * passed through the intent. Then creates the adapter and populates the list view.
	 * Finally, sets a listener for when an item in the listview is clicked.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);
        
        Bundle bundle = getIntent().getExtras();
        product = bundle.getParcelable("Product");
        
        ProductAdapter adapter = new ProductAdapter(this, R.layout.product_row, product.getProductInfos());
        
        //String[] testArray = {"troy", "Dillon", "Shane", "Raj"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testArray);
        
        listView = (ListView) findViewById(R.id.itemsListView);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(listviewItemClickListener);
        
        TextView alcoholNameText = (TextView) findViewById(R.id.alcoholNameText);
        TextView alcoholSizeText = (TextView) findViewById(R.id.alcoholSizeText);
        
        alcoholNameText.setText(product.getProductName());
        alcoholSizeText.setText(PriceInfo.convertSize(Integer.parseInt(product.getSize())));
    }
    
    /**
     * The Listener for when an item is clicked. Currently a dummy function that passes 
     * it onto the store page. 
     */
    private OnItemClickListener listviewItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(ProductPage.this, StorePage.class);
			
			//passing ID to do search on other side
			intent.putExtra("ID", product.getProductInfos()
					.get((int)listView.getItemIdAtPosition(arg2)).getStoreID());
			startActivity(intent);
		} 
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}