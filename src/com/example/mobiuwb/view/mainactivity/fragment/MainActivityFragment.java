package com.example.mobiuwb.view.mainactivity.fragment;

import com.example.mobiuwb.MobiUwbApp;
import com.example.mobiuwb.R;
import com.example.mobiuwb.utillities.MenuMethods;
import com.example.mobiuwb.view.interfaces.GuiAccess;
import com.example.mobiuwb.view.mainactivity.MainActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivityFragment extends Fragment implements GuiAccess
{
	private WebView mainWebView;
	private MobiUwBWebViewClient mobiUwbWebViewClient;
	
	
	public MainActivityFragment()
	{
		setRetainInstance(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main_content_fragment, container, false);
		
		return rootView;
	}
	
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mobiUwbWebViewClient = new MobiUwBWebViewClient();
		
		mainWebView = (WebView)getView().findViewById(
				R.id.main_webview);
		
		
		mainWebView.setBackgroundColor(0);
		mainWebView.setWebViewClient(mobiUwbWebViewClient);
		mainWebView.getSettings().setJavaScriptEnabled(true);
		
		
		MainActivity mainActivity = (MainActivity)getActivity();
		String url = mainActivity.
				propertiesXmlResult.
				getXmlPropertiesRootElement().
				getDefaultWebsite().
				getUrl();

		syncCookie(url);
		mainWebView.loadUrl(url);
	}
	
	private void syncCookie(String page)
	{
		CookieSyncManager cookieSyncManager = 
				CookieSyncManager.createInstance(
						getView().getContext());
		CookieManager cookieManager = CookieManager.getInstance();
	
		cookieManager.setAcceptCookie(true);
		cookieManager.setCookie(page, "client=Android;");
		cookieSyncManager.sync();
	}
	
	public String getCurrentUrl()
	{
		return mainWebView.getUrl();
	}
	
	public void goBackInWebView()
	{
		mainWebView.goBack();
	}
	
	public void loadUrl(String url)
	{
		mainWebView.loadUrl(url);
	}
	
	@Override
	public void onStop()
	{
		mobiUwbWebViewClient.dismissProgressDialog();
		super.onStop();
	}

	@Override
	public void finishActivity()
	{
		this.getActivity().finish();
	}


	@Override
	public void refreshWebView()
	{
		mainWebView.reload();
	}
}
