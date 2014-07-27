package com.example.message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryOthersListAdapter extends BaseAdapter {
	
	private Context context = null;
	private ArrayList<ListItem> data = null;
	private int resource = 0;
	
	public HistoryOthersListAdapter(Context context, ArrayList<ListItem> data, int resource) {
		this.context = context;
		this.data = data;
		this.resource = resource;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity)context;
		ListItem item = (ListItem)getItem(position);
		LinearLayout v = (LinearLayout)activity.getLayoutInflater().inflate(resource, null);
		((TextView)v.findViewById(R.id.question)).setText(item.getQuestion());
		((TextView)v.findViewById(R.id.hash)).setText(item.getHash());
		((TextView)v.findViewById(R.id.aid)).setText(item.getAid());
		((TextView)v.findViewById(R.id.answer)).setText(item.getAnswer());
		((TextView)v.findViewById(R.id.datetime)).setText(item.getDatetime());
		((TextView)v.findViewById(R.id.thumb_up_count)).setText(item.getThumbUpCount());
		((TextView)v.findViewById(R.id.thumb_down_count)).setText(item.getThumbDownCount());
		((TextView)v.findViewById(R.id.up_icon_color)).setText(item.getUpIconColor());
		((TextView)v.findViewById(R.id.down_icon_color)).setText(item.getDownIconColor());
		((ImageView)v.findViewById(R.id.thumb_up_icon)).setImageResource(item.getThumbUpIcon());
		((ImageView)v.findViewById(R.id.thumb_down_icon)).setImageResource(item.getThumbDownIcon());
		return v;
	}

}
