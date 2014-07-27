package com.example.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

public class Reply extends ActionBarActivity implements OnClickListener {

	private EditText answer;
	private Button send_answer;
	private ProgressDialog pDialog;
 
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
    private static final String POST_ANSWER_URL = "http://talktocal.net/messaging/addAnswer.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ANSWER = "answer";
	private static final String TAG_QUESTION = "question";
	private static final String TAG_HASH = "hash";
	private static final String TAG_SID = "sid";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		
		//get the question and hash to show on the TextView
		TextView question = (TextView)findViewById(R.id.question);
		TextView hash = (TextView)findViewById(R.id.hash);
		question.setText(getIntent().getStringExtra(TAG_QUESTION));
		hash.setText(getIntent().getStringExtra(TAG_HASH));

		answer = (EditText)findViewById(R.id.answer);
		
		send_answer = (Button)findViewById(R.id.send_answer);
		send_answer.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		new PostAnswer().execute();
	}
	
	class PostAnswer extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Reply.this);
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
			String post_answer = answer.getText().toString();
			
			//Retrieving Saved Username Data:
			//SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Reply.this);
			//String post_username = sp.getString("regId", "anon");
			final SharedPreferences prefs = getSharedPreferences(TabsActivity.class.getSimpleName(), 
					Context.MODE_PRIVATE);
			String post_username = prefs.getString("regId", "anon");
			
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("answer", post_answer));
				params.add(new BasicNameValuePair("username", post_username));
				params.add(new BasicNameValuePair("original_id", getIntent().getStringExtra(TAG_SID)));
 
				Log.d("request!", "starting");
				
				//Posting user data to script 
				JSONObject json = jsonParser.makeHttpRequest(
						POST_ANSWER_URL, "POST", params);
 
				// full json response
				Log.d("Post Comment attempt", json.toString());
 
				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Answer Added!", json.toString());	
					finish();
					return json.getString(TAG_ANSWER);
				}else{
					Log.d("Comment Failure!", json.getString(TAG_ANSWER));
					return json.getString(TAG_ANSWER);
					
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
				Toast.makeText(Reply.this, file_url, Toast.LENGTH_LONG).show();
			}
 
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reply, menu);
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
