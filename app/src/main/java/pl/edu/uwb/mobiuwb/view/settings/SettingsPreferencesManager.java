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
 * Jest to zarządca preferencjami w ekranie Ustawień.
 */
public class SettingsPreferencesManager
{
    /**
     * Ogólny klucz umiejscowienia preferencji.
     */
    public static final String SHARED_PREFERENCES_NAME = "SettingsPreferences";

    /**
     * Klucz minuty OD.
     */
    public static final String SETTINGS_SHARED_FROM_MINS = "SSFM";

    /**
     * Klucz godziny OD.
     */
    public static final String SETTINGS_SHARED_FROM_HOURS = "SSFH";

    /**
     * Klucz minuty DO.
     */
    public static final String SETTINGS_SHARED_TO_MINS = "SSTM";

    /**
     * Klucz godziny DO.
     */
    public static final String SETTINGS_SHARED_TO_HOURS = "SSTH";

    /**
     * Klucz z wartością czy powiadomienia są aktywne.
     */
    public static final String SETTINGS_SHARED_ANNOUCEMENTS_ACTIVE = "SSAA";

    /**
     * Klucz z wartością czy zakres czasu jest aktywny.
     */
    public static final String SETTINGS_SHARED_ANNOUCEMENTS_TIME_RANGE_ACTIVE = "SSATRA";

    /**
     * Klucz z wartością odstępu między powiadomieniami.
     */
    public static final String SETTINGS_SHARED_FREQUENCY_CHOSEN_VALUE = "SSFCV";

    /**
     * Klucz z wartością określonego typu połączenia.
     */
    public static final String SETTINGS_SHARED_CONNECTION_TYPE_CHOSEN_VALUE = "SSCTCV";

    /**
     * Klucz z wartością czy checkbox jest zaznaczony.
     */
    public static final String SETTINGS_SHARED_CHECKBOX_CHECKED = "SSCC";


    /**
     * Preferencje,
     */
    private SharedPreferences preferences;

    /**
     * Kontekst widoku lub aplikacji.
     */
    private Context context;

    /**
     * Lista modeli z widoku Opcji/Ustawień.
     */
    private ArrayList<ItemModel> list;

    /**
     * Model kontrolki daty "od".
     */
    private DialogItemModel<Date> from;

    /**
     * Model kontrolki daty "do".
     */
    private DialogItemModel<Date> to;

    /**
     * Model kontrolki rozszerzającej modele-dzieci w liście
     * rozszerzalnej.
     */
    private ExpandableOnOffItemModel announcements;

    /**
     * Model odpowiadający za kontrolkę z doborem częstości odstępu od
     * powiadomień.
     */
    private ListPickerItemModel frequency;

    /**
     * Kontrolka, której zadanie obejmuje zwężanie/rozszerzanie podgrupy
     * związanej z zarządzaniem zakresu czasu powiadomień.
     */
    private ExpandableOnOffItemModel timeRange;

    /**
     * Kontrolka odpowiadająca za dobór częstości odstępu od
     * powiadomień.
     */
    private ListDialog frequencySimpleDialog;

    /**
     * Kontrolka odpowiadająca za dobór godziny "od" w zakresie godzin.
     */
    private TimePickerDialog fromTimePickerDialog;

    /**
     * Kontrolka odpowiadająca za dobór godziny "do" w zakresie godzin.
     */
    private TimePickerDialog toTimePickerDialog;

    /**
     * Jedyna instancja tej klasy.
     */
    private static SettingsPreferencesManager instance;

    /**
     * Pobiera jedyną instancję tej klasy.
     * @param context Kontekst aplikacji lub okna.
     * @return Jedyna instancja tej klasy.
     */
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

    /**
     * Konstruktor ten jest prywatny aby uniemożliwić tworzenie instancji tej klasy
     * innej niż tylko jedna.
     */
    private SettingsPreferencesManager()
    {
    }

    /**
     * Pobiera ID kontrolki odpowiedzialnej za checbkoxa bazując na jego indeksie.
     * @param index Indeks.
     * @return ID kontrolki odpowiedzialnej za checbkoxa.
     */
    public String getChecboxItemId(int index)
    {
        String key = SETTINGS_SHARED_CHECKBOX_CHECKED + "_" + index;
        return key;
    }

    /**
     * Zapisuje listę modeli z Ustawień/Opcji do pamięci urządzenia.
     * @param models Lista modeli.
     */
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

    /**
     * Odtwarza listę modeli z pamięci urządzenia.
     * @param configXmlResult Xml konfiguracji.
     * @return Odtworzona lista modeli z pamięci urządzenia.
     */
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

    /**
     * Tworzy model zarządzający datą "do".
     */
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


    /**
     * Tworzy model zarządzający datą "od".
     */
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

    /**
     * Tworzy model zarządzający zakresem dat.
     */
    private void createTimeRangeItemModel()
    {
        timeRange = new ExpandableOnOffItemModel();
        timeRange.setText(context.getString(R.string.settings_subcategory_time_range));
        timeRange.setExpanded(
                preferences.getBoolean(SETTINGS_SHARED_ANNOUCEMENTS_TIME_RANGE_ACTIVE, false));
        announcements.nestedModels.add(timeRange);
    }

    /**
     * Tworzy model zarządzający częstością wystąpień powiadomień.
     */
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

    /**
     * Tworzy model zarządzający rozwijaniem i zwijaniem elementów
     * odpowiadających za powiadomienia.
     */
    private void createAnnouncementsItemModel()
    {
        announcements = new ExpandableOnOffItemModel();
        announcements.setText(context.getString(R.string.settings_category_annoucements));
        announcements
                .setExpanded(preferences.getBoolean(SETTINGS_SHARED_ANNOUCEMENTS_ACTIVE, true));
        list.add(announcements);
    }

    /**
     * Wydarza się gdy zmienimy częstość występowania powiadomień.
     */
    OnValueChangedListener<Integer> onFrequencySimpleDialogValueChangeListener =
            new OnValueChangedListener<Integer>()
            {
                @Override public void notifyChanged(Integer oldValue, Integer newValue)
                {
                    frequency.setValue(newValue);
                }
            };

    /**
     * Wydarza się gdy zmienimy datę "od" w odpowiedniej kontrolce.
     */
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


    /**
     * Wydarza się gdy zmienimy datę "do" w odpowiedniej kontrolce.
     */
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

    /**
     * Dodaje kategorie do ustawień bazując na pliku konfiguracyjnym.
     * Dzięki tej funkcji każda kategoria może mieć włączone lub wyłączone
     * powiadomienia.
     * @param configXmlResult Zawiera plik XML konfiguracji.
     */
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

    /**
     * Tworzy pojedynczy element zajmujący się pojedynczą kategorią.
     * Ustala, czy należy otrzymywać powiadomienia z tej kategorii czy też nie.
     * @param section Sekcja/kategoria dla której tworzymy ten element.
     */
    private void createCategory(Section section)
    {
        CheckBoxItemModel checkBoxItemModel = new CheckBoxItemModel();
        checkBoxItemModel.setText(section.title);
        checkBoxItemModel.setId(section.id);
        announcements.nestedModels.add(checkBoxItemModel);
    }
}
