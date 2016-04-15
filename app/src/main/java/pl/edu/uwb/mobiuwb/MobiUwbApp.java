package pl.edu.uwb.mobiuwb;

import android.app.Application;

/**
 * Jest to reprezentacja aplikacji MobiUwB w programie.
 * Klasa ta tworzy obiekt przy starcie tejże aplikacji.
 */
public class MobiUwbApp extends Application
{

    /**
     * Instancja tej klasy.
     */
    private static MobiUwbApp instance;

    /**
     * Wywołuje się, gdy aplikacja się tworzy.
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    /**
     * Metoda ta pozyskuje kontekst aplikacji.
     * @return Kontekst aplikacji.
     */
    public static MobiUwbApp getContext()
    {
        return instance;
    }
}