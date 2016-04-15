package pl.edu.uwb.mobiuwb.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import pl.edu.uwb.mobiuwb.MobiUwbApp;

import java.util.ArrayList;

/**
 * Rozpoznaje typ Internetu, z jakiego korzysta urządzenie.
 */
public abstract class InternetChecker
{
    /**
     * Obejmuje menedżer połączeń systemu operacyjnego.
     */
    private ConnectivityManager connectivityManager;
    /**
     * Jest odpowiedzialna za listę informacji o połączeniu.
     * Podklasy wypełniają tą listę w metodzie fillNetworkInfo.
     */
    protected ArrayList<NetworkInfo> networkInfos;

    /**
     * Inicjuje zmienne oraz pobiera z systemu instancję menedżera systemu.
     * Wywołuje metodę wypełnienia listy z informacjami o połączeniu,
     * która jest delegowana do podklas.
     */
    public InternetChecker()
    {
        networkInfos = new ArrayList<NetworkInfo>();
        connectivityManager = (ConnectivityManager)
                MobiUwbApp.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        fillNetworkInfo(connectivityManager, networkInfos);
    }

    /**
     * Metoda ta wypełnia, w zależności od implementacji, listę informacji o połączeniu.
     * @param connectivityManager Menedżer połączeń systemu operacyjnego.
     * @param networkInfos Lista połączeń do wypełnienia
     */
    protected abstract void fillNetworkInfo(ConnectivityManager connectivityManager,
                                            ArrayList<NetworkInfo> networkInfos);

    /**
     * Sprawdza, czy mamy do czynienia z połączeniem obsługiwanym przez tą podklasą.
     * Wywołuje metodę wypełnienia listy z informacjami o połączeniu,
     * która jest delegowana do podklas.
     * @return Czy to jest to połączenie, w zależności od podklasy.
     */
    public boolean check()
    {
        networkInfos.clear();
        fillNetworkInfo(connectivityManager, networkInfos);
        return makeCheck();
    }

    /**
     * Sprawdza, czy określone typy połączeń reprezentowane przez
     * tą podklasę są dostępne na urządzeniu.
     * @return true, jeśli wszystkie są dostępne, false, jeśli choćby jeden był niedostępny.
     */
    public boolean isAvailable()
    {
        for (NetworkInfo networkInfo : networkInfos)
        {
            if(networkInfo == null)
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Implementacja tej metody sprawdza, czy ta podklasa zawiera w sobie
     * aktualnie obsługiwany typ połączenia.
     * @return true, jeżeli spośród obsługiwanych typów połączenia jest ten aktualny,
     * false, jeśli nie.
     */
    protected abstract boolean makeCheck();
}
