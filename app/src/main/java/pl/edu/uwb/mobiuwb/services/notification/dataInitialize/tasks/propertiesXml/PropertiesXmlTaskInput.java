package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.propertiesXml;

import android.content.Context;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * {@inheritDoc}
 * Są to dane wejściowe dla zadania z plikiem XML Właściwości.
 */
public class PropertiesXmlTaskInput extends TaskInput
{
    /**
     * Nazwa pliku Właściwości.
     */
    private String propertiesFileName;

    /**
     *  Pobiera nazwę pliku Właściwości.
     *  @return Nazwa pliku Właściwości.
     */
    public String getPropertiesFileName()
    {
        return propertiesFileName;
    }

    /**
     * Podstawowy kontekst z widoku lub aplikacji.
     */
    private Context baseContext;

    /**
     * Pobiera podstawowy kontekst z widoku lub aplikacji.
     * @return Podstawowy kontekst z widoku lub aplikacji.
     */
    public Context getBaseContext()
    {
        return baseContext;
    }

    /**
     * Nadaje wartości polom.
     * @param propertiesFileName Nazwa pliku Właściwości.
     * @param baseContext Podstawowy kontekst z widoku lub aplikacji.
     */
    public PropertiesXmlTaskInput(String propertiesFileName, Context baseContext)
    {
        this.propertiesFileName = propertiesFileName;
        this.baseContext = baseContext;
    }
}
