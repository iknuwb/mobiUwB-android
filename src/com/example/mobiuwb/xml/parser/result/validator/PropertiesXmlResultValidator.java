package com.example.mobiuwb.xml.parser.result.validator;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.mobiuwb.MobiUwbApp;
import com.example.mobiuwb.R;
import com.example.mobiuwb.xml.model.Website;
import com.example.mobiuwb.xml.model.XmlPropertiesRootElement;
import com.example.mobiuwb.xml.parser.result.model.PropertiesXmlResult;

public class PropertiesXmlResultValidator 
{
	
	public PropertiesXmlResult validate(List<Website> defaultWebsites,
			List<Website> websites)
	{
		Website defaultWebsite = null;
		
		List<String> errors = new ArrayList<String>();
		if(defaultWebsites.size() > 1)
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
		
		if(websites.size() == 0)
		{
			errors.add(MobiUwbApp.getContext().getString(
					R.string.error_sections_not_exists));
		}
		
		PropertiesXmlResult result = 
				new PropertiesXmlResult(
						new XmlPropertiesRootElement(
								defaultWebsite, 
								websites)
						);
		
		if(errors.size() != 0)
		{
			result.setValid(false);
			result.errors = errors;
		}
		Log.d("MOBIUWB",result.toString());
		return result;
	}
}
