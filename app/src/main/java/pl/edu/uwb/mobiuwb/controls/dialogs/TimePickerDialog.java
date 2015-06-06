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
 * Created by sennajavie on 2015-04-30.
 */
public class TimePickerDialog extends DialogCreator<Date>
{

    @Override
    public DialogFragment create()
    {
        DialogFragment timePicker = new TimePickerFragment(model, valueChange);
        return timePicker;
    }

    public static class TimePickerFragment extends DialogFragment
            implements android.app.TimePickerDialog.OnTimeSetListener
    {
        private DialogItemModel<Date> model;
        OnValueChangedListener timeSetListener;

        public TimePickerFragment()
        {
        }

        public TimePickerFragment(DialogItemModel<Date> model,
                                  OnValueChangedListener timeSetListener)
        {
            this.model = model;
            this.timeSetListener = timeSetListener;
        }

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
