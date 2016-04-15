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
 * Jest to widok, w którym widnieją różne informacje o aplikacji: lista
 * autorów, licencja, podziękowania.
 */
public class AboutActivity extends Activity
{
    /**
     * Wydarza się gdy Android tworzy ten widok.
     * Wydarzenie to nadaje widokowi wygląd z XML.
     * @param savedInstanceState Zapisany stan widoku.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * Następuje po utworzeniu danego widoku.
     * Pobiera ListView autorów, wypełnia ją adapterem.
     * Nadaje jej poprawną wysokość.
     * @param savedInstanceState Zapisany stan widoku.
     */
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
