package pl.edu.uwb.mobiuwb.view.interfaces;

import android.content.Context;
import android.content.Intent;

/**
 * Odpowiada za metody dostępu do danego widoku.
 */
public interface GuiAccess
{
    /**
     * Zamyka dany widok.
     */
    void finishActivity();

    /**
     * Odświeża obecną w nim przeglądarkę.
     */
    void refreshWebView();

    /**
     * Startuje nowy widok.
     * @param intent Intencja do startu widoku.
     */
    void startActivityAccess(Intent intent);

    /**
     * Startuje nowy widok i czeka na wiadomość zwrotną.
     * @param intent Intencja do startu widoku.
     * @param RequestCode Kod wiadomości zwrotnej.
     */
    void startActivityForResultAccess(Intent intent, int RequestCode);

    /**
     * Pobiera kontekst widoku.
     * @return Kontekst widoku.
     */
    Context getContextAccess();
}
