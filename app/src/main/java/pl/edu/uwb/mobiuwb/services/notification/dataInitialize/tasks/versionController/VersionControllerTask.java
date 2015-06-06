package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.versionController;

import android.content.Context;

import java.io.File;

import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningRequest;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.VersionControl.VersionController;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.DataInitializeTaskOutput;
import pl.edu.uwb.mobiuwb.tasks.Task;
import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class VersionControllerTask implements Task<DataInitializeTaskOutput>
{
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

        VersionController versionController = new VersionController();
        VersioningResult versioningResult = versionController.getNewestFile(versioningRequest);

        output.addErrors(versioningResult.getErrorMessages());

        output.configurationFile = configurationFile;
        output.versioningResult = versioningResult;
    }
}
