package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.versionController;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningRequest;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.VersionControl.VersionController;
import pl.edu.uwb.mobiuwb.services.notification.ServicePreferencesManager;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.DataInitializeTaskOutput;
import pl.edu.uwb.mobiuwb.tasks.Task;
import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * {@inheritDoc}
 * To jest zadanie polegające na zapewnieniu zawsze najnowszej wersji pliku
 * Konfiguracji.
 */
public class VersionControllerTask implements Task<DataInitializeTaskOutput>
{
    /**
     * Nazwa usługi do Preferencji.
     */
    public static final String SHARED_PREFERENCES_SERVICE_NAME = "sharedPrefsService";

    /**
     * {@inheritDoc}
     * Zadanie to wykonuje bardzo złożony proces, polegający na pobraniu
     * najnowszej wersji pliku Konfiguracji. Sprowadza się on do dwóch założeń:
     * Sprawdzenia stanu pliku Konfiguracji lokalnego, oraz sprawdzenia pliku
     * Konfiguracji w Internecie. Jeżeli któryś z nich jest aktualniejszy, to
     * zostaje on pobrany na urządzenie a metoda ta wypełni swój output
     * właśnie nim.
     */
    @Override
    public void execute(TaskInput input, DataInitializeTaskOutput output)
    {
        VersionControllerTaskInput versionControllerTaskInput = (VersionControllerTaskInput)input;
        String configurationFileName = versionControllerTaskInput.getConfigurationFileName();
        Context baseContext = versionControllerTaskInput.getBaseContext();

        String configurationDirectoryPath = baseContext.getFilesDir().getPath();
        File configurationFile = new File(configurationDirectoryPath,configurationFileName);

        VersioningRequest versioningRequest = new VersioningRequest(
                output.propertiesXmlResult.getXmlPropertiesRootElement().getConfigurationFilePath(),
                configurationFile.getAbsolutePath());

        SharedPreferences shareds = baseContext.getSharedPreferences(
                SHARED_PREFERENCES_SERVICE_NAME,
                Context.MODE_PRIVATE);

        VersionController versionController = new VersionController(shareds);

        VersioningResult versioningResult = versionController.getNewestFile(versioningRequest);

        output.addErrors(versioningResult.getErrorMessages());

        output.configurationFile = configurationFile;
        output.versioningResult = versioningResult;
    }
}
