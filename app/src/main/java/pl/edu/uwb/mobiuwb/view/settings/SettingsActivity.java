package pl.edu.uwb.mobiuwb.view.settings;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ExpandableListView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.configuration.StartupConfig;
import pl.edu.uwb.mobiuwb.services.notification.NotificationService;
import pl.edu.uwb.mobiuwb.services.notification.ServiceManager;
import pl.edu.uwb.mobiuwb.view.settings.adapter.SettingsAdapter;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

import java.util.ArrayList;

public class SettingsActivity extends Activity
{
    private static final String SETTINGS_ITEMS_MODELS = "SettingsItemsModels";
    public static final String SHARED_PREFERENCES_NAME = "SettingsPreferences";
    public static FragmentManager settingsActivityFragmentManager;

    private ExpandableListView settingsActivityListView;

    private ArrayList<ItemModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsActivityFragmentManager = getFragmentManager();
        if (savedInstanceState != null)
        {
            data = savedInstanceState.getParcelableArrayList(SETTINGS_ITEMS_MODELS);
        }
        else
        {
            SettingsPreferencesManager settingsPreferencesManager =
                    SettingsPreferencesManager.getInstance(this);
            data = settingsPreferencesManager.restore(StartupConfig.configXmlResult);
        }
        InitializeControls();
    }



    @Override
    protected void onStop()
    {
        super.onStop();
        storeSettingsData();

        this.setResult(RESULT_OK);
        finish();
        ServiceManager.configureNotificationService(
                this,
                NotificationService.class,
                NotificationService.RELOAD_CONFIGURATION);
    }

    private void storeSettingsData()
    {
        SettingsPreferencesManager settingsPreferencesManager =
                SettingsPreferencesManager.getInstance(this);
        settingsPreferencesManager.store(data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SETTINGS_ITEMS_MODELS, data);
    }

    private void InitializeControls()
    {
        InitializeSettingsActivityListView();
    }

    private void InitializeSettingsActivityListView()
    {
        settingsActivityListView =
                (ExpandableListView) this.findViewById(R.id.SettingsActivityListView);
        SettingsAdapter settingsAdapter = new SettingsAdapter(data);
        settingsActivityListView.setAdapter(settingsAdapter);
    }

    @Override public void onBackPressed()
    {
        storeSettingsData();
        this.setResult(RESULT_OK);
        super.onBackPressed();
    }
}
