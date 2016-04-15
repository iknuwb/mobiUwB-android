package pl.edu.uwb.mobiuwb.VersionControl;

import android.content.SharedPreferences;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningRequest;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.io.IoManager;

/**
 * Jest to kontroler wersji pliku.
 * Podstawowym jego zadaniem jest zapewnienie korzystającego, że plik jest
 * w najnowszej możliwej wersji.
 */
public class VersionController
{
    /**
     * Preferencje systemu operacyjnego Android.
     */
    private SharedPreferences shareds;

    /**
     * Inicjalizuje zmienne.
     * @param shareds Preferencje systemu operacyjnego Android.
     */
    public VersionController(SharedPreferences shareds)
    {
        this.shareds = shareds;
    }

    /**
     * Metoda ta pobiera możliwie najnowszy plik, niezależnie od tego,
     * czy znajduje się on lokalnie na urządzeniu czy też w Internecie.
     * Jeżeli w Internecie znajduje się jego nowsza wersja, pobiera go
     * po czym wysyła na wyjście (w tym przypadku wypełnia nim zmienną w
     * danych wyjściowych). Jeżeli z kolei jego nowsza wersja znajduje się
     * lokalnie na urządzeniu, to po prostu czyta ją i to nią wypełnia
     * zmienną wyjściową.
     * @param versioningRequest Wejściowy obiekt żądania kontroli wersji.
     *                          Zawiera wszelkie niezbędne startowe dane.
     * @return Rezultat funkcji, zawierający dane o najnowszej możliwej
     * wersji zwracanego pliku.
     */
    public VersioningResult getNewestFile(VersioningRequest versioningRequest)
    {
        VersioningResult result = new VersioningResult();
        try
        {
            Boolean deviceFileExists =
                    IoManager.checkIfFileExists(
                            versioningRequest.getOverwriteDestinationPath());

            if(deviceFileExists)
            {
                Boolean isDeviceFileCurrent = checkIfIsCurrent(versioningRequest);
                if(!isDeviceFileCurrent)
                {
                    downloadFile(versioningRequest);
                }
            }
            else
            {
                downloadFile(versioningRequest);
            }
            FillResultWithLocalFile(
                versioningRequest,
                result);
        }
        catch (Exception e)
        {
            result.addErrorMessage(e.getMessage());
            result.setSucceeded(false);
        }
        return result;
    }

    /**
     * Metoda ta pobiera plik z Internetu.
     * @param versioningRequest Wejściowe dane.
     * @throws IOException Gdy wystąpi błąd zapisu/odczytu.
     * @throws ParseException Gdy wystąpi błąd z analizą pliku.
     */
    private void downloadFile(VersioningRequest versioningRequest)
            throws IOException, ParseException
    {
        IoManager.downloadFile(
                versioningRequest.getInternetFile(),
                versioningRequest.getOverwriteDestinationPath());

        Date internetFileModificationDate = getInternetFileModificationDate(versioningRequest);

        SharedPreferences.Editor editor = shareds.edit();
        editor.putLong(versioningRequest.getOverwriteDestinationPath(),
                       internetFileModificationDate.getTime());
        editor.commit();
    }

    /**
     * Ta metoda sprawdza, czy plik obecny w Internecie nie nadaje się do pobrania.
     * Innymi słowy, czy ma starszą wersję.
     * Wszystko determinuje data modyfikacji.
     * @param versioningRequest Wejściowe dane.
     * @return Czy plik w Internecie ma starszą datę niż lokalny.
     * @throws IOException Gdy wystąpi błąd zapisu/odczytu.
     * @throws ParseException Gdy wystąpi błąd z analizą pliku.
     */
    private Boolean checkIfIsCurrent(VersioningRequest versioningRequest)
            throws IOException, ParseException
    {
        Date internetFileModificationDate =
                getInternetFileModificationDate(versioningRequest);

        long defaultLongDate = 0;

        Long lastDateLong =
                shareds.getLong(
                        versioningRequest.getOverwriteDestinationPath(),
                        defaultLongDate);
        Date lastDate = new Date(lastDateLong);

        if(lastDate.before(internetFileModificationDate))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Pobiera datę modyfikacji pliku obecnego w Internecie.
     * W przypadku nieobecnej informacji o dacie modyfikacji,
     * zwracana jest pierwsza możliwa data.
     * @param versioningRequest Wejściowe dane.
     * @return Data modyfikacji.
     * @throws IOException Gdy wystąpi błąd zapisu/odczytu.
     * @throws ParseException Gdy wystąpi błąd z analizą pliku.
     */
    private Date getInternetFileModificationDate(VersioningRequest versioningRequest)
            throws IOException, ParseException
    {
        URL obj = new URL(versioningRequest.getInternetFile());
        URLConnection conn = obj.openConnection();

        String internetFileModificationDateString =
                conn.getHeaderField("Last-Modified");
        if(internetFileModificationDateString == null)
        {
            return new Date(0);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.parse(
                internetFileModificationDateString);
    }

    /**
     * Wypełnia informacje zwrotne danymi pliku.
     * @param versioningRequest Wejściowe dane.
     * @param result Dane zwrotne, z wypełnionymi informacjami o pliku.
     * @throws IOException Gdy wystąpi błąd zapisu/odczytu.
     */
    private void FillResultWithLocalFile(
            VersioningRequest versioningRequest,
            VersioningResult result)
            throws IOException
    {
        result.setFileContent(
                IoManager.getFileContent(
                        versioningRequest.getOverwriteDestinationPath()));

        result.setFileInputStream(
                IoManager.getFileInputStream(
                        versioningRequest.getOverwriteDestinationPath()));
    }
}
