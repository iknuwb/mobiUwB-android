package pl.edu.uwb.mobiuwb.view.settings;

import android.content.Context;
import android.content.SharedPreferences;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.configuration.StartupConfig;
import pl.edu.uwb.mobiuwb.controls.dialogs.ListDialog;
import pl.edu.uwb.mobiuwb.controls.dialogs.TimePickerDialog;
import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.checkbox.CheckBoxItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.itempicker.ListPickerItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.timepicker.TimePickerItemModel;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.expandableonoff.ExpandableOnOffItemModel;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.ConfigXmlResult;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.Section;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tunczyk on 2015-05-01.
 */
public class SettingsPreferencesManager
{
    public static final String SHARED_PREFERENCES_NAME = "SettingsPreferences";

    public static final String SETTINGS_SHARED_FROM_MINS = "SSFM";
    public static final String SETTINGS_SHARED_FROM_HOURS = "SSFH";

    public static final String SETTINGS_SHARED_TO_MINS = "SSTM";
    public static final String SETTINGS_SHARED_TO_HOURS = "SSTH";

    public static final String SETTINGS_SHARED_ANNOUCEMENTS_ACTIVE = "SSAA";
    public static final String SETTINGS_SHARED_ANNOUCEMENTS_TIME_RANGE_ACTIVE = "SSATRA";

    public static final String SETTINGS_SHARED_FREQUENCY_CHOSEN_VALUE = "SSFCV";
    public static final String SETTINGS_SHARED_CONNECTION_TYPE_CHOSEN_VALUE = "SSCTCV";

    public static final String SETTINGS_SHARED_CHECKBOX_CHECKED = "SSCC";


    private ConfigXmlResult configXmlResult;

    private SharedPreferences preferences;
    private Context context;

    private ArrayList<ItemModel> list;

    private DialogItemModel<Date> from;
    private DialogItemModel<Date> to;
    private ExpandableOnOffItemModel announcements;
    private ListPickerItemModel connectionType;
    private ListPickerItemModel frequency;
    private ExpandableOnOffItemModel timeRange;

