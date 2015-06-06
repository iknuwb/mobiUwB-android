package pl.edu.uwb.mobiuwb.parsers.xml.parser.result.validator;

import android.util.Log;

import pl.edu.uwb.mobiuwb.MobiUwbApp;
import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.parsers.xml.model.Website;
import pl.edu.uwb.mobiuwb.parsers.xml.model.XmlPropertiesRootElement;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.PropertiesXmlResult;

import java.util.ArrayList;
import java.util.List;

public class PropertiesXmlResultValidator
{

    public PropertiesXmlResult validate(List<Website> defaultWebsites,
                                        List<Website> websites,
                                        String configurationFilePath)
    {
        Website defaultWebsite = null;

        List<String> errors = new ArrayList<String>();
        if (defaultWebsites.size() > 1)
        {
            errors.add(MobiUwbApp.getContext().getString(
                    R.string.error_too_many_default_sections));
        }
        else if (defaultWebsites.size() == 0)
        {
            errors.add(MobiUwbApp.getContext().getString(
                    R.string.error_not_existing_default_section));
        }
        else
        {
            defaultWebsite = defaultWebsites.get(0);
        }

        if (websites.size() == 0)
        {
            errors.add(MobiUwbApp.getContext().getString(
                    R.string.error_sections_not_exists));
        }

        if(configurationFilePath == null || configurationFilePath.length() < 0)
        {
            errors.add(MobiUwbApp.getContext().getString(
                    R.string.error_sections_not_exists));
        }

        PropertiesXmlResult result =
                new PropertiesXmlResult(
                        new XmlPropertiesRootElement(
                                defaultWebsite,
                                websites,
                                configurationFilePath)
                );

        if (errors.size() != 0)
        {
            result.setValid(false);
            result.errors = errors;
        }
        Log.d("MOBIUWB", result.toString());
        return result;
    }
}
