package pl.edu.uwb.mobiuwb.controls.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListAdapter;

import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.itempicker.ListPickerItemModel;

/**
 * {@inheritDoc}
 * Ten twórca tworzy okienko z listą.
 */
public class ListDialog extends DialogCreator<Integer>
{

    /**
     * Jest to adapter kontrolki listy przechowujący jej elementy.
     */
    private ListAdapter listAdapter;

    /**
     * Pobiera adapter kontrolki listy, który przechowuje jej elementy.
     * @return Adapter kontrolki listy.
     */
    public ListAdapter getAdapter()
    {
        return listAdapter;
    }

    /**
     * Inicjuje zmienne.
     * @param listAdapter Adapter kontrolki listy
     */
    public ListDialog(ListAdapter listAdapter)
    {
        super();
        this.listAdapter = listAdapter;
    }

    /**
     * {@inheritDoc}
     */
    @Override public DialogFragment create()
    {
        DialogFragment dialogFragment =
                new SimpleDialogFragment((ListPickerItemModel) model, valueChange, listAdapter);
        return dialogFragment;
    }


    /**
     * Jest to klasa fragmentu tego okienka. Fragment ten tworzony jest
     * przez tą klasę, aby utworzyć jej okno.
     */
    public static class SimpleDialogFragment extends DialogFragment
            implements DialogInterface.OnClickListener
    {
        /**
         * Reprezentuje instancję modelu w ekranie Opcji, który to jest
         * odpowiedzialny za funkcjonalność wyświetlenia okienka z listą.
         */
        private ListPickerItemModel model;

        /**
         * Jest to adapter kontrolki listy przechowujący jej elementy.
         */
        private ListAdapter listAdapter;

        /**
         * Pobiera reprezentację instancji modelu w ekranie Opcji, który to jest
         * odpowiedzialny za funkcjonalność wyświetlenia okienka z listą.
         * @return Model reprezentujący element Opcji.
         */
        public ListPickerItemModel getModel()
        {
            return model;
        }

        /**
         * Nadaje reprezentację instancji modelu w ekranie Opcji, który to jest
         * odpowiedzialny za funkcjonalność wyświetlenia okienka z listą.
         * @param model Model reprezentujący element Opcji.
         */
        private void setModel(ListPickerItemModel model)
        {
            this.model = model;
        }

        /**
         * Wydarza się, gdy zostanie zmieniona wartość w okienku, czyli kiedy
         * user wybierze nową wartość z listy.
         */
        private OnValueChangedListener valueChange;

        /**
         * Tworzy obiekt.
         * Fragmenty wymagają takiego konstruktora.
         */
        public SimpleDialogFragment()
        {
        }

        /**
         * Tworzy obiekt i nadaje pola.
         * @param model Reprezentuje instancję modelu w ekranie Opcji,
         *              który to jest odpowiedzialny za funkcjonalność
         *              wyświetlenia okienka z listą.
         * @param valueChange Nasłuchiwacz na zmianę wartości.
         * @param listAdapter Jest to adapter kontrolki
         *                    listy przechowujący jej elementy.
         */
        public SimpleDialogFragment(ListPickerItemModel model,
                                    OnValueChangedListener valueChange,
                                    ListAdapter listAdapter)
        {
            this.model = model;
            this.valueChange = valueChange;
            this.listAdapter = listAdapter;
        }

        /**
         * Metoda ta wywołuje się, gdy tworzy się niniejszy fragment dialoga.
         * Nadaje dialogowi tytuł oraz adapter, oraz wydarzenie
         * dotknięcia opcji na dialogu.
         * @param savedInstanceState Zapisany stan tej kontrolki, w przypadku re-kreacji jej.
         * @return Nowy dialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(model.getTitle())
                    .setAdapter(listAdapter,this);
            return builder.create();
        }

        /**
         * Wywołuje się gdy dotkniemy opcji na dialogu.
         * Dodatkowo wywołuje wydarzenie zmiany wartości w dialogu.
         * @param dialog Dotknięty dialog.
         * @param which Która opcja została dotknięta.
         */
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if (valueChange != null)
            {
                valueChange.notifyChanged(model.getValue(), which);
            }
        }
    }
}