    private ListDialog connectionTypeSimpleDialog;
    private ListDialog frequencySimpleDialog;
    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    private static SettingsPreferencesManager instance;
    public static SettingsPreferencesManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new SettingsPreferencesManager();
        }
        instance.context = context;
        instance.preferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return instance;
    }

    private SettingsPreferencesManager()
    {
    }

    public String getChecboxItemId(int index)
    {
        String key = SETTINGS_SHARED_CHECKBOX_CHECKED + "_" + index;
        return key;
    }

    public void store(List<ItemModel> models)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SETTINGS_SHARED_FROM_MINS, from.getValue().getMinutes());
        editor.putInt(SETTINGS_SHARED_FROM_HOURS, from.getValue().getHours());

        editor.putInt(SETTINGS_SHARED_TO_MINS, to.getValue().getMinutes());
        editor.putInt(SETTINGS_SHARED_TO_HOURS, to.getValue().getHours());

        editor.putInt(SETTINGS_SHARED_FREQUENCY_CHOSEN_VALUE, frequency.getValue());

        editor.putBoolean(SETTINGS_SHARED_ANNOUCEMENTS_ACTIVE, announcements.isExpanded());

        editor.putBoolean(SETTINGS_SHARED_ANNOUCEMENTS_TIME_RANGE_ACTIVE, timeRange.isExpanded());

        int iter = 0;
        for (int i = 0; i < announcements.nestedModels.size(); i++)
        {
            ItemModel model = announcements.nestedModels.get(i);
            if (model instanceof CheckBoxItemModel)
            {
                CheckBoxItemModel checkBoxItemModel = (CheckBoxItemModel) model;
                editor.putBoolean(
                        getChecboxItemId(iter),
                        checkBoxItemModel.isChecked());
                iter++;
            }
        }
        editor.apply();
    }

    public ArrayList<ItemModel> restore(ConfigXmlResult configXmlResult)
    {
        list = new ArrayList<ItemModel>();
        createAnnouncementsItemModel();
        createFrequencyItemModel();
        createTimeRangeItemModel();
        createTimePickerFromItemModel();
        createTimePickerToItemModel();
        addCategories(configXmlResult);

        return list;
    }

    private void createTimePickerToItemModel()
    {
        toTimePickerDialog = new TimePickerDialog();
        toTimePickerDialog.setOnValueChangedListener(onToTimePickerDialogValueChangeListener);
        to = new TimePickerItemModel(toTimePickerDialog);
        to.setText(context.getString(R.string.settings_subsubcategory_to));
        Date defaultDate = new Date();
        defaultDate.setMinutes(preferences.getInt(SETTINGS_SHARED_TO_MINS, 00));
        defaultDate.setHours(preferences.getInt(SETTINGS_SHARED_TO_HOURS, 18));
        to.setValue(defaultDate);
        timeRange.nestedModels.add(to);
    }

    private void createTimePickerFromItemModel()
    {
        fromTimePickerDialog = new TimePickerDialog();
        fromTimePickerDialog.setOnValueChangedListener(onFromTimePickerDialogValueChangeListener);
        from = new TimePickerItemModel(fromTimePickerDialog);
        from.setText(context.getString(R.string.settings_subsubcategory_from));
        Date defaultDate = new Date();
        defaultDate.setMinutes(preferences.getInt(SETTINGS_SHARED_FROM_MINS, 00));
        defaultDate.setHours(preferences.getInt(SETTINGS_SHARED_FROM_HOURS, 6));
        from.setValue(defaultDate);
        timeRange.nestedModels.add(from);
    }

    private void createTimeRangeItemModel()
    {
        timeRange = new ExpandableOnOffItemModel();
        timeRange.setText(context.getString(R.string.settings_subcategory_time_range));
        timeRange.setExpanded(
                preferences.getBoolean(SETTINGS_SHARED_ANNOUCEMENTS_TIME_RANGE_ACTIVE, false));
        announcements.nestedModels.add(timeRange);
    }

    private void createFrequencyItemModel()
    {
        String[] list = context.getResources().getStringArray(R.array.frequencies);

        frequencySimpleDialog = new ListDialog(
                new FrequencyListAdapter(context,list));
        frequencySimpleDialog.setOnValueChangedListener(
                onFrequencySimpleDialogValueChangeListener);
        frequency = new ListPickerItemModel(frequencySimpleDialog);
        frequency.setValue(preferences.getInt(SETTINGS_SHARED_FREQUENCY_CHOSEN_VALUE, 0));
        frequency.setText(context.getString(R.string.settings_subcategory_frequency));
        frequency.setTitle(context.getString(R.string.settings_subcategory_frequency));
        announcements.nestedModels.add(frequency);
    }

    private void createAnnouncementsItemModel()
    {
        announcements = new ExpandableOnOffItemModel();
        announcements.setText(context.getString(R.string.settings_category_annoucements));
        announcements
                .setExpanded(preferences.getBoolean(SETTINGS_SHARED_ANNOUCEMENTS_ACTIVE, true));
        list.add(announcements);
    }

    OnValueChangedListener<Integer> onConnectionSimpleDialogValueChangeListener =
            new OnValueChangedListener<Integer>()
            {
                @Override public void notifyChanged(Integer oldValue, Integer newValue)
                {
                    connectionType.setValue(newValue);
                }
            };

    OnValueChangedListener<Integer> onFrequencySimpleDialogValueChangeListener =
            new OnValueChangedListener<Integer>()
            {
                @Override public void notifyChanged(Integer oldValue, Integer newValue)
                {
                    frequency.setValue(newValue);
                }
            };

    OnValueChangedListener<Date> onFromTimePickerDialogValueChangeListener =
            new OnValueChangedListener<Date>()
            {
                @Override public void notifyChanged(Date oldValue, Date newValue)
                {
                    if (SettingsValidator.validateTime(newValue, to.getValue()))
                    {
                        from.setValue(newValue);
                    }
                }
            };

    OnValueChangedListener<Date> onToTimePickerDialogValueChangeListener =
            new OnValueChangedListener<Date>()
            {
                @Override public void notifyChanged(Date oldValue, Date newValue)
                {
                    if (SettingsValidator.validateTime(from.getValue(), newValue))
                    {
                        to.setValue(newValue);
                    }
                }
            };

    private void addCategories(ConfigXmlResult configXmlResult)
    {
        for (Section section : configXmlResult.getCurrentUniversityUnit().getSections())
        {
            createCategory(section);
        }

        int iter = 0;
        for (int i = 0; i < announcements.nestedModels.size(); i++)
        {
            ItemModel model = announcements.nestedModels.get(i);
            if (model instanceof CheckBoxItemModel)
            {
                CheckBoxItemModel checkBoxItemModel = (CheckBoxItemModel) model;
                checkBoxItemModel.setChecked(
                        preferences.getBoolean(
                                getChecboxItemId(iter),
                                true));
                iter++;
            }
        }
    }

    private void createCategory(Section section)
    {
        CheckBoxItemModel checkBoxItemModel = new CheckBoxItemModel();
        checkBoxItemModel.setText(section.title);
        checkBoxItemModel.setId(section.id);
        announcements.nestedModels.add(checkBoxItemModel);
    }
}
