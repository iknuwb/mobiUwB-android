package pl.edu.uwb.ii.mobiuwb;

import android.webkit.WebViewClient;


/**
 * Jest to klasa, której zadaniem jest nasłuchiwanie, czy dana strona jest
 * PDF'em.
 */
public class PDFHandlingClient extends WebViewClient
{
	
	/**
	 * Jest to zmienna pomocnicza odpowiadająca za wyłączenie rekurencji w
	 * metodzie shouldOverrideUrlLoading.
	 */
	// private boolean pom = false;
	
	/**
	 * Jest to wydarzenie wywołujące się przy każdym wczytaniu jakiejkolwiek
	 * strony (przy próśbie wczytania). Jego zadaniem jest sprawdzenie, czy dana
	 * strona nie jest dokumentem PDF.
	 */
	// @Override
	// public boolean shouldOverrideUrlLoading(WebView view, String url)
	// {
	// return true;
	// }
	
}
