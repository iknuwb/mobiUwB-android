package pl.edu.uwb.mobiuwb.view.mainactivity.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pl.edu.uwb.mobiuwb.R;

/**
 * Jest to klient przeglądarki, którego zadaniem jest nadanie
 * dla przegląarki odpowiednich zachowań w zależności od konkretnej akcji
 * aby dostosowała się ona do logiki aplikacji.
 */
public class MobiUwBWebViewClient extends WebViewClient
{
    /**
     * Jest to kontrolka prezentująca pasek postępu.
     */
    private ProgressDialog progressDialog;

    /**
     * Zmienna pomocnicza służąca do obsługi PDF w przypadku odnalezienia
     * takiego w wybieranym linku.
     */
    private boolean secondPdfTry;


    /**
     * Tworzy obiekt.
     */
    public MobiUwBWebViewClient()
    {
    }

    /**
     * Wydarza się gdy zaczynamy ładować stronę WWW.
     * Inicjalizuje dialog z paskiem postępu i sprawia, że on się pojawia.
     * @param view Przeglądarka.
     * @param url ładowany URL.
     * @param favicon Ikonka.
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        super.onPageStarted(view, url, favicon);
        initProgressDialog(view.getContext());
        progressDialog.show();
    }

    /**
     * Wydarza się gdy ładowanie strony się zakończyło.
     * Jeżeli dialog z postępem był pokazany, to znika.
     * @param view Przeglądarka.
     * @param url URL.
     */
    @Override
    public void onPageFinished(WebView view, String url)
    {
        super.onPageFinished(view, url);
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    /**
     * Sprawia, że znika okienko z postępem.
     */
    public void dismissProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    /**
     * Inicjalizuje okieno z postępem. Nadaje mu tytuł oraz fakt, że
     * użytkownik nie może go wyłączyć.
     * @param context Kontekst widoku lub aplikacji.
     */
    private void initProgressDialog(Context context)
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(
                    context,
                    AlertDialog.THEME_HOLO_DARK);

            progressDialog.setCancelable(false);
            progressDialog.setMessage(context.getString(
                    R.string.dialog_load_webiste_message));
        }
    }

    /**
     * Ta metoda wydarza się wtedy gdy chcemy nadpisać ładowanie aktualnego
     * linku URL.
     * Wywołuje wizualizer PDF gdy wykryje PDF w linku.
     * @param view Przeglądarka.
     * @param url URL.
     * @return Czy nadpisujemy ładowanie czy też kontynuujemy je.
     */
    @Override public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        if(url.endsWith(".pdf") && secondPdfTry == false)
        {
            view.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            secondPdfTry = true;
            return true;
        }
        else
        {
            secondPdfTry = false;
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
