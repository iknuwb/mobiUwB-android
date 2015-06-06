package pl.edu.uwb.mobiuwb.view.about;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.configuration.StartupConfig;
import pl.edu.uwb.mobiuwb.utillities.ListViewHacks;
import pl.edu.uwb.mobiuwb.view.about.adapter.AuthorsAdapter;

import java.util.List;

/**
 * Created by sennajavie on 2015-05-15.
 */
public class AboutActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        ListView listview = (ListView)findViewById(R.id.authors_list_view);
        List<String> stringList = StartupConfig.configXmlResult.authors;
        AuthorsAdapter authorsAdapter = new AuthorsAdapter(
                this,
                R.layout.round_button, stringList);
        listview.setAdapter(authorsAdapter);
        ListViewHacks.setListViewHeightBasedOnChildren(listview);
    }
}
