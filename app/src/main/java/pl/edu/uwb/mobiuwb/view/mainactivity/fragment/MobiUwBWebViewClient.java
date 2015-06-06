package pl.edu.uwb.mobiuwb.view.mainactivity.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pl.edu.uwb.mobiuwb.R;

public class MobiUwBWebViewClient extends WebViewClient
{
    private ProgressDialog progressDialog;
    private boolean secondPdfTry;


    public MobiUwBWebViewClient()
    {
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        super.onPageStarted(view, url, favicon);
        initProgressDialog(view.getContext());
        progressDialog.show();
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        super.onPageFinished(view, url);
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }

    public void dismissProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

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
