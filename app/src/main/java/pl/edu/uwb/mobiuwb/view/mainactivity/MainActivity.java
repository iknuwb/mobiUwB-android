package pl.edu.uwb.mobiuwb.view.mainactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.configuration.StartupConfig;
import pl.edu.uwb.mobiuwb.connection.CheckingType;
import pl.edu.uwb.mobiuwb.connection.InternetCheckingService;
import pl.edu.uwb.mobiuwb.connection.checker.MobileChecker;
import pl.edu.uwb.mobiuwb.connection.checker.WiFiChecker;
import pl.edu.uwb.mobiuwb.connection.interfaces.OnConnectionChangedListener;
import pl.edu.uwb.mobiuwb.services.notification.NotificationService;
import pl.edu.uwb.mobiuwb.services.notification.ServiceManager;
import pl.edu.uwb.mobiuwb.utillities.ActionBarMethods;
import pl.edu.uwb.mobiuwb.utillities.DrawerListMethods;
import pl.edu.uwb.mobiuwb.utillities.Globals;
import pl.edu.uwb.mobiuwb.utillities.MenuMethods;
import pl.edu.uwb.mobiuwb.view.adapter.DrawerListAdapter;
import pl.edu.uwb.mobiuwb.view.interfaces.GuiAccess;
import pl.edu.uwb.mobiuwb.view.mainactivity.fragment.MainActivityFragment;
import pl.edu.uwb.mobiuwb.view.model.DrawerListAdapterModel;
import pl.edu.uwb.mobiuwb.view.settings.SettingsPreferencesManager;
import pl.edu.uwb.mobiuwb.parsers.xml.model.Website;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Jest to główne okno aplikacji MobiUwB.
 */
public class MainActivity extends Activity implements GuiAccess
{
    /**
     * Kod zwrotny dla uruchomienia widoku Ustawień/Opcji.
     */
    public static final int SETTINGS_ACTIVITY_RESULT = 0;

    /**
     * Jest to usługa odpowiedzialna za sprawdzanie dostępności oraz rodzaju Internetu.
     */
    private InternetCheckingService internetCheckingService;

    /**
     * Pobiera usługę od sprawdzania Internetu.
     * @return Usługa od sprawdzania Internetu.
     */
    public InternetCheckingService getInternetCheckingService()
    {
        return internetCheckingService;
    }

    /**
     * Ten przycisk odpowiada za dostęp do wysuwanego bocznego menu.
     */
    public ActionBarDrawerToggle drawerToggle;

    /**
     * Jest to fragment dla tego widoku, zawierający sporą jego część.
     */
    private MainActivityFragment fragment;

    /**
     * Wygląd z wysuwanym bocznym menu.
     */
    private DrawerLayout drawerLayout;

    /**
     * Lista jednostek w wysuwanym bocznym menu.
     */
    private ListView drawerListView;

    /**
     * Kontener na listę jednostek w wysuwanym bocznym menu.
     */
    private LinearLayout drawerLinearLayout;

    /**
     * Aktualnie zaznaczona pozycja na liście wysuwanego bocznego menu.
     */
    private int currentSelectedPosition;

    /**
     * Czas na ponowne dotknięcie przycisku back, aby wyłączyć aplikację.
     */
    private long backPressedTime;

    /**
     * Dzieje się, gdy Android tworzy ten widok.
     * Zmusza Androida aby wyświetlił przycisk Menu.
     * Nadaje widok z XML oraz wywołuje funkcję inicjalizującą.
     * @param savedInstanceState Zapisany stan widoku na wypadek ponownego tworzenia go.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        forceShowMenuButton();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * Dzieje się, gdy system operacyjny pozbywa się tego widoku z pamięci urządzenia.
     * Zatrzymuje usługę sprawdzania Internetu.
     * Aktywuje usługę powiadomień.
     */
    @Override protected void onDestroy()
    {
        internetCheckingService.stopService();

        ServiceManager.configureNotificationService(
                this,
                NotificationService.class,
                NotificationService.START);
        super.onDestroy();
    }

