package pl.edu.uwb.mobiuwb.view.settings.adapter.items.checkbox;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;

/**
 * Jest to element ekranu Ustawień/Opcji który zawiera w sobie
 * Checkboxa.
 */
public class CheckBoxItem
        extends Item<CheckBoxItemModel>
        implements CompoundButton.OnCheckedChangeListener
{

    /**
     * Inicjuje pola.
     * @param model Model tego elementu.
     */
    public CheckBoxItem(CheckBoxItemModel model)
    {
        super(model);
    }

    /**
     * Wywołuje się gdy Android chce skonfigurować ten view.
     * Korzysta z wzorca ViewHolder aby Android nie musiał co chwila tworzyć
     * tych widoków.
     * @param groupPosition Pozycja w grupie.
     * @param convertView Widok.
     * @param parent Rodzic.
     */
    @Override
    protected void configureGroupView(int groupPosition, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.CheckBox);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.TextView);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(model.getText());
        viewHolder.checkbox.setChecked(model.isChecked());
        viewHolder.checkbox.setOnCheckedChangeListener(this);
    }

    /**
     * Dzieje się gdy zmienimy wartość w checkboxie.
     * @param compoundButton Przycisk checkbox.
     * @param checked Czy zaznaczono wartośc w checkboxie.
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
    {
        model.setChecked(checked);
    }


    /**
     * Klasa odpowiedzialna za wzorzec przechowywania widoku.
     * Dzięki temu nie jest on ciągle re-generowany.
     */
    private class ViewHolder
    {
        /**
         * Przechowuje kontrolkę checkboxa.
         */
        public CheckBox checkbox;

        /**
         * Przechowuje kontrolkę wyświetlającą tekst.
         */
        public TextView textView;

    }
}


