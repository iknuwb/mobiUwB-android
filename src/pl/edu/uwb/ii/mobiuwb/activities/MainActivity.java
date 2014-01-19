package pl.edu.uwb.ii.mobiuwb.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import pl.edu.uwb.ii.mobiuwb.GlobalVariables;
import pl.edu.uwb.ii.mobiuwb.LocalData;
import pl.edu.uwb.ii.mobiuwb.R;
import pl.edu.uwb.ii.mobiuwb.models.NotyficationsSettingsModel;
import pl.edu.uwb.ii.mobiuwb.models.WebsiteModel;
import pl.edu.uwb.ii.mobiuwb.notyfications.NotificationsService;
import pl.edu.uwb.ii.mobiuwb.parsers.XMLParser;
import pl.za.sennajavie.FreeInternetCheckerTask;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends ActionBarActivity
{
	LocalData localData;
	FreeInternetCheckerTask ict;
	private static long back_pressed;
	
	/**
	 * Jest to zmienna odpowiadająca za aktualnie aktywny model strony WWW.
	 */
	public static WebsiteModel webMod;
	/**
	 * Jest to zmienna odpowiadająca za listę stron WWW w programie.
	 */
	public static ArrayList<WebsiteModel> webSites = new ArrayList<WebsiteModel>();
	/**
	 * Jest to zmienna odpowiadająca za aktualną instancję kontrolki WebView,
	 * służącej do wyświetlania strony WWW w aplikacji.
	 */
	WebView current;
	
	/**
	 * Jest to zmienna odpowiadająca za aktualnąinstancję kontrolki Button,
	 * służącej do powrotu do strony głównej kontroli WebView.
	 */
	Button goBackButton;
	
	/**
	 * Jest to zmienna odpowiadająca za aktualną instancję kontrolki Button
	 * służącej do odświeżenia obecnej strony www.
	 */
	Button refreshButton;
	
	boolean isInternet;
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// super.onSaveInstanceState(outState);
		current.saveState(outState);
	}
	
	ProgressDialog al;
	
	
	/**
	 * Jest to główna metoda tego okna Activity. Odpowiada ona za utworzenie go
	 * i zainicjalizowanie elementów.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		al = new ProgressDialog(MainActivity.this);
		
		al.setMessage("Ładuję ...");
		al.show();
		// pb = ProgressDialog.show(this.getBaseContext(), "", "Ładowanie...",
		// true);
		this.setTitle("MobiUwB");
		fillWebSiteList();
		if(!validateList())
		{
			Toast.makeText(
					this,
					"Jeden z linków jest niepoprawny. Proszę zedytować plik properties.xml.",
					Toast.LENGTH_LONG).show();
		}
		goBackButton = (Button)this.findViewById(R.id.goBackButton);
		refreshButton = (Button)this.findViewById(R.id.RefreshButton);
		
		refreshButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onRefreshButtonClick();
			}
		});
		
		goBackButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				onBackButtonClick();
			}
		});
		current = (WebView)findViewById(R.id.mainWebView);
		WebSettings currentSettings = current.getSettings();
		currentSettings.setJavaScriptEnabled(true);
		currentSettings.setAllowFileAccess(false);
		currentSettings.setNeedInitialFocus(false);
		currentSettings.setLoadWithOverviewMode(true);
		currentSettings.setUseWideViewPort(true);
		currentSettings.setBuiltInZoomControls(false);
		currentSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		
		current.setPersistentDrawingCache(WebView.PERSISTENT_ALL_CACHES);
		
		current.setWebViewClient(new WebViewClient()
		{
			private boolean pom = false;
			
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,
					String urlNewString)
			{
				// Toast.makeText(MainActivity.this, "restartuje url",
				// Toast.LENGTH_LONG).show();
				if(isPDF(urlNewString) && !pom)
				{
					urlNewString = "http://docs.google.com/gview?embedded=true&url="
							+ urlNewString;
					view.loadUrl(urlNewString);
					pom = true;
					
					return pom;
				}
				pom = false;
				return pom;
			}
			
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				
				super.onPageStarted(view, url, favicon);
				
			}
			
			
			@Override
			public void onPageFinished(WebView view, String url)
			{
				al.dismiss();
			}
		});
		
		if(savedInstanceState != null)
		{
			
			((WebView)findViewById(R.id.mainWebView)).restoreState(savedInstanceState);
			
		}
		else
		{
			try
			{
				URL startPage = new URL(webMod.getURL());
				current.loadUrl(startPage.toString());
			}
			catch(MalformedURLException e)
			{
				
				Toast.makeText(this,
						"Błędny URL. Kod błędu :" + e.getLocalizedMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
		localData = new LocalData(this.getSharedPreferences(GlobalVariables.LOCAL_STORAGE, Context.MODE_PRIVATE));
		
		restoreNotificationsSettings();
	}
	
	
	@Override
	protected void onDestroy()
	{
		storeNotificationsSettings();
		super.onDestroy();
	}
	
	
	/*
	 * private void addWebViewIfNotExists() { webViewLayout =
	 * (RelativeLayout)findViewById(R.id.mainWebViewRelativeLayout); if(current
	 * == null) {
	 * 
	 * } webViewLayout.addView(current); }
	 */
	
	@Override
	protected void onStart()
	{
		Context ctx = getApplicationContext();
		ict = new AsyncInternetChecker(ctx);
		ict.execute(Long.valueOf(1000));
		NotificationsService.MAKE_CLOSE = true;
		super.onStart();
	}
	
	
	@Override
	protected void onStop()
	{
		Context ctx = getApplicationContext();
		al.dismiss();
		ict.cancel(true);
		NotificationsService.MAKE_CLOSE = false;
		SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
		localData.zapiszDanaLokalna(GlobalVariables.LOCAL_STORE_LAST_VISIT_DATE, sdf.format(Calendar.getInstance().getTime()));
		
		Intent i = new Intent(ctx, NotificationsService.class);
		ctx.startService(i);
		super.onStop();
	}
	
	
	/**
	 * Metoda ta wywołuje się, gdy użytkownik kliknie przycisk "Strona Główna".
	 */
	private void onBackButtonClick()
	{
		// current.loadUrl(webMod.getURL());
		current.goBack();
	}
	
	
	/**
	 * Metoda ta służy do odświeżania obecnie otwartej strony Wół Wół Wół :)
	 */
	private void onRefreshButtonClick()
	{
		String url = current.getUrl();
		current.loadUrl(url);
	}
	
	
	/**
	 * Metoda ta wywołuje się, gdy użytkownik powraca do tego Activity. Nadaje
	 * ona nowy URL dla kontrolki WebView.
	 */
	@Override
	protected void onResume()
	{
		// current.loadUrl(webMod.getURL());
		super.onResume();
	}
	
	
	/**
	 * Jest to metoda tworząca menu w tym Activity.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	/**
	 * Jest to zdarzenie, które pochłania standardowe menu danej strony WWW, aby
	 * zawsze było aktywne custom menu aplikacji.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_MENU)
		{
			this.openOptionsMenu();
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			String URL = current.getUrl();
			if(URL.equals(webMod.getPingURL()) || URL.equals(webMod.getURL()))
			{
				if(back_pressed + 2000 > System.currentTimeMillis())
				{
					finish();
				}
				else
				{
					Toast.makeText(getBaseContext(),
							"Wcisnij ponownie aby wyjść!", Toast.LENGTH_SHORT)
							.show();
				}
				back_pressed = System.currentTimeMillis();
				return true;
			}
			return false;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	
	/**
	 * Jest to zdarzenie odpowiedzialne za kliknięcie na określoną opcję menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_services:
			{
				Intent i = new Intent(this, WebsiteChooseActivity.class);
				this.startActivity(i);
				return true;
			}
			case R.id.action_exit:
			{
				super.onBackPressed();
				return true;
			}
			case R.id.action_notifications_config:
			{
				Intent i = new Intent(this, NotificationsSettingsActivity.class);
				this.startActivity(i);
				return true;
				
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void restoreNotificationsSettings()
	{
		boolean onOf = Boolean.parseBoolean(localData.pobierzDanaLokalna(GlobalVariables.LOCAL_STORE_NOTIFICATION_SETTINGS_ON_OF_CHECKBOX, "true"));
		TypeConnectionCheck tcc = TypeConnectionCheck.valueOf(localData.pobierzDanaLokalna(GlobalVariables.LOCAL_STORE_NOTIFICATION_SETTINGS_CONNECTION, "WIFI"));
		Long time = Long.valueOf(localData.pobierzDanaLokalna(GlobalVariables.LOCAL_STORE_NOTIFICATION_SETTINGS_TIME_INTERVAL_SPINNER, Long.valueOf(60000).toString()));
		
		NotificationsSettingsActivity.nsm = new NotyficationsSettingsModel();
		NotificationsSettingsActivity.nsm.setTypeConnectionChech(tcc);
		NotificationsSettingsActivity.nsm.setNotificationTurnedOn(onOf);
		NotificationsSettingsActivity.nsm.setNotificationTimeInterwal(time);
	}
	
	
	private void storeNotificationsSettings()
	{
		localData.zapiszDanaLokalna(GlobalVariables.LOCAL_STORE_NOTIFICATION_SETTINGS_ON_OF_CHECKBOX, NotificationsSettingsActivity.nsm.isNotificationTurnedOn() + "");
		localData.zapiszDanaLokalna(GlobalVariables.LOCAL_STORE_NOTIFICATION_SETTINGS_CONNECTION, NotificationsSettingsActivity.nsm.getTypeConnectionChech().toString());
		localData.zapiszDanaLokalna(GlobalVariables.LOCAL_STORE_NOTIFICATION_SETTINGS_TIME_INTERVAL_SPINNER, NotificationsSettingsActivity.nsm.getNotificationTimeInterwal().toString());
	}
	
	
	/**
	 * Jest to metoda odpowiedzialna za wypełnianie listy stron WWW w programie.
	 */
	private void fillWebSiteList()
	{
		webSites.clear();
		String content = "";
		AssetManager am = this.getAssets();
		try
		{
			InputStream is = (InputStream)am.open("properties.xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String x = null;
			x = br.readLine();
			while(x != null)
			{
				content += x;
				x = br.readLine();
			}
		}
		catch(IOException e)
		{
			// TODO
		}
		if(content == "")
		{
			Toast.makeText(this, "Błąd odczytu pliku.", Toast.LENGTH_LONG)
					.show();
		}
		else
		{
			XMLParser.currentXML = content;
			XMLParser.getWebsitesFromXML();
		}
	}
	
	
	/**
	 * Jest to metoda walidująca listę.
	 * 
	 * @return Jeżeli cała lista webSites jest zwalidowana pozytywnie, wynik
	 *         true. Jeżeli choć jeden element nie jest zwalidowany, wynik
	 *         false.
	 */
	private boolean validateList()
	{
		for(WebsiteModel item : webSites)
		{
			if(item.isValid() == false)
			{
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Jest to metoda sprawdzająca, czy dany URL prowadzi do PDF'a
	 */
	private boolean isPDF(String url)
	{
		if(url.endsWith(".pdf"))
		{
			return true;
		}
		return false;
	}
	
	private class AsyncInternetChecker extends FreeInternetCheckerTask
	{
		
		public AsyncInternetChecker(Context ctx)
		{
			super(ctx);
		}
		
		
		@Override
		protected void onProgressUpdate(Boolean... values)
		{
			if(values[0] == true)
			{
				Toast.makeText(MainActivity.this, "Uzyskano połączenie z internetem", Toast.LENGTH_LONG)
						.show();
			}
			else
			{
				Toast.makeText(MainActivity.this, "Utracono połączenie z internetem", Toast.LENGTH_LONG)
						.show();
			}
			super.onProgressUpdate(values);
		}
		
	}
}
