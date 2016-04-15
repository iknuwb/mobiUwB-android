package pl.edu.uwb.mobiuwb.controls.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;

import java.util.Calendar;
import java.util.Date;

/**
 * {@inheritDoc}
 * Ten twórca dialogów tworzy dialog z możliwością wyboru daty.
 */
public class TimePickerDialog extends DialogCreator<Date>
{

    /**
     * {@inheritDoc}
     */
    @Override
    public DialogFragment create()
    {
        DialogFragment timePicker = new TimePickerFragment(model, valueChange);
        return timePicker;
    }

    /**
     * Fragment ten odpowiedzialny jest za utworzenie okienka z wyborem daty.
     */
    public static class TimePickerFragment extends DialogFragment
            implements android.app.TimePickerDialog.OnTimeSetListener
    {
        /**
         * Reprezentuje instancję modelu w ekranie Opcji, który to jest
         * odpowiedzialny za funkcjonalność wyświetlenia okienka z wyborem daty.
         */
        private DialogItemModel<Date> model;

        /**
         * Wydarza się, gdy wybierzemy datę w okienku.
         */
        OnValueChangedListener timeSetListener;

        /**
         * Tworzy instancję.
         * Jest wymagany przez Fragment.
         */
        public TimePickerFragment()
        {
        }

        /**
         * Tworzy instancję, nadaje pola w klasie.
         * @param model Reprezentuje instancję modelu w ekranie Opcji,
         *              który to jest odpowiedzialny za funkcjonalność
         *              wyświetlenia okienka z wyborem daty.
         * @param timeSetListener Nasłuchiwacz na nadanie daty w okienku.
         */
        public TimePickerFragment(DialogItemModel<Date> model,
                                  OnValueChangedListener timeSetListener)
        {
            this.model = model;
            this.timeSetListener = timeSetListener;
        }

        /**
         * Wydarza się gdy następuje tworzenie tego fragmentu okienka.
         * Nadaje domyślną datę z modelu.
         * Nadaje tytuł.
         * Nadaje format daty.
         * Nadaje wydarzenie, które wydarza się, gdy zmienimy czas.
         * @param savedInstanceState Zapisany stan tej kontrolki,
         *                           na wypadek re-kreacji.
         * @return Nowo utworzony dialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Use the current time as the default values for the picker
            final Calendar calendar = Calendar.getInstance();
            int hour = model.getValue().getHours();
            int minute = model.getValue().getMinutes();
            // Create a new instance of TimePickerDialog and return it
            Dialog dialog = new android.app.TimePickerDialog(
                    getActivity(),
                    this,
                    hour,
                    minute,
                    DateFormat.is24HourFormat(getActivity()));
            dialog.setTitle(model.getTitle());
            return dialog;
        }

        /**
         * Wydarza się, gdy zmienimy czas w niniejsyzm okienku.
         * Nadaje wartości oraz uruchamia wydarzenie
         * o zmianie wartości w okienku.
         * @param view Kontrolka do nadawania daty.
         * @param hourOfDay Nowo nadana godzina.
         * @param minute Nowo nadana minuta.
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            if (timeSetListener != null)
            {
                Date oldValue = model.getValue();
                Date newValue = new Date();
                newValue.setMinutes(minute);
                newValue.setHours(hourOfDay);

                timeSetListener.notifyChanged(oldValue, newValue);
            }
        }
    }
}
