package com.example.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SendQuestion extends ActionBarActivity {
	private Context context = null;
	
	private ProgressDialog pDialog;
	 
	// JSON parser class
	JSONParser jsonParser = new JSONParser();
    private static final String POST_QUESTION_URL = "http://talktocal.net/messaging/addQuestion.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_QUESTION = "question";
	private String post_question;
	private String post_hash;
	private String post_username;
	
	public void add(Context context, String question, String hash, String username) {
		this.context = context;
		post_question = question;
		post_hash = hash;
		post_username = username;
		new PostQuestion().execute();
	}
	
	class PostQuestion extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
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
//			String post_question = question.getText().toString();
						
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("question", post_question));
				params.add(new BasicNameValuePair("hash", post_hash));
				params.add(new BasicNameValuePair("username", post_username));
 
				Log.d("request!", "starting");
				
				//Posting user data to script 
				JSONObject json = jsonParser.makeHttpRequest(
						POST_QUESTION_URL, "POST", params);
 
				// full json response
				Log.d("Post Comment attempt", json.toString());
 
				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Question Added!", json.toString());	
					return json.getString(TAG_QUESTION);
				}else{
					Log.d("Comment Failure!", json.getString(TAG_QUESTION));
					return json.getString(TAG_QUESTION);
					
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
				Toast.makeText(SendQuestion.this, file_url, Toast.LENGTH_LONG).show();
			}
 
		}
		
	}
}
