package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.sharedPreferences;

import android.content.Context;
import android.content.res.Resources;

import java.util.List;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.DataInitializeTaskOutput;
import pl.edu.uwb.mobiuwb.tasks.Task;
import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;
import pl.edu.uwb.mobiuwb.view.settings.SettingsPreferencesManager;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.checkbox.CheckBoxItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.itempicker.ListPickerItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.timepicker.TimePickerItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.expandableonoff.ExpandableOnOffItemModel;

/**
 * {@inheritDoc}
 * Zawarte jest tutaj zadanie związane z Preferencjami ekranu Ustawień/Opcji.
 * Polega ono na pobraniu wszelkich informacji
 * na temat opcji w ekranie Ustawień/Opcji.
 */
public class SettingsPreferenceManagerTask implements Task<DataInitializeTaskOutput>
{
    /**
     * Tekst ustawienia notyfikacji.
     */
    public String notificationText;

    /**
     * Tekst ustawienia zakresu czasu.
     */
    public String timeRangeText;

    /**
     * Tekst ustawienia częstotliwości powiadomień.
     */
    public String frequencyText;

    /**
     * Tekst ustawienia czasu "od".
     */
    public String fromText;

    /**
     * Tekst ustawienia czasu "do".
     */
    public String toText;

    /**
     * {@inheritDoc}
     * Ta logika jest odpowiedzialna za przeszukanie wszystkich opcji w ekranie Opcji/Ustawień
     * i wydobycie z nich wszelkich informacji, takich jak czasy, zaznaczenie checkboxa i innych.
     * Modele tych opcji są pozyskiwane z Preferencji ekranu Ustawień/Opcji.
     */
    @Override public void execute(TaskInput input, DataInitializeTaskOutput output)
    {
        SettingsPreferenceManagerTaskInput settingsPreferenceManagerTaskInput =
                (SettingsPreferenceManagerTaskInput)input;
        Context baseContext = settingsPreferenceManagerTaskInput.getBaseContext();
        Resources baseContextResources = baseContext.getResources();


        SettingsPreferencesManager settingsPreferencesManager =
                SettingsPreferencesManager.getInstance(
                        baseContext);

        List<ItemModel> itemModels = settingsPreferencesManager.restore(output.configXmlResult);



        notificationText = baseContextResources.getString(
                R.string.settings_category_annoucements);
        timeRangeText = baseContextResources.getString(R.string.settings_subcategory_time_range);
        frequencyText = baseContextResources.getString(R.string.settings_subcategory_frequency);
        fromText = baseContextResources.getString(
                R.string.settings_subsubcategory_from);
        toText = baseContextResources.getString(
                R.string.settings_subsubcategory_to);

        try
        {
            recursiveFindModel(itemModels, output);
            int[] array = baseContextResources.getIntArray(
                    R.array.frequencies_values);
            output.interval = array[output.intervalIndex];
        }
        catch (Exception e)
        {
            output.addError(e.getMessage());
        }
    }



    private void recursiveFindModel(List<ItemModel> itemModels,
                                    DataInitializeTaskOutput output)
            throws Exception
    {
        for(ItemModel itemModel : itemModels)
        {
            if(itemModel instanceof CheckBoxItemModel)
            {
                CheckBoxItemModel checkBoxItemModel = (CheckBoxItemModel)itemModel;

                String key = checkBoxItemModel.getId();
                Boolean selected = checkBoxItemModel.isChecked();
                if(!output.categories.containsKey(key))
                {
                    output.categories.put(key, selected);
                }
                else
                {
                    throw new IllegalArgumentException("Unhandled CheckBoxItemModel");
                }
            }
            else if(itemModel instanceof ExpandableOnOffItemModel)
            {
                ExpandableOnOffItemModel expandableOnOffItemModel = (ExpandableOnOffItemModel)itemModel;

                if(expandableOnOffItemModel.getText().equals(notificationText))
                {
                    output.isNotificationActive = expandableOnOffItemModel.isExpanded();
                }
                else if(expandableOnOffItemModel.getText().equals(timeRangeText))
                {
                    output.isTimeRangeActive = expandableOnOffItemModel.isExpanded();
                }
                else
                {
                    throw new IllegalArgumentException("Unhandled ExpandableOnOffItemModel");
                }
            }
            else if(itemModel instanceof ListPickerItemModel)
            {
                ListPickerItemModel listPickerItemModel = (ListPickerItemModel)itemModel;

                if(listPickerItemModel.getText().equals(frequencyText))
                {
                    output.intervalIndex = listPickerItemModel.getValue();
                }
                else
                {
                    throw new IllegalArgumentException("Unhandled ListPickerItemModel");
                }
            }
            else if(itemModel instanceof TimePickerItemModel)
            {
                TimePickerItemModel timePickerItemModel = (TimePickerItemModel)itemModel;

                if(timePickerItemModel.getText().equals(fromText))
                {
                    output.timeRangeFrom = timePickerItemModel.getValue();
                }
                else if(timePickerItemModel.getText().equals(toText))
                {
                    output.timeRangeTo = timePickerItemModel.getValue();
                }
                else
                {
                    throw new IllegalArgumentException("Unhandled TimePickerItemModel");
                }
            }

            recursiveFindModel(itemModel.nestedModels,output);
        }
    }
}
