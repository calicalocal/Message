package com.example.message;

import android.support.v7.app.ActionBarActivity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabsActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		
		TabHost host = getTabHost();
		host.setup();
		
		TabSpec tab1 = host.newTabSpec("tab1");
		tab1.setIndicator(
			getResources().getString(R.string.tab1)
		);
		//Intent tab1intent = new Intent(this, DisplayActivity.class);
		Intent tab1intent = new Intent(this, ReadComments.class);
	    tab1.setContent(tab1intent);
		//tab1.setContent(R.id.tab1);
		host.addTab(tab1);
		
		TabSpec tab2 = host.newTabSpec("tab2");
		tab2.setIndicator(
			getResources().getString(R.string.tab2)
		);
		
/*
	    //tab2.setContent(new Intent(this, TwitterActivity.class));
		Intent tab2intent = new Intent(this, MainActivity.class);
		tab2intent.putExtra(Config.REGISTER_NAME, getIntent().getStringExtra(Config.REGISTER_NAME));
		tab2intent.putExtra("regId", getIntent().getStringExtra("regId"));
*/
		Intent tab2intent = new Intent(this, History.class);		
	    tab2.setContent(tab2intent);
		//tab2.setContent(R.id.tab2);
		host.addTab(tab2);
		
		TabSpec tab3 = host.newTabSpec("tab3");
		tab3.setIndicator(
			getResources().getString(R.string.tab3)
		);
		
//		Intent tab3intent = new Intent(this, MainActivity.class);
//		tab3intent.putExtra(Config.REGISTER_NAME, getIntent().getStringExtra(Config.REGISTER_NAME));
//		tab3intent.putExtra("regId", getIntent().getStringExtra("regId"));
		Intent tab3intent = new Intent(this, HistoryOthers.class);
		tab3.setContent(tab3intent);
		//tab3.setContent(R.id.tab3);
		host.addTab(tab3);
		
		host.setCurrentTab(0);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabs, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
