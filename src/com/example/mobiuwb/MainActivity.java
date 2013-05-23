package com.example.mobiuwb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity 
{
	/**
	 * Jest to zmienna odpowiadaj¹ca za aktualnie aktywny model strony WWW.
	 */
	public static WebsiteModel webMod;
	/**
	 * Jest to zmienna odpowiadaj¹ca za listê stron WWW w programie.
	 */
	public static ArrayList<WebsiteModel> webSites = new ArrayList<WebsiteModel>();
	/**
	 * Jest to zmienna odpowiadaj¹ca za aktualn¹ instancjê kontrolki WebView, s³u¿¹cej do wyœwietlania strony WWW w aplikacji.
	 */
	WebView current;
	
	/**
	 * Jest to g³ówna metoda tego okna Activity. 
	 * Odpowiada ona za utworzenie go i zainicjalizowanie elementów.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("MobiUwB");
        fillWebSiteList();
        
        if(!validateList())
        {
        	Toast.makeText(this, "Jeden z linków jest niepoprawny. Proszê zedytowaæ plik properties.xml.", Toast.LENGTH_LONG).show();
        }
        current = (WebView)this.findViewById(R.id.mainWebView);
        WebSettings currentSettings = current.getSettings();
        currentSettings.setJavaScriptEnabled(true);
        currentSettings.setAllowFileAccess(false);
        currentSettings.setNeedInitialFocus(false);
        currentSettings.setLoadWithOverviewMode(true);
        currentSettings.setUseWideViewPort(true);
        currentSettings.setBuiltInZoomControls(false);
        currentSettings.setSupportZoom(false);
        current.setWebViewClient(new WebViewClient());
        WebView.enablePlatformNotifications();
        try
        {
			URL startPage = new URL(webMod.getURL());
	        current.loadUrl(startPage.toString());
		} 
        catch (MalformedURLException e) 
        {
        	Toast.makeText(this, "B³êdny URL. Kod b³êdu :" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}      
    }
    
    
    /**
     * Metoda ta wywo³uje siê, gdy u¿ytkownik powraca do tego Activity.
     * Nadaje ona nowy URL dla kontrolki WebView.
     */
    @Override
	protected void onResume() 
    {
		super.onResume();
		current.loadUrl(webMod.getURL());
	}

    /**
     * Jest to metoda tworz¹ca menu w tym Activity.
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
    {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Serwisy MobiUwB");
		return true;
	}

	/**
	 * Jest to zdarzenie, które poch³ania standardowe menu danej strony WWW, aby zawsze by³o aktywne custom menu aplikacji.
	 */
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) 
        {	
			this.openOptionsMenu();
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_BACK)
        {
        	if(current.getUrl().equals(webMod.getURL()))
        	{
        		finish();
        	}
    		return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }
    }
	
	/**
	 * Jest to zdarzenie odpowiedzialne za klikniêcie na okreœlon¹ opcjê menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId())
		{
			case 0:
			{
				Intent i = new Intent(this, WebsiteChooseActivity.class);				
				this.startActivity(i);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	 * Jest to metoda odpowiedzialna za wype³nianie listy stron WWW w programie.
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
    	catch (IOException e) 
		{
    		//TODO
		}
    	if(content=="")
    	{
        	Toast.makeText(this, "B³¹d odczytu pliku.", Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		XMLParser.currentXML = content;
    		XMLParser.getWebsitesFromXML();
    	}
    }
    
	/**
	 * Jest to metoda waliduj¹ca listê.
	 * @return je¿eli ca³a lista webSites jest zwalidowana pozytywnie, wynik true. Je¿eli choæ jeden element nie jest zwalidowany, wynik false.s
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
}
