package pl.edu.uwb.mobiuwb.connection;

import android.os.AsyncTask;
import android.util.Log;

import pl.edu.uwb.mobiuwb.connection.interfaces.OnConnectionChangedListener;
import pl.edu.uwb.mobiuwb.utillities.Globals;

/**
 * Jest to wątek odpowiedzialny za sprawdzanie stanu połączenia z Internetem.
 */
public class InternetCheckingService extends AsyncTask<Void, InternetCheckingService.ProgressUpdateData, Void>
{
    /**
     * Odstęp, po jakim następuje sprawdzenie Internetu.
     */
    private static final long INTERVAL_MILISECONDS = 500;

    /**
     * Sprawdzacz typu połączenia z Internetem WiFi.
     */
    private InternetChecker wifiInternetChecker;

    /**
     * Nadaje sprawdzacz typu połączenia z Internetem WiFi.
     * @param wifiInternetChecker Sprawdzacz typu połączenia
     *                            z Internetem WiFi.
     */
    public void setWifiInternetChecker(
            InternetChecker wifiInternetChecker)
    {
        this.wifiInternetChecker = wifiInternetChecker;
    }

    /**
     * Sprawdzacz typu połączenia z Internetem mobilnym.
     */
    private InternetChecker mobileInternetChecker;

    /**
     * Nadaje sprawdzacz typu połączenia z Internetem mobilnym.
     * @param mobileInternetChecker Sprawdzacz typu połączenia
     *                              z Internetem mobilnym.
     */
    public void setMobileInternetChecker(
            InternetChecker mobileInternetChecker)
    {
        this.mobileInternetChecker = mobileInternetChecker;
    }

    /**
     * Wydarza się, gdy zostanie zmieniony stan połączenia.
     * Udostępnia informację o tym stanie.
     */
    private OnConnectionChangedListener onConnectionLostListener;

    /**
     * Nadaje nasłuchiwacz na wydarzenie o zmianie stanu połączenia.
     * @param onConnectionLostListener Nowy nasłuchiwacz na wydarzenie
     *                                 o zmianie stanu połączenia.
     */
    public void setOnConnectionLostListener(
            OnConnectionChangedListener onConnectionLostListener)
    {
        this.onConnectionLostListener = onConnectionLostListener;
    }

    /**
     * Wydarza się, gdy urządzenie otrzyma połączenie Internetowe.
     */
    private OnConnectionChangedListener onConnectionRecivedListener;

    /**
     * Nadaje nasłuchiwacz na nowe połączenie Internetowe.
     * @param onConnectionRecivedListener Nasłuchiwacz na nowe
     *                                    połączenie Internetowe.
     */
    public void setOnConnectionRecivedListener(
            OnConnectionChangedListener onConnectionRecivedListener)
    {
        this.onConnectionRecivedListener = onConnectionRecivedListener;
    }

    /**
     * Stwierdza, czy ten wątek działa.
     */
    private boolean running;

    /**
     * Sprawia, że wątek przestaje działać.
     */
    public void stopService()
    {
        running = false;
    }

    /**
     * Wydarza się, zanim wywoła się wątek.
     * Nadaje zmienną, że wątek działa.
     */
    @Override protected void onPreExecute()
    {
        super.onPreExecute();
        //Log.d(Globals.MOBIUWB_TAG, "onPreExecute");
        running = true;
    }

