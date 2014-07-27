package com.example.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddComment extends ActionBarActivity implements OnClickListener {

	private EditText title, message;
	private Button  mSubmit;
	private ProgressDialog pDialog;
 
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
    private static final String POST_COMMENT_URL = "http://talktocal.net/android/addcomment.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment);
		
		title = (EditText)findViewById(R.id.title);
		message = (EditText)findViewById(R.id.message);
		
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		new PostComment().execute();
	}
	
	class PostComment extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddComment.this);
			pDialog.setMessage("Posting Comment...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
			int success;
			String post_title = title.getText().toString();
			String post_message = message.getText().toString();
			
			//Retrieving Saved Username Data:
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddComment.this);
			String post_username = sp.getString("username", "anon");
			
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", post_username));
				params.add(new BasicNameValuePair("title", post_title));
				params.add(new BasicNameValuePair("message", post_message));
 
				Log.d("request!", "starting");
				
				//Posting user data to script 
				JSONObject json = jsonParser.makeHttpRequest(
						POST_COMMENT_URL, "POST", params);
 
				// full json response
				Log.d("Post Comment attempt", json.toString());
 
				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Comment Added!", json.toString());	
					finish();
					return json.getString(TAG_MESSAGE);
				}else{
					Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
 
			return null;
			
		}
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(AddComment.this, file_url, Toast.LENGTH_LONG).show();
			}
 
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_comment, menu);
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