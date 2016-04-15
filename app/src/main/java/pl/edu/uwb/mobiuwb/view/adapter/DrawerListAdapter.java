package pl.edu.uwb.mobiuwb.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.model.DrawerListAdapterModel;

import java.util.List;

/**
 * Jest to adapter listy bocznej wysuwanej.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerListAdapterModel>
{
    /**
     * Kontekst widoku lub aplikacji.
     */
    Context context;

    /**
     * Nadaje zmienne.
     * @param context Kontekst widoku lub aplikacji.
     * @param items Modele tego adaptera.
     */
    public DrawerListAdapter(Context context, List<DrawerListAdapterModel> items)
    {
        super(context, R.layout.drawer_list_adapter, items);
        this.context = context;
    }


    /**
     * Wydarza się, gdy system zechce utworzyć pojedynczy element
     * kontrolki ListView.
     * Ta funkcja służy za twórcę elementu prezentującego element
     * bocznego menu będący Jednostką, bazując
     * na skonfigurowanym przez programistę modelu kontrolki.
     * Event ten korzysta z wzorca ViewHolder, który zapamiętuje wygenerowane
     * kontrolki, aby system nie musiał tworzyć ich ponownie.
     * @param position Który element z listy jest aktualnie tworzony.
     * @param convertView Kontrolka listy.
     * @param parent Rodzic kontrolki.
     * @return Utworzona kontrolka, wypełniona.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(
                    R.layout.drawer_list_adapter,
                    parent,
                    false);

            viewHolder = new ViewHolder();
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(
                    R.id.drawer_List_adapter_linearlayout);
            viewHolder.textView = (TextView) convertView.findViewById(
                    R.id.drawer_list_adapter_textview);
            viewHolder.imageView = (ImageView) convertView.findViewById(
                    R.id.drawer_list_adapter_imageview);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ListView listView = (ListView) parent;

        Log.d("MOBIUWB",
              "Selected: " + listView.getCheckedItemPosition() + " Current: " + position);
        if (listView.getCheckedItemPosition() == position)
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

    /**
     * Klasa ta służy jako zapamiętywacz kontrolek, aby system
     * nie musiał tworzyć ich ponownie.
     */
    private class ViewHolder
    {
        /**
         * Referencja do kontenera.
         */
        LinearLayout linearLayout;

        /**
         * Obrazek.
         */
        ImageView imageView;

        /**
         * Pojemnik na tekst.
         */
        TextView textView;
    }
}
