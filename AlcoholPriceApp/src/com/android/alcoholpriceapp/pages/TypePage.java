package com.android.alcoholpriceapp.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.alcoholpriceapp.R;
import com.android.alcoholpriceapp.listadapters.TypeAdapter;
import com.android.alcoholpriceapp.menu.MenuControl;
import com.android.alcoholpriceapp.models.Type;
import com.android.alcoholpriceapp.util.SizeTypeUtility;

public class TypePage extends Activity{
	private Type type = null;
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.type_page);
		
		Bundle bundle = getIntent().getExtras();
		type = bundle.getParcelable("Type");
		
		TypeAdapter adapter = new TypeAdapter(this, R.layout.type_row, type.getProducts());
		
		listView = (ListView) findViewById(R.id.typeProductListView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(listViewItemClickListener);
		
		TextView alcoholTypeText = (TextView) findViewById(R.id.alcoholTypeText);
		TextView alcoholSizeText = (TextView) findViewById(R.id.alcoholTypeSizeText);
		
		alcoholTypeText.setText(SizeTypeUtility.INSTANCE.convertType(Integer.parseInt(type.getAlcoholType())));
		alcoholSizeText.setText(SizeTypeUtility.INSTANCE.convertSize(Integer.parseInt(type.getSize())));
		
	}
	
	private OnItemClickListener listViewItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(TypePage.this, ProductPage.class);
			
			intent.putExtra("from", 2);
			String s = type.getProducts().get((int) listView.getItemIdAtPosition(arg2));
			intent.putExtra("name", type.getProducts()
					.get((int) listView.getItemIdAtPosition(arg2)));
			intent.putExtra("size", SizeTypeUtility.INSTANCE.convertSize(Integer.parseInt(type.getSize())));
			startActivity(intent);
		}
	};

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