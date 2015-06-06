package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.configurationXml;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.DataInitializeTaskOutput;
import pl.edu.uwb.mobiuwb.tasks.Task;
import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.XMLParser;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class ConfigurationXmlTask implements Task<DataInitializeTaskOutput>
{
    @Override
    public void execute(TaskInput input, DataInitializeTaskOutput output)
    {
        String fileContent = output.versioningResult.getFileContent();
        XMLParser xmlParser = new XMLParser();
        try
        {
            output.configXmlResult =
                    xmlParser.deserializeConfigurationXml(fileContent);
        }
        catch (Exception e)
        {
            output.addError(e.getMessage());
        }
    }
}
