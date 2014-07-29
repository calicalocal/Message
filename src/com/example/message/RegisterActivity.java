package com.example.message;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.facebook.*;
import com.facebook.model.*;

public class RegisterActivity extends ActionBarActivity implements FacebookLogin.OnItemSelectedListener {

	  Button btnGCMRegister;
	  Button btnAppShare;
	  GoogleCloudMessaging gcm;
	  Context context;
	  String regId;
	  EditText userName;
//	  String name;
	  
	  ShareExternalServer appUtil;
	  AsyncTask<Void, Void, String> shareRegidTask;
	  
	  public static final String REG_ID = "regId";
	  private static final String APP_VERSION = "appVersion";
	 
	  static final String TAG = "Register Activity";
	  
	  private MainFragment mainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
/*
	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
*/
/*
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					// make request to the /me API
					Request.newMeRequest(session, new Request.GraphUserCallback() {

					  // callback after Graph API response with user object
					  @Override
					  public void onCompleted(GraphUser user, Response response) {
						  if (user != null) {
						    TextView welcome = (TextView) findViewById(R.id.textView1);
						    welcome.setText("Hello " + user.getName() + "!");
						  }
					  }
					}).executeAsync();
				}
			}
		});
*/		
		
		
		context = getApplicationContext();

/*
		btnGCMRegister = (Button) findViewById(R.id.btnGCMRegister);
		btnGCMRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(regId)) {
					regId = registerGCM();
					Log.d("RegisterActivity", "GCM RegId: " + regId);
				} else {
					Toast.makeText(getApplicationContext(), "Already registered with GCM server!", 
						Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	    btnAppShare = (Button) findViewById(R.id.btnAppShare);
	    btnAppShare.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View arg0) {
	    		userName = (EditText) findViewById(R.id.userName);
	    		name = userName.getText().toString();
	    	
	    		if (TextUtils.isEmpty(regId)) {
	    			Toast.makeText(getApplicationContext(), "RegId is empty!",
	    				Toast.LENGTH_SHORT).show();
	    		} else if (TextUtils.isEmpty(name)) {
	    			Toast.makeText(getApplicationContext(), "Name is empty!",
	    				Toast.LENGTH_SHORT).show();
	    		} else {
	    			appUtil = new ShareExternalServer();
	    			
	    			//final Context context = this;
	    			shareRegidTask = new AsyncTask<Void, Void, String>() {

	    				@Override
	    				protected String doInBackground(Void... params) {
	    					String result = appUtil.shareRegIdWithAppServer(regId, name);
	    					return result;
	    				}
	    				
	    				@Override
	    				protected void onPostExecute(String result) {
	    					shareRegidTask = null;
	    					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
	    				}
	    				
	    			};
	    			shareRegidTask.execute(null, null, null);
	    			
	    			
	    			
	    			Intent i = new Intent(getApplicationContext(),
	    				TabsActivity.class);
	    			i.putExtra(Config.REGISTER_NAME, name);
	    			i.putExtra("regId", regId);
	    			Log.d("RegisterActivity",
	    				"onClick of Share: Before starting main activity.");
	    			startActivity(i);
	    			finish();
	    			Log.d("RegisterActivity", "onClick of Share: After finish.");
	    		}
	    	}
	    });	
*/	
	}
	
	@Override
	public void onLoggedIn(final String fbname) {
		if (TextUtils.isEmpty(regId)) {
			regId = registerGCM();
			Log.d("RegisterActivity", "GCM RegId: " + regId);
		}
		appUtil = new ShareExternalServer();
    			
		//final Context context = this;
		shareRegidTask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String result = appUtil.shareRegIdWithAppServer(regId, fbname);
				return result;
			}
				
			@Override
			protected void onPostExecute(String result) {
				shareRegidTask = null;
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			}
				
		};
		shareRegidTask.execute(null, null, null);
	
		Intent i = new Intent(getApplicationContext(),
			TabsActivity.class);
		i.putExtra(Config.REGISTER_NAME, fbname);
		i.putExtra("regId", regId);
		Log.d("RegisterActivity",
			"onClick of Share: Before starting main activity.");
		startActivity(i);
		finish();
		Log.d("RegisterActivity", "onClick of Share: After finish.");

	}

	public String registerGCM() {
		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(context);
		
		if (TextUtils.isEmpty(regId)) {
			registerInBackground();
			
			Log.d("RegisterActivity", "registerGCM - successfully registered with GCM server - regId: "
					+ regId);
		} else {
			Toast.makeText(getApplicationContext(), "RegId already available. RegId: " + regId, 
				Toast.LENGTH_SHORT).show();
		}
		return regId;
	}
	
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
			TabsActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationID = prefs.getString(REG_ID, "");
		if (registrationID.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationID;
	}
	
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
				.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity", "I never expected this! Going down!" + e);
			throw new RuntimeException(e);
		}
	}
	
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: " + regId);
					msg = "Device registered, registration ID=" + regId;
					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}
			
			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(getApplicationContext(), "Registered with GCM Server." + msg, Toast.LENGTH_SHORT).show();
			}
			
		}.execute(null, null, null);
	}
	
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
			TabsActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}
