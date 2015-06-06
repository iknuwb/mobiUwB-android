package pl.edu.uwb.mobiuwb.parsers.json.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.edu.uwb.mobiuwb.parsers.json.model.Feed;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class JsonParser
{
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public List<Feed> parseFeedsJson(String fileContent)
        throws JSONException
    {
        List<Feed> feeds = new ArrayList<Feed>();

        JSONArray jsonArray = new JSONArray(fileContent);

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String title = jsonObject.getString("tytul");
            String content = jsonObject.getString("tresc");
            Date date = parseDate(jsonObject.getString("data"));
            feeds.add(new Feed(title,content,date));
        }
        return feeds;
    }

    private Date parseDate(String unparsedDate) throws JSONException
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try
        {
            date = simpleDateFormat.parse(unparsedDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }
}
