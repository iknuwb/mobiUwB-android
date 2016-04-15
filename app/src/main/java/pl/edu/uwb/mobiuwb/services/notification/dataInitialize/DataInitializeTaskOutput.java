package pl.edu.uwb.mobiuwb.services.notification.dataInitialize;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.tasks.models.TaskOutput;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.ConfigXmlResult;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.PropertiesXmlResult;

/**
 * {@inheritDoc}
 * Te dane wyjściowe są współdzielone przez wiele zadań w tej aplikacji.
 * Są odpowiedzialne za enkapsulację wielu elementów powiązanych z plikami
 * oraz z elementami z ekranu Opcji/Ustawień.
 */
public class DataInitializeTaskOutput extends TaskOutput
{
    /**
     * Plik Właściwości.
     */
    public File propertiesFile;

    /**
     * Plik Konfiguracji.
     */
    public File configurationFile;

    /**
     * Wynik analizy pliku XML Właściwości.
     */
    public PropertiesXmlResult propertiesXmlResult;

    /**
     * Wynik procesu kontroli wersji.
     */
    public VersioningResult versioningResult;

    /**
     * Wynik analizy pliku XML Konfiguracji.
     */
    public ConfigXmlResult configXmlResult;

    /**
     * Czy notyfikacje w programie są uruchomione.
     */
    public boolean isNotificationActive;

    /**
     * Czy zakres czasowy jest uruchomiony w programie.
     * Oznacza on czas w jakim aplikacja może wysyłać powiadomienia.
     */
    public boolean isTimeRangeActive;

    /**
     * Godzina "od" w zakresie czasu.
     */
    public Date timeRangeFrom;

    /**
     * Godzina "do" w zakresie czasu.
     */
    public Date timeRangeTo;

    /**
     * Indeks na liście dostępnych odstępów czasowych od kolejnych powiadomień.
     */
    public int intervalIndex;

    /**
     * Przerwa między powiadomieniami.
     */
    public long interval;

    /**
     * Mapa, której zadaniem jest zmapowanie
     * nazwa_właściwości_w_Opcjach -> czy_notyfikacje_aktywne.
     */
    public HashMap<String, Boolean> categories;

    /**
     * Inicjalizuje instancję.
     */
    public DataInitializeTaskOutput()
    {
        categories = new HashMap<String, Boolean>();
    }
}
