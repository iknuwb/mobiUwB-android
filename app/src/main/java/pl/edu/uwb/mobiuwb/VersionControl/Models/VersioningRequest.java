package pl.edu.uwb.mobiuwb.VersionControl.Models;

import java.net.URI;

/**
 * Enkapsuluje w sobie żądanie systemu zarządzania wersjami.
 */
public class VersioningRequest
{
    /**
     * ścieżka dostępu do pliku w Internecie.
     */
    private String internetFile;

    /**
     * Pobiera ścieżkę dostępu do pliku w Internecie.
     * @return ścieżka dostępu do pliku w Internecie.
     */
    public String getInternetFile()
    {
        return internetFile;
    }

    /**
     * ścieżka do zapisania pliku na urządzeniu.
     */
    private String overwriteDestinationPath;

    /**
     * Pobiera ścieżkę do zapisania pliku na urządzeniu.
     * @return ścieżka do zapisania pliku na urządzeniu.
     */
    public String getOverwriteDestinationPath()
    {
        return overwriteDestinationPath;
    }

    /**
     * Nadaje pola w klasie.
     * @param internetFile ścieżka dostępu do pliku w Internecie.
     * @param overwriteDestinationPath ścieżka do zapisania pliku na urządzeniu.
     */
    public VersioningRequest(String internetFile, String overwriteDestinationPath)
    {
        this.internetFile = internetFile;
        this.overwriteDestinationPath = overwriteDestinationPath;
    }
}
