package pl.edu.uwb.mobiuwb.view.mainactivity.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.configuration.StartupConfig;
import pl.edu.uwb.mobiuwb.view.interfaces.GuiAccess;
import pl.edu.uwb.mobiuwb.view.mainactivity.MainActivity;

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
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.main_content_fragment, container, false);
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        MainActivity mainActivity = (MainActivity) getActivity();
        mobiUwbWebViewClient = new MobiUwBWebViewClient();

        mainWebView = (WebView) getView().findViewById(
                R.id.main_webview);


        mainWebView.setBackgroundColor(0);
        mainWebView.setWebViewClient(mobiUwbWebViewClient);
        mainWebView.getSettings().setJavaScriptEnabled(true);
        String url = StartupConfig.
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

    @Override
    public void startActivityAccess(Intent intent)
    {
        this.getActivity().startActivity(intent);
    }

    @Override public void startActivityForResultAccess(Intent intent, int RequestCode)
    {
        startActivityForResult(intent, RequestCode);
    }

    @Override public Context getContextAccess()
    {
        return getActivity();
    }

}
