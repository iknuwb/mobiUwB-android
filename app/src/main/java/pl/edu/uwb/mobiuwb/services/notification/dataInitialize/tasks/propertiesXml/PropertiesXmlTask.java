package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.propertiesXml;

import android.content.Context;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.DataInitializeTaskOutput;
import pl.edu.uwb.mobiuwb.tasks.Task;
import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.XMLParser;

/**
 * {@inheritDoc}
 * Zadanie to deserializuje plik Właściwości.
 */
public class PropertiesXmlTask implements Task<DataInitializeTaskOutput>
{
    /**
     * {@inheritDoc}
     * Zadanie to deserializuje plik XML Właściwości.
     * Pobiera z niego wszelkie właściwości i zapisuje do outputu.
     */
    @Override
    public void execute(TaskInput input, DataInitializeTaskOutput output)
    {
        PropertiesXmlTaskInput propertiesXmlTaskInput = (PropertiesXmlTaskInput)input;

        String configurationFileName = propertiesXmlTaskInput.getPropertiesFileName();
        Context baseContext = propertiesXmlTaskInput.getBaseContext();

        String assetsDirrectoryPath = baseContext.getFilesDir().getPath();

        File propFile = new File(assetsDirrectoryPath, configurationFileName);
        output.propertiesFile = propFile;


        XMLParser xmlParser = new XMLParser();

        try
        {
            output.propertiesXmlResult = xmlParser.deserializePropertiesXml(propFile);
        }
        catch (IOException e)
        {
            output.addError(e.getMessage());
        }
        catch (ParserConfigurationException e)
        {
            output.addError(e.getMessage());
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
    }
}
