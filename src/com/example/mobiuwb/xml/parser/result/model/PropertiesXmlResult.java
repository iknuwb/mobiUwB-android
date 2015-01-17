package com.example.mobiuwb.xml.parser.result.model;

import java.util.List;

import com.example.mobiuwb.xml.model.XmlPropertiesRootElement;

public class PropertiesXmlResult 
{
	private boolean valid;
	public boolean isValid() 
	{
		return valid;
	}
	public void setValid(boolean valid) 
	{
		this.valid = valid;
	}
	
	private XmlPropertiesRootElement xmlPropertiesRootElement;
	public XmlPropertiesRootElement getXmlPropertiesRootElement() 
	{
		return xmlPropertiesRootElement;
	}
	public void setXmlPropertiesRootElement(
			XmlPropertiesRootElement xmlPropertiesRootElement) 
	{
		this.xmlPropertiesRootElement = xmlPropertiesRootElement;
	}
	
	public List<String> errors;
	
	public PropertiesXmlResult(
			XmlPropertiesRootElement xmlPropertiesRootElement) 
	{
		this.valid = true;
		this.xmlPropertiesRootElement = xmlPropertiesRootElement;
	}
	
	@Override
	public String toString() {
		return "PropertiesXmlResult [valid=" + valid
				+ ", xmlPropertiesRootElement=" + xmlPropertiesRootElement
				+ ", errors=" + errors + "]";
	}
}
