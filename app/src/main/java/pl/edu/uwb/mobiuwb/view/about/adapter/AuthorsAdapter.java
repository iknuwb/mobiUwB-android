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
 * Jest to adapter listy z autorami.
 */
public class AuthorsAdapter extends ArrayAdapter<String>
{
    /**
     * Inicjuje zmienne wywołując bazowy konstruktor.
     * @param context Kontekst widoku bądź aplikacji.
     * @param textViewResourceId ID kontrolki TextView
     * @param objects Obiekty do zwizualizowania w Adapterze.
     */
    public AuthorsAdapter(Context context, int textViewResourceId,
                          List<String> objects)
    {
        super(context, textViewResourceId, objects);
    }

    /**
     * Wydarza się, gdy system zechce utworzyć pojedynczy element
     * kontrolki ListView.
     * Ta funkcja służy za twórcę elementu prezentującego Autora bazując
     * na skonfigurowanym przez programistę modelu kontrolki.
     * @param position Który element z listy jest aktualnie tworzony.
     * @param convertView Kontrolka listy.
     * @param parent Rodzic kontrolki.
     * @return Utworzona kontrolka, wypełniona.
     */
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
