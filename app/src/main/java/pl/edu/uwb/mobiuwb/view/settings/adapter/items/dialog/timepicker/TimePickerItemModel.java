package pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.timepicker;

import pl.edu.uwb.mobiuwb.controls.dialogs.DialogCreator;
import pl.edu.uwb.mobiuwb.utillities.TimeConventers;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;

import java.util.Date;

/**
 * Created by sennajavie on 2015-05-03.
 */
public class TimePickerItemModel extends DialogItemModel<Date>
{
    public TimePickerItemModel(
            DialogCreator dialogCreationStrategy)
    {
        super(dialogCreationStrategy);
    }

    @Override public String prettyPrintValue(Date value)
    {
        return TimeConventers.getPrettyTime(value.getHours(), value.getMinutes());
    }

    @Override protected int initLayout()
    {
        return pl.edu.uwb.mobiuwb.R.layout.settings_adapter_time_picker_item;
    }


}
