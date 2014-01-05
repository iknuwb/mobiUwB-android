package pl.edu.uwb.ii.mobiuwb.activities;

import java.util.ArrayList;
import pl.edu.uwb.ii.mobiuwb.R;
import pl.edu.uwb.ii.mobiuwb.models.WebsiteModel;
import pl.edu.uwb.ii.mobiuwb.parsers.XMLParser;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Jest to klasa Activity odpowiadającego za ekran wyboru strony.
 */
public class WebsiteChooseActivity extends Activity
{
	
	/**
	 * Jest to główna metoda tego okna Activity. Odpowiada ona za utworzenie go
	 * i zainicjalizowanie elementów.
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_website_choose);
		this.setTitle("MobiUwB");
		final ArrayList<WebsiteModel> temp = new ArrayList<WebsiteModel>();
		for(WebsiteModel item : MainActivity.webSites)
		{
			if(item.isValid())
			{
				temp.add(item);
			}
		}
		final ListView lv = (ListView)this.findViewById(R.id.listView1);
		ArrayAdapter<WebsiteModel> aa = new ArrayAdapter<WebsiteModel>(
																		getBaseContext(),
																		android.R.layout.simple_list_item_single_choice, temp);
		lv.setAdapter(aa);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		int pos = findPosition(temp, MainActivity.webMod);
		if(pos != -1)
		{
			lv.setItemChecked(pos, true);
		}
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				MainActivity.webMod = (WebsiteModel)temp.get(arg2);
				finish();
			}
			
		});
	}
	
	
	/**
	 * Metoda ta wywołuje się, gdy cofnie się ekran do poprzednika. Nadaje ona
	 * nową domyślną stronę WWW.
	 */
	@Override
	protected void onStop()
	{
		super.onStop();
		XMLParser.setNewDefaultWebsite(MainActivity.webMod);
	}
	
	
	/**
	 * Metoda zajmuj�ca si� przeszukiwaniem listy w poszukiwaniu konkretnego
	 * elementu.
	 * 
	 * @param currentList
	 *            lista do przeszukania
	 * @param searchedElement
	 *            element do znalezienia
	 * @return -1, je�eli nie znalaz�o, w przeciwnym wypadku pozycja na podanej
	 *         li�cie.
	 */
	private int findPosition(ArrayList<WebsiteModel> currentList,
			WebsiteModel searchedElement)
	{
		for(int i = 0; i < currentList.size(); i++)
		{
			if(currentList.get(i).getURL()
					.equals(MainActivity.webMod.getURL()))
			{
				return i;
			}
		}
		return -1;
	}
}
