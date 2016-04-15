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

/**
 * Jest to fragment głównego widoku w aplikacji.
 */
public class MainActivityFragment extends Fragment implements GuiAccess
{
    /**
     * Główna przeglądarka aplikacji.
     */
    private WebView mainWebView;

    /**
     * Klient przeglądarki w aplikacji.
     * Posiada logikę zachowania przeglądarki.
     */
    private MobiUwBWebViewClient mobiUwbWebViewClient;

    /**
     * Tworzy niniejszy fragment i stwierdza, że powinien on trzymać
     * swą instancję.
     */
    public MainActivityFragment()
    {
        setRetainInstance(true);
    }

    /**
     * Dzieje się gdy Android tworzy widok tego fragmentu.
     * @param inflater Nadmuchiwacz, czyli narzędzie do tworzenia layoutu z XML.
     * @param container Kontener.
     * @param savedInstanceState Zapisane informacje widoku na wypadek ponownego
     *                           tworzenia go.
     * @return Utworzony widok reprezentowany przez fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.main_content_fragment, container, false);
    }


    /**
     * Dzieje się, gdy widok tego fragmentu już zostanie utworzony.
     * Pobiera oraz konfiguruje główną przeglądarkę aplikacji.
     * @param view Widok.
     * @param savedInstanceState Zapisane informacje widoku na wypadek ponownego
     *                           tworzenia go.
     */
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

    /**
     * Wysyła ciastko do serwisu WWW. Oznacza ono, że serwis wie już,
     * że ma do czynienia z Androidem.
     * @param page Strona WWW do której trzeba nadać nowe ciastko.
     */
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

    /**
     * Pobiera aktualny URL do którego w tym momencie odnosi się
     * przeglądarka.
     * @return Aktualny URL przeglądarki.
     */
    public String getCurrentUrl()
    {
        return mainWebView.getUrl();
    }

    /**
     * Wykonuje akcję cofania w przeglądarce do poprzedniej strony WWW.
     */
    public void goBackInWebView()
    {
        mainWebView.goBack();
    }

    /**
     * ładuje link w przeglądarce.
     * @param url Link do załadowania.
     */
    public void loadUrl(String url)
    {
        mainWebView.loadUrl(url);
    }

    /**
     * Wykonuje się gdy Android stopuje ten widok.
     * Wykonuje akcję ukrycia dialoga odpowiedzialnego za pokazanie
     * progresu jaki zaszedł w ładowaniu strony WWW.
     */
    @Override
    public void onStop()
    {
        mobiUwbWebViewClient.dismissProgressDialog();
        super.onStop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishActivity()
    {
        this.getActivity().finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshWebView()
    {
        mainWebView.reload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startActivityAccess(Intent intent)
    {
        this.getActivity().startActivity(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override public void startActivityForResultAccess(Intent intent, int RequestCode)
    {
        startActivityForResult(intent, RequestCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override public Context getContextAccess()
    {
        return getActivity();
    }

}
