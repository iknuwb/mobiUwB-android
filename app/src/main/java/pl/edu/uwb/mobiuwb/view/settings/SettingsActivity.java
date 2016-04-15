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

/**
 * Jest to widok odpowiedzialny za Ustawienia/Opcje w programie.
 */
public class SettingsActivity extends Activity
{
    /**
     * Klucz, pod którym zapisane są modele opcji.
     */
    private static final String SETTINGS_ITEMS_MODELS = "SettingsItemsModels";

    /**
     * Zarządzacz fragmentami.
     */
    public static FragmentManager settingsActivityFragmentManager;

    /**
     * ListView z zagnieżdżonymi rekurencyjnie grupami widoków.
     */
    private ExpandableListView settingsActivityListView;

    /**
     * Modele zawarte w zagnieżdżającym się ListView.
     */
    private ArrayList<ItemModel> data;

    /**
     * Dzieje się gdy tworzymy ten widok.
     * Nadaje wygląd z XML.
     * Inicjuje zarządzacza fragmentami.
     *
     * Inicjuje kontrolki.
     * @param savedInstanceState Zapisany stan widoku,
     *                           na wypadek re-generacji.
     */
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

    /**
     * Wydarza się, gdy Android stopuje ten widok.
     * Zapisuje dane ustawień.
     * Nadaje wynik tego okna na OK.
     * Wyłącza okno.
     * Ponownie startuje usługę powiadomień.
     */
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

    /**
     * Zapisuje do pamięci telefonu listę elementów obecnych w opcjach.
     */
    private void storeSettingsData()
    {
        SettingsPreferencesManager settingsPreferencesManager =
                SettingsPreferencesManager.getInstance(this);
        settingsPreferencesManager.store(data);
    }

    /**
     * Wydarza się, gdy Android zapisuje status tego widoku.
     * Dorzuca do paczki zapisującej elementy z listy zagnieżdżanej.
     * @param outState Paczka do zapisu statusu widoku.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SETTINGS_ITEMS_MODELS, data);
    }

    /**
     * Inicjalizuje kontrolki.
     */
    private void InitializeControls()
    {
        InitializeSettingsActivityListView();
    }

    /**
     * Inicjalizuje listę elementów zagnieżdżanych.
     * Nadaje jej adapterowi elementy.
     * Nadaje jej adapter z tymi elementami.
     */
    private void InitializeSettingsActivityListView()
    {
        settingsActivityListView =
                (ExpandableListView) this.findViewById(R.id.SettingsActivityListView);
        SettingsAdapter settingsAdapter = new SettingsAdapter(data);
        settingsActivityListView.setAdapter(settingsAdapter);
    }

    /**
     * Enkapsuluje w sobie logikę kliknięcia na przycisk cofnij.
     * Zapisuje w nim elementy listy rozwijanej w tym programie do pamięci tel.
     * Nadaje rezultat tego widoku na OK.
     */
    @Override public void onBackPressed()
    {
        storeSettingsData();
        this.setResult(RESULT_OK);
        super.onBackPressed();
    }
}
