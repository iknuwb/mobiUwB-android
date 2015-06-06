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

public class MainActivity extends Activity implements GuiAccess
{
    public static final int SETTINGS_ACTIVITY_RESULT = 0;


    private InternetCheckingService internetCheckingService;


    public InternetCheckingService getInternetCheckingService()
    {
        return internetCheckingService;
    }


    public ActionBarDrawerToggle drawerToggle;

    private MainActivityFragment fragment;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private LinearLayout drawerLinearLayout;
    private int currentSelectedPosition;
    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        forceShowMenuButton();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override protected void onDestroy()
    {
        internetCheckingService.stopService();

        ServiceManager.configureNotificationService(
                this,
                NotificationService.class,
                NotificationService.START);
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        ServiceManager.configureNotificationService(
                this,
                NotificationService.class,
                NotificationService.STOP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }


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

    private void init()
    {
        initControls();
        initNavigationDrawer();
        setDefaults();
        initInternetChecker();
        readSettings();
    }

    private void initInternetChecker()
    {
        internetCheckingService = new InternetCheckingService();
        //TODO sprawdzić czy istnieje w splash :)
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

    OnConnectionChangedListener connectionLost = new OnConnectionChangedListener()
    {
        @Override public void connectionChanged(CheckingType checkingType)
        {
            Toast.makeText(MainActivity.this, getString(R.string.internet_checker_connection_lost), Toast.LENGTH_SHORT).show();
        }
    };
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

    private void initControls()
    {
        drawerLayout = (DrawerLayout) findViewById(
                R.id.drawer_layout);
        drawerListView = (ListView) findViewById(
                R.id.drawer_listview);
        drawerLinearLayout = (LinearLayout) findViewById(
                R.id.drawer_linearlayout);
    }

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
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            catch (ParserConfigurationException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            catch (SAXException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            catch (TransformerException e)
                            {
                                // TODO Auto-generated catch block
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

        drawerLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
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

    @Override
    public void finishActivity()
    {
        this.finish();
    }

    @Override
    public void refreshWebView()
    {
        fragment.refreshWebView();
    }

    @Override
    public void startActivityAccess(Intent intent)
    {
        this.startActivity(intent);
    }

    @Override public void startActivityForResultAccess(Intent intent, int requestCode)
    {
        this.startActivityForResult(intent, requestCode);
    }

    @Override public Context getContextAccess()
    {
        return this;
    }

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
                    //TODO restore preferences
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
