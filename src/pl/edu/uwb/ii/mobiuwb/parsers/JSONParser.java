package pl.edu.uwb.ii.mobiuwb.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.edu.uwb.ii.mobiuwb.models.JSONNotificationModel;
import android.util.Log;


public class JSONParser
{
	private static final String TAG_TITLE = "tytul";
	private static final String TAG_CONTENT = "tresc";
	private static final String TAG_DATE = "data";
	private static final String TAG_BLAD = "blad";
	
	private static InputStream is = null;
	private static JSONArray jObj = null;
	private static String json = "";
	private static String URL;
	
	public ArrayList<JSONNotificationModel> JSONModelList;
	
	
	public JSONParser(String URL)
	{
		JSONParser.URL = URL;
		JSONModelList = new ArrayList<JSONNotificationModel>();
		
		getJSONFromUrl();
	}
	
	
	public void parseJSON()
	{
		JSONArray contentArray = null;
		JSONNotificationModel model;
		String content;
		String title;
		String dateText;
		Date date = null;
		SimpleDateFormat sdf;
		
		try
		{
			contentArray = new JSONArray(jObj.toString());
			
			
			
			for(int i = 0; i < contentArray.length(); i++)
			{
				JSONObject c = contentArray.getJSONObject(i);
				
				title = c.getString(TAG_TITLE);
				content = c.getString(TAG_CONTENT);
				dateText = c.getString(TAG_DATE);
				
				sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				try
				{
					date = sdf.parse(dateText);
				}
				catch(ParseException e)
				{
					e.printStackTrace();
				}
				
				if(title != "" && title != null)
				{
					model = new JSONNotificationModel(title, content, date);
				}
				else
				{
					model = new JSONNotificationModel(content, date);
				}
				JSONModelList.add(model);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void getJSONFromUrl()
	{
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch(ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
																				is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		}
		catch(Exception e)
		{
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		
		try
		{
			jObj = new JSONArray(json);
		}
		catch(JSONException e)
		{
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
	}
}