    /**
     * Dzieje się, gdy już widok zostanie utworzony.
     * Zatrzymuje usługę notyfikacyjną.
     * @param savedInstanceState Zapisany stan widoku na wypadek ponownego tworzenia go.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        ServiceManager.configureNotificationService(
                this,
                NotificationService.class,
                NotificationService.STOP);
    }

    /**
     * Dzieje się gdy Android tworzy menu.
     * Tworzy menu z XML.
     * @param menu Menu.
     * @return Czy utworzono menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Dzieje się gdy dotkniemy jakiegoś elemenu menu.
     * Deleguje dotknięcie do klasy MenuMethods.
     * @param item Dotknięty element w menu.
     * @return Czy nadpisać tę akcję.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        MenuMethods.menuOptions(id, this);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dzieje się gdy Android zmieni konfigurację w trakcie działania
     * tego widoku.
     * @param newConfig Nowa konfiguracja.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Dzieje się gdy dotkniemy przycisku back.
     * Stopuje wyłaczenie aplikacji do czasu dwukliku back.
     * Stopuje wyłączenie aplikacji do czasu uzyskania pierwszej
     * możliwej strony WWW.
     */
    @Override
    public void onBackPressed()
    {
        String ping = StartupConfig.propertiesXmlResult.
                getXmlPropertiesRootElement().
                getDefaultWebsite().
                getPing();

        if (fragment.getCurrentUrl().equals(ping))
        {
            if (backPressedTime + 2000 > System.currentTimeMillis())
            {
                super.onBackPressed();
            }
            else
            {
                Toast.makeText(
                        getBaseContext(),
                        getResources().
                                getString(
                                        R.string.toast_press_again_to_close),
                        Toast.LENGTH_SHORT).
                        show();
            }
            backPressedTime = System.currentTimeMillis();
        }
        else
        {
            fragment.goBackInWebView();
        }
    }

