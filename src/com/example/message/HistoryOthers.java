package com.example.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryOthers extends ActionBarActivity {
	
	private ProgressDialog pDialog;
    private static final String READ_COMMENTS_URL = "http://talktocal.net/messaging/history_others.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_POSTS = "posts";
//	private static final String TAG_POST_ID = "post_id";
//	private static final String TAG_USERNAME = "username";
	private static final String TAG_QUESTION = "question";
	private static final String TAG_HASH = "hash";
	private static final String TAG_AID = "aid";
	private static final String TAG_ANSWER = "answer";
	private static final String TAG_DATETIME = "datetime";
	private static final String TAG_THUMB_UP_COUNT = "thumb_up_count";
	private static final String TAG_THUMB_DOWN_COUNT = "thumb_down_count";
	private static final String TAG_UP_ICON_COLOR = "up_icon_color";
	private static final String TAG_DOWN_ICON_COLOR = "down_icon_color";
	private static final String TAG_ANSWER_LIKED = "answer_liked";
	private static final String TAG_ANSWER_DISLIKED = "answer_disliked";

	SendQuestion sq;
	String post_username;
	ShareExternalServer appUtil;
	Likes likes;
	
	//it's important to note that the message is both in the parent branch of 
	//our JSON tree that displays a "Post Available" or a "No Post Available" message,
	//and there is also a message for each individual post, listed under the "posts"
	//category, that displays what the user typed as their message.
	
	//An array of all of our comments
	private JSONArray mComments = null;
	//manages all of our comments in a list.
//	private ArrayList<HashMap<String, String>> mCommentList;
//custom ArrayList to include image
	private ArrayList<ListItem> mCommentList;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_others);
		
		final SharedPreferences prefs = getSharedPreferences(TabsActivity.class.getSimpleName(), 
				Context.MODE_PRIVATE);
		post_username = prefs.getString("regId", "anon");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//loading the comments via AsyncTask
		new LoadComments().execute();
	}

	public void addQuestion(View v) {
		EditText question_to_send = (EditText)findViewById(R.id.question_to_send);
		Spinner hash_to_send = (Spinner)findViewById(R.id.spnHash);
		
		if (TextUtils.isEmpty(question_to_send.getText().toString())) {
			Log.d("ReadComments","empty");
		} else {
			sq = new SendQuestion();
			sq.add(this, question_to_send.getText().toString(), 
					hash_to_send.getSelectedItem().toString(), post_username);
			
			//String toUserName = "cal";
			sendMessageToGCMAppServer("cal", question_to_send.getText().toString());
			
			//hide the keyboard
			InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(question_to_send.getWindowToken(), 0);
			
			question_to_send.setText("");
			Toast.makeText(this, "Question added!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void like(View v) {
		LinearLayout ll = (LinearLayout)v.getParent().getParent().getParent();
		TextView tv_aid = (TextView)ll.findViewById(R.id.aid);
		
		likes = new Likes();
		TextView tv_up_icon_color = (TextView)ll.findViewById(R.id.up_icon_color);
		ImageView iv = (ImageView)ll.findViewById(R.id.thumb_up_icon);
		//if grey, change to color and like it
		if (tv_up_icon_color.getText().toString() == "grey") {
			iv.setImageResource(R.drawable.thumb_up);
			tv_up_icon_color.setText("colored");
			likes.like(this, tv_aid.getText().toString(), post_username);
		//if colored, change to grey and unlike it
		} else {
			iv.setImageResource(R.drawable.thumb_up_g);
			tv_up_icon_color.setText("grey");
			likes.unlike(this, tv_aid.getText().toString(), post_username);
		}				
	}
	
	public void dislike(View v) {
		LinearLayout ll = (LinearLayout)v.getParent().getParent().getParent();
		TextView tv_aid = (TextView)ll.findViewById(R.id.aid);
		
		likes = new Likes();
		TextView tv_down_icon_color = (TextView)ll.findViewById(R.id.down_icon_color);
		ImageView iv = (ImageView)ll.findViewById(R.id.thumb_down_icon);
		//if grey, change to color and dislike it
		if (tv_down_icon_color.getText().toString() == "grey") {
			iv.setImageResource(R.drawable.thumb_down);
			tv_down_icon_color.setText("colored");
			likes.dislike(this, tv_aid.getText().toString(), post_username);
		//if colored, change to grey and undislike it
		} else {
			iv.setImageResource(R.drawable.thumb_down_g);
			tv_down_icon_color.setText("grey");
			likes.undislike(this, tv_aid.getText().toString(), post_username);
		}
	}

	private void sendMessageToGCMAppServer(final String toUserName, final String messageToSend) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				appUtil = new ShareExternalServer();
				String result = appUtil.sendMessage("emi", toUserName, messageToSend);
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
	
	/**
	 * Retrieves json data of comments
	 */
	public void updateJSONdata() {
		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..
		
//		mCommentList = new ArrayList<HashMap<String, String>>();
		mCommentList = new ArrayList<ListItem>();
		
		// Bro, it's time to power up the J parser 
		JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		//back a JSON object.  Boo-yeah Jerome.
		
		//instead of just getting json from the url, pass username param to only get its history
		//with makeHttpRequest
//		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);
		
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", post_username));

		Log.d("HistoryRequest!", "starting");
		
		//Posting user data to script 
		JSONObject json = jParser.makeHttpRequest(
				READ_COMMENTS_URL, "POST", params);
		

		//when parsing JSON stuff, we should probably
		//try to catch any exceptions:
		try {
			
			//I know I said we would check if "Posts were Avail." (success==1)
			//before we tried to read the individual posts, but I lied...
			//mComments will tell us how many "posts" or comments are
			//available
			mComments = json.getJSONArray(TAG_POSTS);

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				//gets the content of each tag
				String question = c.getString(TAG_QUESTION);
				String hash = c.getString(TAG_HASH);
				String aid = c.getString(TAG_AID);
				String answer = c.getString(TAG_ANSWER);
				String datetime = c.getString(TAG_DATETIME);
				String thumb_up_count = c.getString(TAG_THUMB_UP_COUNT);
				String thumb_down_count = c.getString(TAG_THUMB_DOWN_COUNT);
				String up_icon_color;
				String down_icon_color;

				Boolean answer_liked = c.getBoolean(TAG_ANSWER_LIKED);
				Boolean answer_disliked = c.getBoolean(TAG_ANSWER_DISLIKED);
				int thumb_up_icon = 0;
				int thumb_down_icon = 0;
				
				//answer_liked true means already liked, so show colored thumb up
				if (answer_liked) {
					thumb_up_icon = R.drawable.thumb_up;
					up_icon_color = "colored";
				} else {
					thumb_up_icon = R.drawable.thumb_up_g;
					up_icon_color = "grey";
				}
				
				if (answer_disliked) {
					thumb_down_icon = R.drawable.thumb_down;
					down_icon_color = "colored";
				} else {
					thumb_down_icon = R.drawable.thumb_down_g;
					down_icon_color = "grey";
				}
				
				ListItem item = new ListItem();
				item.setId((new Random()).nextLong());
				item.setQuestion(question);
				item.setHash(hash);
				item.setAid(aid);
				item.setAnswer(answer);
				item.setDatetime(datetime);
				item.setThumbUpCount(thumb_up_count);
				item.setThumbDownCount(thumb_down_count);
				item.setUpIconColor(up_icon_color);
				item.setDownIconColor(down_icon_color);
				item.setThumbUpIcon(thumb_up_icon);
				item.setThumbDownIcon(thumb_down_icon);
				mCommentList.add(item);
/*				
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
			  
				map.put(TAG_QUESTION, question);
				map.put(TAG_HASH, hash);
				map.put(TAG_ANSWER, answer);
				map.put(TAG_DATETIME, datetime);
				map.put(TAG_THUMB_UP_COUNT, thumb_up_count);
				map.put(TAG_THUMB_DOWN_COUNT, thumb_down_count);
			 
				// adding HashList to ArrayList
				mCommentList.add(map);
*/				
				//annndddd, our JSON data is up to date same with our array list
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * Inserts the parsed data into our listview
	 */
	private void updateList() {
		// For a ListActivity we need to set the List Adapter, and in order to do
		//that, we need to create a ListAdapter.  This SimpleAdapter,
		//will utilize our updated Hashmapped ArrayList, 
		//use our single_post xml template for each item in our list,
		//and place the appropriate info from the list to the
		//correct GUI id.  Order is important here.
/*
		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.single_history_others, new String[] { TAG_QUESTION, TAG_HASH, TAG_ANSWER, 
						TAG_DATETIME, TAG_THUMB_UP_COUNT, TAG_THUMB_DOWN_COUNT
						 }, new int[] { R.id.question, R.id.hash, R.id.answer, 
						R.id.datetime, R.id.thumb_up_count, R.id.thumb_down_count
						 });
		setListAdapter(adapter);
*/
		
		HistoryOthersListAdapter adapter = new HistoryOthersListAdapter(this, mCommentList, 
				R.layout.single_history_others);
		ListView list_others = (ListView)findViewById(R.id.list_others);
		list_others.setAdapter(adapter);
		
		// Optional: when the user clicks a list item we could do something.
		//ListView lv = getListView();	need to extend ListActivity to use this method
		list_others.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("ReadComments", "item man!");

				// TODO Auto-generated method stub	
			}
		});

/*	
		//TextView reply = (TextView) findViewById(R.id.reply);
		LinearLayout vLinear = (LinearLayout)this.getLayoutInflater().inflate(R.layout.single_post, null);
		TextView reply = (TextView) vLinear.findViewById(R.id.reply);
		reply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent i = new Intent(ReadComments.this, AddComment.class);
				//startActivity(i);		
				Log.d("ReadComments", "clicked man!");
			}
		});
*/
	}
	
	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(HistoryOthers.this);
			pDialog.setMessage("Loading Comments...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected Boolean doInBackground(Void... arg0) {
			//we will develop this method in version 2
			updateJSONdata();
			return null;

		}


		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			//we will develop this method in version 2
			updateList();
		}
		

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_comments, menu);
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
