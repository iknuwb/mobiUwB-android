package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.versionController;

import android.content.Context;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * {@inheritDoc}
 * Są to dane wejściowe dla zadania związanego z kontrolą wersji pliku.
 */
public class VersionControllerTaskInput extends TaskInput
{
    /**
     * Nazwa pliku Konfiguracji.
     */
    private String configurationFileName;

    /**
     * Pobiera nazwę pliku Konfiguracji.
     * @return Nazwa pliku Konfiguracji.
     */
    public String getConfigurationFileName()
    {
        return configurationFileName;
    }

    /**
     * Kontekst pochodzący z aplikacji lub z widoku.
     */
    private Context baseContext;

    /**
     * Pobiera kontekst pochodzący z aplikacji lub z widoku.
     * @return Kontekst pochodzący z aplikacji lub z widoku.
     */
    public Context getBaseContext()
    {
        return baseContext;
    }

    /**
     * Inicjalizuje pola.
     * @param propertiesFileName Nazwa pliku Konfiguracji.
     * @param baseContext Kontekst pochodzący z aplikacji lub z widoku.
     */
    public VersionControllerTaskInput(String propertiesFileName,
                                      Context baseContext)
    {
        this.configurationFileName = propertiesFileName;
        this.baseContext = baseContext;
    }
}
