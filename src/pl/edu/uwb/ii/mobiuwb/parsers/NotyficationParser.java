package pl.edu.uwb.ii.mobiuwb.parsers;

import android.util.Log;


public class NotyficationParser
{
	private static final String ENCJA_AND = "&amp;";
	private static final String TEXT_AND = "&";
	
	private static final String ENCJA_LT = "&lt;";
	private static final String TEXT_LT = "<";
	
	private static final String ENCJA_GT = "&gt;";
	private static final String TEXT_GT = ">";
	
	private static final String ENCJA_QUOT = "&quot;";
	private static final String TEXT_QUOT = "\"";
	
	private static final String ENCJA_APOS = "&apos;";
	private static final String TEXT_APOS = "\'";
	
	
	public static String prepareTextToNotification(String textToClear)
	{
		textToClear = clearHTMLTags(textToClear);
		textToClear = replaceEntitiesToText(textToClear);
		return textToClear;
	}
	
	
	private static String replaceEntitiesToText(String content)
	{
		if(content != null && content != "")
		{
			content = content.replace(ENCJA_AND, TEXT_AND);
			content = content.replace(ENCJA_LT, TEXT_LT);
			content = content.replace(ENCJA_GT, TEXT_GT);
			content = content.replace(ENCJA_QUOT, TEXT_QUOT);
			content = content.replace(ENCJA_APOS, TEXT_APOS);
		}
		return content;
	}
	
	
	private static String clearHTMLTags(String content)
	{
		int openTagSign;
		int closeTagSign;
		
		if(content != null && content != "")
		{
			do
			{
				openTagSign = content.indexOf(TEXT_LT);
				closeTagSign = content.indexOf(TEXT_GT);
				
				if(openTagSign != -1 && closeTagSign != -1)
				{
					content = deleteStringFragment(openTagSign, closeTagSign,
							content);
				}
				else if((openTagSign == -1 || closeTagSign == -1)
						&& openTagSign != closeTagSign)
				{
					Log.d("DEBUG",
							"Kto� �le zapisa� tagi i sie nie zgadzaj� si� ilo�ci: <, >");
					break;
				}
			}
			while(openTagSign != -1 && closeTagSign != -1);
		}
		return content;
	}
	
	
	private static String deleteStringFragment(int start, int end,
			String content)
	{
		if(content != null && content != "")
		{
			String partOne = content.substring(0, start);
			String partTwo = content.substring(end + 1, content.length());
			return partOne + partTwo;
		}
		else
		{
			return null;
		}
	}
}