    /**
     * Metoda ta wywołuje się w oddzielnym wątku.
     * Odpowiada ona za ciągłe sprawdzanie Internetu w aplikacji.
     * Determinuje typ aktualnego Internetu.
     * Wysyła powiadomienia do onProgressUpdate o zmienionym typie połączenia.
     * @param params Te parametry są nieużywane.
     * @return Typ zwracany jest nieużywany.
     */
    @Override protected Void doInBackground(Void... params)
    {
        boolean wifiCheck = false;
        boolean mobileCheck = false;

        boolean previousInternetState = true;
        boolean currentInternetState = false;

        CheckingType checkingType = CheckingType.WIFI;

        while (running)
        {
            try
            {
                if(wifiInternetChecker != null)
                {
                    wifiCheck = wifiInternetChecker.check();
                    currentInternetState = wifiCheck;
                }
                if(wifiCheck == true)
                {
                    if(checkingType != CheckingType.WIFI)
                    {
                        checkingType = CheckingType.WIFI;
                        publishProgress(new ProgressUpdateData(checkingType, currentInternetState));
                        previousInternetState = true;
                    }
                }
                else
                {
                    if(mobileInternetChecker != null)
                    {
                        mobileCheck = mobileInternetChecker.check();
                        currentInternetState = mobileCheck;
                    }
                    if(mobileCheck == true)
                    {
                        if(checkingType != CheckingType.MOBILE)
                        {
                            checkingType = CheckingType.MOBILE;
                            publishProgress(new ProgressUpdateData(checkingType, currentInternetState));
                            previousInternetState = true;
                        }
                    }
                    else
                    {
                        Log.d(Globals.MOBIUWB_TAG,"else previousInternetState: " + previousInternetState);
                        Log.d(Globals.MOBIUWB_TAG,"currentInternetState: " + currentInternetState);
                        if(previousInternetState != currentInternetState)
                        {
                            Log.d(Globals.MOBIUWB_TAG,"if previousInternetState: " + previousInternetState);
                            Log.d(Globals.MOBIUWB_TAG,"currentInternetState: " + currentInternetState);
                            checkingType = CheckingType.NONE;
                            previousInternetState = currentInternetState;
                            publishProgress(new ProgressUpdateData(checkingType, currentInternetState));
                        }
                    }
                }
                Thread.sleep(INTERVAL_MILISECONDS);

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Wydarza się, gdy wątek powiadomi o zmianie typu połączenia.
     * Uruchamia zdarzenie o zmianie typu połączenia.
     * @param values Zawiera informacje o typie połączenia.
     */
    @Override protected void onProgressUpdate(ProgressUpdateData... values)
    {
        //Log.d(Globals.MOBIUWB_TAG, "onProgressUpdate: " + values[0]);
        super.onProgressUpdate(values);
        ProgressUpdateData data = values[0];

        if (data.isActive())
        {
            onConnectionRecivedListener.connectionChanged(data.getCheckingType());
        }
        else
        {
            onConnectionLostListener.connectionChanged(data.getCheckingType());
        }
    }

    /**
     * Zawiera informacje o typie połączenia.
     */
    public class ProgressUpdateData
    {
        /**
         * Opisuje typ połączenia.
         */
        private CheckingType checkingType;

        /**
         * Pobiera typ połączenia.
         * @return Typ połączenia.
         */
        public CheckingType getCheckingType()
        {
            return checkingType;
        }

        /**
         * Nadaje typ połączenia.
         * @param checkingType Typ połączenia.
         */
        public void setCheckingType(CheckingType checkingType)
        {
            this.checkingType = checkingType;
        }

        /**
         * Stwierdza, czy połączenie jest aktywne.
         */
        private boolean active;

        /**
         * Stwierdza, czy połączenie jest aktywne.
         * @return Czy połączenie jest aktywne. True, jeśli tak, false, jeśli nie.
         */
        public boolean isActive()
        {
            return active;
        }

        /**
         * Nadaje informację, czy połączenie jest aktywne.
         * @param active Czy połączenie jest aktywne.
         */
        public void setActive(boolean active)
        {
            this.active = active;
        }

        /**
         * Inicjuje pola w klasie.
         * @param checkingType Typ połączenia.
         * @param active Czy połączenie jest aktywne.
         */
        public ProgressUpdateData(CheckingType checkingType,
                                  boolean active)
        {
            this.checkingType = checkingType;
            this.active = active;
        }

        /**
         * Porównuje dwa obiekty tego typu.
         * @param o Inny obiekt tego typu.
         * @return True, jeśli są równe, false, jeśli nie.
         */
        @Override public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            ProgressUpdateData data = (ProgressUpdateData) o;

            if (isActive() != data.isActive())
            {
                return false;
            }
            return getCheckingType() == data.getCheckingType();

        }

        /**
         * Pobiera haszkod. Jeśli dwie instancje mają różny haszkod,
         * to uważane są za różne.
         * @return Haszkod instancji.
         */
        @Override
        public int hashCode()
        {
            return 0;
        }
    }
}