    /**
     * Wydarza się gdy dotkniemy przycisku i nie zostanie on obsłużony
     * przez jakąkolwiek kontrolkę w tym widoku.
     * Obsługujemy tutaj przycisk back w podobny sposób, co
     *  w metodzie onBackPressed.
     * @param keyCode Kod dotkniętego przycisku.
     * @param event Wydarzenie dotknięcia przycisku.
     * @return Czy obsłużyć doktnięcie przycisku tutaj
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
            {
                String pingUrl = StartupConfig.propertiesXmlResult.
                        getXmlPropertiesRootElement().
                        getDefaultWebsite().
                        getPing();

                String currentUrl = fragment.getCurrentUrl();
                if (currentUrl.equals(pingUrl) == false)
                {
                    fragment.goBackInWebView();
                    return true;
                }
                if (backPressedTime + 2000 > System.currentTimeMillis())
                {
                    finish();
                }
                else
                {
                    Toast.makeText(
                            this,
                            getString(
                                    R.string.toast_press_again_to_close),
                            Toast.LENGTH_SHORT)
                            .show();
                }
                backPressedTime = System.currentTimeMillis();
                return true;
            }
            default:
            {
                return super.onKeyDown(keyCode, event);
            }
        }
    }

    /**
     * Ustanawia domyślne właściwości dla tego widoku.
     * W skład nich wchodzą tytuł widoku zależny od jednostki,
     * utworzenie fragmentu z przeglądarką.
     */
    private void setDefaults()
    {
        String title = StartupConfig.propertiesXmlResult.
                getXmlPropertiesRootElement().
                getDefaultWebsite().
                getName();

        ActionBarMethods.setActionBarTitle(this, title);


        fragment = new MainActivityFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_relativelayout, fragment)
                .commit();
    }

    /**
     * Inicjalizuje kontrolki tego widoku.
     */
    private void init()
    {
        initControls();
        initNavigationDrawer();
        setDefaults();
        initInternetChecker();
        readSettings();
    }

    /**
     * Inicjalizuje sprawdzacza stanu Internetu.
     * Nadaje wszystkie typy sprawdzanego Internetu.
     */
    private void initInternetChecker()
    {
        internetCheckingService = new InternetCheckingService();
        if(StartupConfig.isMobileAvailable())
        {
            internetCheckingService.setMobileInternetChecker(new MobileChecker());
        }
        if(StartupConfig.isWiFiAvailable())
        {
            internetCheckingService.setWifiInternetChecker(new WiFiChecker());
        }
        internetCheckingService.setOnConnectionLostListener(connectionLost);
        internetCheckingService.setOnConnectionRecivedListener(connectionReceived);
        internetCheckingService.execute();
    }

    /**
     * Reprezentuje nasłuchiwacz na utracone połączenie.
     * Raportuje o tym za pomocą powiadomień Toast.
     */
    OnConnectionChangedListener connectionLost = new OnConnectionChangedListener()
    {
        @Override public void connectionChanged(CheckingType checkingType)
        {
            Toast.makeText(MainActivity.this, getString(R.string.internet_checker_connection_lost), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Reprezentuje nasłuchiwacz na zmieniony typ połączenia.
     * Raportuje o tym za pomocą powiadomień Toast.
     * Odświeża przeglądarkę.
     */
    OnConnectionChangedListener connectionReceived = new OnConnectionChangedListener()
    {
        @Override public void connectionChanged(CheckingType checkingType)
        {
            switch(checkingType)
            {
                case WIFI:
                {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.internet_checker_wifi_active),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case MOBILE:
                {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.internet_checker_mobile_active),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            refreshWebView();
        }
    };

    /**
     * Inicjalizuje różne kontrolki związane z bocznym wysuwanym menu.
     */
    private void initControls()
    {
        drawerLayout = (DrawerLayout) findViewById(
                R.id.drawer_layout);
        drawerListView = (ListView) findViewById(
                R.id.drawer_listview);
        drawerLinearLayout = (LinearLayout) findViewById(
                R.id.drawer_linearlayout);
    }

    /**
     * Inicjalizuje boczne wysuwane menu nadając mu elementy
     * pobrane z listy z pliku Właściwości.
     * Ponadto nadaje wydarzenia związane z wyborem tych elementów.
     * Będzie odświeżać stronę WWW przy wyborze jakiegoś elementu.
     * Nadaje też domyślnie zaznaczony element.
     */
    private void initNavigationDrawer()
    {

        List<DrawerListAdapterModel> list = new ArrayList<DrawerListAdapterModel>();
        for (Website item : StartupConfig.propertiesXmlResult.getXmlPropertiesRootElement().websites)
        {
            list.add(new DrawerListAdapterModel(
                    getResources().getDrawable(R.drawable.logouwb),
                    item, 0));
        }

        currentSelectedPosition = StartupConfig.propertiesXmlResult.
                getXmlPropertiesRootElement().
                getDefaultWebsiteIndex();

        if (currentSelectedPosition == -1)
        {
            currentSelectedPosition = 0;
        }

        final DrawerListAdapter drawerListAdapter = new DrawerListAdapter(this, list);

        drawerListView.setAdapter(drawerListAdapter);
        drawerListView.setItemChecked(
                currentSelectedPosition,
                true);
        drawerListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id)
                    {
                        currentSelectedPosition = position;
                        if (drawerListView != null)
                        {
                            drawerListView.setItemChecked(position, true);
                        }
                        if (drawerLayout != null)
                        {
                            drawerLayout.closeDrawer(drawerLinearLayout);
                        }
                        if (drawerListView != null)
                        {
                            DrawerListAdapterModel mdl = (DrawerListAdapterModel)
                                    drawerListView.getAdapter().getItem(position);

                            //configXmlResult.universityUnits

                            //mdl.getWebsite()
                            try
                            {
                                StartupConfig.xmlParser.serializeDefaultWebsite(mdl.getWebsite());
                                StartupConfig.propertiesXmlResult.
                                        getXmlPropertiesRootElement().
                                        setDefaultWebsite(mdl.getWebsite());
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            catch (ParserConfigurationException e)
                            {
                                e.printStackTrace();
                            }
                            catch (SAXException e)
                            {
                                e.printStackTrace();
                            }
                            catch (TransformerException e)
                            {
                                e.printStackTrace();
                            }
                            DrawerListMethods.onItemSelected(mdl);

                            fragment.loadUrl(mdl.getWebsite().getUrl());
                            ActionBarMethods.setActionBarTitle(MainActivity.this, mdl.getText());
                        }
                    }
                });

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                                     GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * Metoda wymuszająca na androidzie aby wyświetlił on menu button.
     */
    private void forceShowMenuButton()
    {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null)
            {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception ex)
        {
            // Ignore
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishActivity()
    {
        this.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshWebView()
    {
        fragment.refreshWebView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startActivityAccess(Intent intent)
    {
        this.startActivity(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override public void startActivityForResultAccess(Intent intent, int requestCode)
    {
        this.startActivityForResult(intent, requestCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override public Context getContextAccess()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     * Czyta nowe ustawienia, jeżeli zgodzi się kod zwrotny dla
     * ekranu Ustawień/Opcji.
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e(Globals.MOBIUWB_TAG, "ResultCode: " + resultCode);
        Log.e(Globals.MOBIUWB_TAG, "RequestCode: " + requestCode);

        if(resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case SETTINGS_ACTIVITY_RESULT:
                {
                    readSettings();
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Czyta nowe ustawienia z ekranu Ustawień/Opcji.
     */
    private void readSettings()
    {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(SettingsPreferencesManager.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        int connectionTypeValue = sharedPreferences.getInt(
                SettingsPreferencesManager.SETTINGS_SHARED_CONNECTION_TYPE_CHOSEN_VALUE,
                0);
        CheckingType checkingType = CheckingType.values()[connectionTypeValue];
    }
}
