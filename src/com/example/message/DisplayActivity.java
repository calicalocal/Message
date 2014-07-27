package com.example.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DisplayActivity extends ActionBarActivity {

	ArrayList<HashMap<String, String>> sentenceList;
	private ProgressDialog progressMessage;
	JSONParser jParser = new JSONParser();
	private static String url = "http://talktocal.net/messaging/list_sentences.php";
	JSONArray sentences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

/*
		String message = getIntent().getStringExtra("message");		
		ArrayList<String> data = new ArrayList<String>();
		data.add(message);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this, android.R.layout.simple_list_item_1, data);
		ListView list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
*/
		
/*
		setContentView(R.layout.activity_list_animal);
		sentenceList = new ArrayList<HashMap<String, String>>();
		new LoadAllProducts().execute();
*/
	}

/*	
	class LoadAllProducts extends AsyncTask<String, String, String> {
		 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressMessage = new ProgressDialog(ListAnimalActivity.this);
			progressMessage.setMessage("Loading ...");
			progressMessage.setIndeterminate(false);
			progressMessage.setCancelable(false);
			progressMessage.show();
		}
		 
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(url, "GET", params);
		 
			Log.d("Animals: ", json.toString());
		 
			try {
				int success = json.getInt("success");
		 
				if (success == 1) {
					animals = json.getJSONArray("animals");
					for (int i = 0; i < animals.length(); i++) {
						JSONObject c = animals.getJSONObject(i);
						String id = c.getString("animal_id");
						String name = c.getString("animal_name");
						String type = c.getString("animal_type");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("animal_id", id);
						map.put("animal_name", name);
						map.put("animal_type", type);
						animalList.add(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		 
			return null;
		}

		protected void onPostExecute(String file_url) {
			progressMessage.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					ListAdapter adapter = new SimpleAdapter(
						ListAnimalActivity.this, animalList,
						R.layout.view_animal_entry, new String[] { "animal_id",
							"animal_name","animal_type"},
						new int[] { R.id.animal_id, R.id.animal_name,R.id.animal_type });
					setListAdapter(adapter);
				}
			});
		}
		 
	}	//end of class LoadAllProducts
*/	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
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
