package com.example.mobiuwb.view.adapter;

import java.util.List;

import com.example.mobiuwb.MobiUwbApp;
import com.example.mobiuwb.R;
import com.example.mobiuwb.view.model.DrawerListAdapterModel;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DrawerListAdapter extends ArrayAdapter<DrawerListAdapterModel> 
{
	Context context;

	public DrawerListAdapter(Context context, List<DrawerListAdapterModel> items) {
		super(context,R.layout.drawer_list_adapter ,items);
		this.context = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)
					context.getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(
					R.layout.drawer_list_adapter, 
					parent, 
					false);
			
			viewHolder = new ViewHolder();
			viewHolder.linearLayout =(LinearLayout)convertView.findViewById(
					R.id.drawer_List_adapter_linearlayout);
			viewHolder.textView = (TextView)convertView.findViewById(
					R.id.drawer_list_adapter_textview);
			viewHolder.imageView = (ImageView)convertView.findViewById(
					R.id.drawer_list_adapter_imageview);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		ListView listView = (ListView)parent;
		
		Log.d("MOBIUWB", "Selected: " + listView.getCheckedItemPosition() + " Current: " + position);
		if(listView.getCheckedItemPosition() == position)
		{
			viewHolder.linearLayout.setBackgroundColor(
					context.getResources().getColor(
							R.color.drawer_listview_item_selected_background));
		}
		else
		{
			viewHolder.linearLayout.setBackgroundColor(Color.TRANSPARENT);
		}
		viewHolder.imageView.setImageDrawable(getItem(position).getIcon());
		viewHolder.textView.setText(getItem(position).getText());
		
		
		return convertView;
	}

	private class ViewHolder
	{
		LinearLayout linearLayout;
		ImageView imageView;
		TextView textView;
	}
}
