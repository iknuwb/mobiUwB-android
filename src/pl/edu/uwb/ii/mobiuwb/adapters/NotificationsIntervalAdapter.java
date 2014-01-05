package pl.edu.uwb.ii.mobiuwb.adapters;

import java.util.List;
import pl.edu.uwb.ii.mobiuwb.models.NotificationIntervalModel;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class NotificationsIntervalAdapter extends
		ArrayAdapter<NotificationIntervalModel>
{
	
	public NotificationsIntervalAdapter(Context context,
			List<NotificationIntervalModel> objects)
	{
		super(context, android.R.layout.simple_list_item_1, objects);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater)getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent,
				false);
		TextView textView = (TextView)rowView
				.findViewById(android.R.id.text1);
		
		NotificationIntervalModel aktualnyModel = getItem(position);
		textView.setText(aktualnyModel.getText());
		textView.setTextColor(Color.BLACK);
		return rowView;
	}
	
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater)getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent,
				false);
		TextView textView = (TextView)rowView
				.findViewById(android.R.id.text1);
		
		NotificationIntervalModel aktualnyModel = getItem(position);
		textView.setText(aktualnyModel.getText());
		textView.setTextColor(Color.BLACK);
		return rowView;
	}
}
