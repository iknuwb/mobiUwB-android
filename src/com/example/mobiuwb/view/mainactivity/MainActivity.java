package com.example.mobiuwb.view.mainactivity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobiuwb.MobiUwbApp;
import com.example.mobiuwb.R;
import com.example.mobiuwb.utillities.ActionBarMethods;
import com.example.mobiuwb.utillities.DrawerListMethods;
import com.example.mobiuwb.utillities.MenuMethods;
import com.example.mobiuwb.view.adapter.DrawerListAdapter;
import com.example.mobiuwb.view.interfaces.GuiAccess;
import com.example.mobiuwb.view.mainactivity.fragment.MainActivityFragment;
import com.example.mobiuwb.view.model.DrawerListAdapterModel;
import com.example.mobiuwb.xml.model.Website;
import com.example.mobiuwb.xml.parser.XMLParser;
import com.example.mobiuwb.xml.parser.result.model.PropertiesXmlResult;

public class MainActivity extends Activity implements GuiAccess
{
	public ActionBarDrawerToggle drawerToggle;

	private MainActivityFragment fragment;
	
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private LinearLayout drawerLinearLayout;
	private int currentSelectedPosition;
	public PropertiesXmlResult propertiesXmlResult;
	private long backPressedTime;

	private XMLParser xmlParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		forceShowMenuButton();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if (drawerToggle.onOptionsItemSelected(item)) 
        {
        	return true;
        }
		MenuMethods.menuOptions(id, this);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){        
	    super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed()
	{
		String ping = propertiesXmlResult.
				getXmlPropertiesRootElement().
				getDefaultWebsite().
				getPing();
		
		if(fragment.getCurrentUrl() == ping)
		{
			if (backPressedTime + 2000 > System.currentTimeMillis()) 
			{
				super.onBackPressed();
			}
	        else 
        	{
        		Toast.makeText(
        				getBaseContext(), 
        				getResources().
        				getString(
        						R.string.toast_press_again_to_close),
        				Toast.LENGTH_SHORT).
        				show();
        	}
			backPressedTime = System.currentTimeMillis();
		}
		else
		{
			fragment.goBackInWebView();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
			{
				String pingUrl = propertiesXmlResult.
						getXmlPropertiesRootElement().
						getDefaultWebsite().
						getPing();
				if(fragment.getCurrentUrl().equals(pingUrl) == false)
				{
					fragment.goBackInWebView();
					return true;
				}
				if(backPressedTime + 2000 > System.currentTimeMillis())
				{
					finish();
				}
				else
				{
					Toast.makeText(
							this,
							getString(
									R.string.toast_press_again_to_close), 
							Toast.LENGTH_SHORT)
							.show();
				}
				backPressedTime = System.currentTimeMillis();
				return true;
			}
			default:
			{
				return super.onKeyDown(keyCode, event);
			}
		}
	}
	
	private void setDefaults()
	{
		String title = propertiesXmlResult.
				getXmlPropertiesRootElement().
				getDefaultWebsite().
				getName();
		
		ActionBarMethods.setActionBarTitle(this, title);
		

		
		fragment = new MainActivityFragment();
		
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_container_relativelayout, fragment)
				.commit();
	}
	
	private void init()
	{
		initControls();
		initNavigationDrawer();
		setDefaults();
	}
	
	private void initControls()
	{
		drawerLayout = (DrawerLayout)findViewById(
				R.id.drawer_layout);
		drawerListView = (ListView)findViewById(
				R.id.drawer_listview);
		drawerLinearLayout = (LinearLayout)findViewById(
				R.id.drawer_linearlayout);
	}
	
	private void initNavigationDrawer()
	{
		
		xmlParser = new XMLParser();
		try {
			propertiesXmlResult = xmlParser.deserializePropertiesXml();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<DrawerListAdapterModel> list = new ArrayList<DrawerListAdapterModel>();
		for (Website item : propertiesXmlResult.getXmlPropertiesRootElement().websites) 
		{
			list.add(new DrawerListAdapterModel(
					getResources().getDrawable(R.drawable.logouwb), 
					item, 0));
		}
		
		currentSelectedPosition = propertiesXmlResult.
				getXmlPropertiesRootElement().
				getDefaultWebsiteIndex();
		
		if(currentSelectedPosition == -1)
		{
			currentSelectedPosition = 0;
		}
		
		final DrawerListAdapter drawerListAdapter = new DrawerListAdapter(this, list);
		
		drawerListView.setAdapter(drawerListAdapter);
		drawerListView.setItemChecked(
				currentSelectedPosition, 
				true);
		drawerListView.setOnItemClickListener(
			new AdapterView.OnItemClickListener() 
			{
				@Override
				public void onItemClick(
						AdapterView<?> parent, 
						View view,
						int position, 
						long id) 
				{
					currentSelectedPosition = position;
					if (drawerListView != null) 
					{
						drawerListView.setItemChecked(position, true);
					}
					if (drawerLayout != null) 
					{
						drawerLayout.closeDrawer(drawerLinearLayout);
					}
					if (drawerListView != null) 
					{
						DrawerListAdapterModel mdl = (DrawerListAdapterModel)
								drawerListView.getAdapter().getItem(position);
						
						
						try
						{
							xmlParser.serializeDefaultWebsite(mdl.getWebsite());
							propertiesXmlResult.
							getXmlPropertiesRootElement().
							setDefaultWebsite(mdl.getWebsite());
						}
						catch(IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch(ParserConfigurationException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch(SAXException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch(TransformerException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DrawerListMethods.onItemSelected(mdl);
						
						fragment.loadUrl(mdl.getWebsite().getUrl());
						ActionBarMethods.setActionBarTitle(MainActivity.this, mdl.getText());
					}
				}
			});
		
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		drawerToggle = new ActionBarDrawerToggle(
				this, 
				drawerLayout, 
				R.drawable.ic_drawer, 
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close
				) 
		{
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				/*if (!mUserLearnedDrawer) {
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(MainActivity.this);
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.apply();
				}

				invalidateOptionsMenu();
				*/
			}
		};
		/*
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}
		*/
		drawerLayout.post(new Runnable() {
			@Override
			public void run() {
				drawerToggle.syncState();
			}
		});

		drawerLayout.setDrawerListener(drawerToggle);
	}
	
	/**
	 * Metoda wymuszająca na androidzie aby wyświetlił on menu button.
	 */
	private void forceShowMenuButton() 
	{
		try 
		{
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) 
			{
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} 
		catch (Exception ex) 
		{
			// Ignore
		}
	}

	@Override
	public void finishActivity()
	{
		this.finish();
	}

	@Override
	public void refreshWebView()
	{
		fragment.refreshWebView();
	}

}
