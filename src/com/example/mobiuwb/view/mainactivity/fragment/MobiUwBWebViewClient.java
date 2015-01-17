package com.example.mobiuwb.view.mainactivity.fragment;

import com.example.mobiuwb.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MobiUwBWebViewClient extends WebViewClient
{
	private ProgressDialog progressDialog;
	
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
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
	}
	
	public void dismissProgressDialog()
	{
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
		progressDialog = null;
	}
	
	private void initProgressDialog(Context context)
	{
		if(progressDialog == null)
		{
			progressDialog = new ProgressDialog(
					context, 
					AlertDialog.THEME_HOLO_DARK);
			
			progressDialog.setCancelable(false);
			progressDialog.setTitle(context.getString(
					R.string.dialog_load_webiste_title));
			progressDialog.setMessage(context.getString(
					R.string.dialog_load_webiste_message));
		}
	}
}
