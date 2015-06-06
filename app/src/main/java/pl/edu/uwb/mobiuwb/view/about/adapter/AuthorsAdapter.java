package pl.edu.uwb.mobiuwb.view.about.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.edu.uwb.mobiuwb.R;

/**
 * Created by sennajavie on 2015-05-15.
 */
public class AuthorsAdapter extends ArrayAdapter<String>
{
    public AuthorsAdapter(Context context, int textViewResourceId,
                          List<String> objects)
    {
        super(context, textViewResourceId, objects);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(
                    parent.getContext()).inflate(
                    R.layout.round_button, parent, false);
        }

        TextView textView = (TextView)convertView.findViewById(R.id.round_button_text_view);
        String text = getItem(position);
        textView.setText(text);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.round_button_image_view);
        imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_action_user));
        return convertView;
    }
}
