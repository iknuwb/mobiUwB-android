package pl.edu.uwb.mobiuwb.VersionControl.Models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Jest to klasa opakowywująca dane zwrócone przez system kontroli wersji.
 */
public class VersioningResult
{
    /**
     * Żądanie do systemu kontroli wersji.
     */
    private VersioningRequest versioningRequest;

    /**
     * Pobiera żądanie do systemu kontroli wersji.
     * @return Żądanie do systemu kontroli wersji.
     */
    public VersioningRequest getVersioningRequest()
    {
        return versioningRequest;
    }

    /**
     * Nadaje żądanie do systemu kontroli wersji.
     * @param versioningRequest Żądanie do systemu kontroli wersji.
     */
    public void setVersioningRequest(
            VersioningRequest versioningRequest)
    {
        this.versioningRequest = versioningRequest;
    }

    /**
     * Strumień plikowy dla pobranego pliku.
     */
    private InputStream fileInputStream;

    /**
     * Pobiera strumień plikowy dla pobranego pliku.
     * @return Strumień plikowy dla pobranego pliku.
     */
    public InputStream getFileInputStream()
    {
        return fileInputStream;
    }

    /**
     * Nadaje strumień plikowy dla pobranego pliku.
     * @param fileInputStream Strumień plikowy dla pobranego pliku.
     */
    public void setFileInputStream(InputStream fileInputStream)
    {
        this.fileInputStream = fileInputStream;
    }

    /**
     * Zawartość pobranego pliku.
     */
    private String fileContent;

    /**
     * Pobiera zawartość pobranego pliku.
     * @return Zawartość pobranego pliku.
     */
    public String getFileContent()
    {
        return fileContent;
    }

    /**
     * Nadaje zawartość pobranego pliku.
     * @param fileContent Zawartość pobranego pliku.
     */
    public void setFileContent(String fileContent)
    {
        this.fileContent = fileContent;
    }


    /**
     * Lista komunikatów o błędach/błędów.
     */
    private List<String> errorMessages;

    /**
     * Pobiera listę komunikatów o błędach.
     * @return Lista komunikatów o błedach/błędów.
     */
    public List<String> getErrorMessages()
    {
        return errorMessages;
    }

    /**
     * Dodaje nowy komunikat/błąd do listy.
     * @param errorMessage Komunikat błędu.
     */
    public void addErrorMessage(String errorMessage)
    {
        errorMessages.add(errorMessage);
    }

    /**
     * Determinuje, czy żądanie do systemu się powiodło.
     */
    private Boolean succeeded;

    /**
     * Determinuje, czy żądanie do systemu się powiodło.
     */
    public Boolean getSucceeded()
    {
        return succeeded;
    }

    /**
     * Nadaje, czy żądanie do systemu się powiodło.
     * @param succeeded Czy żądanie do systemu się powiodło.
     */
    public void setSucceeded(Boolean succeeded)
    {
        this.succeeded = succeeded;
    }

    /**
     * Tworzy instancję klasy.
     * Zakłada, że żądanie się powiodło.
     */
    public VersioningResult()
    {
        succeeded = true;
        errorMessages = new ArrayList<String>();
    }
}
