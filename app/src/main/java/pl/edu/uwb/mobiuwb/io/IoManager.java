package pl.edu.uwb.mobiuwb.io;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

/**
 * Reprezentuje pomocniczego zarządcę operacjami wejścia/wyjścia
 */
public class IoManager
{
    /**
     * Jest to zmienna przechowująca nazwę aktualnego pliku
     * XML Właściwości do parsowania.
     */
    public static final String PROPERTIES_XML_FILE_NAME = "properties.xml";

    /**
     * Jest to zmienna przechowująca nazwę aktualnego pliku
     * XML Konfiguracji do parsowania.
     */
    public static final String CONFIGURATION_XML_FILE_NAME = "config.xml";

    /**
     * Funkcja ta kopiuje wskazany plik Assetsów
     * do folderu pamięci wewnętrznej urządzenia.
     * @param assetsImputStream Strumień pochodzący z Assetsów.
     * @param assetsFileName Nazwa pliku w Assetsach.
     * @param destinationFolderPath Do którego folderu zostanie
     *                              wkopiowany plik.
     * @return Czy udało się skopiować plik.
     */
    public static boolean copyAssetsFile(
            InputStream assetsImputStream,
            String assetsFileName,
            String destinationFolderPath)
    {
        try
        {
            File destinationFolderFile = new File(destinationFolderPath);
            if (destinationFolderFile.canWrite())
            {
                File destination = new File(
                        destinationFolderPath,
                        assetsFileName);

                FileOutputStream destinationFileOutputStream = new FileOutputStream(
                        destination);

                copyFile(assetsImputStream, destinationFileOutputStream);

                destinationFileOutputStream.close();
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Pobiera plik z Internetu.
     * @param fileLink Link do pobrania pliku.
     * @param destinationFileName Jak plik ma się nazywać po pobraniu.
     * @param destinationFolderPath Gdzie pobrać dany plik.
     * @throws IOException Jeżeli wystąpi jakikolwiek błąd
     *                     związany z zapisem pliku.
     */
    public static void downloadFile(
            String fileLink,
            String destinationFileName,
            String destinationFolderPath)
            throws IOException
    {
        File file = new File(destinationFolderPath, destinationFileName);
        downloadFile(fileLink, file.getAbsolutePath());
    }

    /**
     * Pobiera plik z Internetu.
     * @param fileLink Link do pobrania pliku.
     * @param destinationFileFullPath Gdzie pobrać dany plik.
     *                              Razem z nazwą tegoż pliku.
     * @throws IOException Jeżeli wystąpi jakikolwiek błąd
     *                     związany z zapisem pliku.
     */
    public static void downloadFile(
            String fileLink,
            String destinationFileFullPath)
            throws IOException
    {
        URL u = new URL(fileLink);
        InputStream is = u.openStream();
        DataInputStream dataInputStream = new DataInputStream(is);

        File destination = new File(destinationFileFullPath);

        FileOutputStream destinationFileOutputStream = new FileOutputStream(
                destination);
        copyFile(dataInputStream, destinationFileOutputStream);
    }

    /**
     * Uogólniona metoda do kopiowania plików w określony strumień.
     * @param in Wejściowy strumień, do odczytu.
     * @param out Wyjściowy strumień, do zapisu.
     * @throws IOException Gdy wystąpi jakikolwiek błąd związany z zapisem.
     */
    private static void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    /**
     * Sprawdza, czy plik istnieje w danej lokalizacji.
     * @param fullFilePath Pełna ścieżka do pliku.
     * @return Czy plik istnieje.
     */
    public static boolean checkIfFileExists(String fullFilePath)
    {
        File file = new File(fullFilePath);
        return file.exists();
    }

    /**
     * Pobiera zawartość pliku.
     * @param fullFilePath ścieżka dostępu do pliku. łącznie z nazwą.
     * @return Zawartość pliku ze ścieżki.
     * @throws IOException Gdy wystąpi jakikolwiek błąd związany z odczytem.
     */

    public static String getFileContent(
            String fullFilePath)
            throws IOException
    {
        FileInputStream fileInputStream = getFileInputStream(fullFilePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    /**
     * Pobiera strumień danego pliku.
     * @param fullFilePath ścieżka dostępu do pliku. łącznie z nazwą.
     * @return Strumień danego pliku do odczytu.
     * @throws FileNotFoundException Gdy funkcja nie znajdzie pliku.
     */
    public static FileInputStream getFileInputStream(
            String fullFilePath)
            throws FileNotFoundException
    {
        File file = new File(fullFilePath);
        return new FileInputStream(file);
    }
}
