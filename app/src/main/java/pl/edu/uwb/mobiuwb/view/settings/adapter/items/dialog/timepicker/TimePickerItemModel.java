package pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.timepicker;

import pl.edu.uwb.mobiuwb.controls.dialogs.DialogCreator;
import pl.edu.uwb.mobiuwb.utillities.TimeConventers;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;

import java.util.Date;

/**
 * {@inheritDoc}
 * Tworzy model dialogu z doborem czasu.
 */
public class TimePickerItemModel extends DialogItemModel<Date>
{
    /**
     * Inicjuje pola za pomocą konstruktora bazowego.
     * @param dialogCreationStrategy Określona strategia tworzenia dialogów.
     */
    public TimePickerItemModel(
            DialogCreator dialogCreationStrategy)
    {
        super(dialogCreationStrategy);
    }

    /**
     * {@inheritDoc}
     */
    @Override public String prettyPrintValue(Date value)
    {
        return TimeConventers.getPrettyTime(value.getHours(), value.getMinutes());
    }

    /**
     * {@inheritDoc}
     */
    @Override protected int initLayout()
    {
        return pl.edu.uwb.mobiuwb.R.layout.settings_adapter_time_picker_item;
    }


}
