package com.example.message;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;

public class MainActivity extends ActionBarActivity {

	ShareExternalServer appUtil;
	String regId;
	String userName;
	AsyncTask<Void, Void, String> shareRegidTask;
	
	EditText toUser;
	EditText message;
	Button btnSendMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		appUtil = new ShareExternalServer();
		
		userName = getIntent().getStringExtra(Config.REGISTER_NAME);
/*		
		regId = getIntent().getStringExtra("regId");
		Log.d("MainActivity", "regId: " + regId);		
	    userName = getIntent().getStringExtra(Config.REGISTER_NAME);
	    Log.d("MainActivity", "userName: " + userName);

		//final Context context = this;
		shareRegidTask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String result = appUtil.shareRegIdWithAppServer(regId, userName);
				return result;
			}
			
			@Override
			protected void onPostExecute(String result) {
				shareRegidTask = null;
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			}
			
		};
*/		
		// to send message to another device via Google GCM
		btnSendMessage = (Button) findViewById(R.id.sendMessage);
		btnSendMessage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toUser = (EditText) findViewById(R.id.toUser);
		        String toUserName = toUser.getText().toString();
		        
		        message = (EditText) findViewById(R.id.message);
		        String messageToSend = message.getText().toString();
		        
		        if (TextUtils.isEmpty(toUserName)) {
		        	Toast.makeText(getApplicationContext(), "To User is empty!", Toast.LENGTH_LONG).show();
		        } else if (TextUtils.isEmpty(messageToSend)) {
		        	Toast.makeText(getApplicationContext(), "Message is empty!", Toast.LENGTH_LONG).show();
		        } else {
		        	Log.d("MainActivity", "Sending message to user: " + toUserName);
		        	sendMessageToGCMAppServer(toUserName, messageToSend);
		        }     
			}
		});	
		
		//shareRegidTask.execute(null, null, null);
		
		
		
	}

	private void sendMessageToGCMAppServer(final String toUserName, final String messageToSend) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String result = appUtil.sendMessage(userName, toUserName, messageToSend);
				Log.d("MainActivity", "Result: " + result);
				return result;
			}
			@Override
			protected void onPostExecute(String msg) {
				Log.d("MainActivity", "Result: " + msg);
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}
		}.execute(null, null, null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
