package pl.edu.uwb.mobiuwb.view.somethingWrong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.splash.SplashScreenActivity;

/**
 * Jest to ekran występujący wtedy, gdy stanie się
 * coś nie tak w aplikacji.
 * Aktualnie skonfigurowany jest pod brak połaczenia
 * z Internetem w krytycznym momencie:
 * czyli przy pierwszym uruchomieniu aplikacji na
 * urządzeniu.
 */
public class SomethingWrongActivity extends Activity
{
    /**
     * Kontener na odświeżacz.
     */
    private LinearLayout refreshLinearLayout;

    /**
     * Wydarza się gdy aplikacja tworzy ten widok.
     * Nadaje layout z XML.
     * @param savedInstanceState Zapisany stan widoku.
     *                           Na wypadek ponownej generacji.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_something_wrong);
    }

    /**
     * Wydarza się po utworzeniu widoku.
     * Wywołuje inicjalizację widoku.
     * @param savedInstanceState Zapisany stan widoku.
     *                           Na wypadek ponownej generacji.
     */
    @Override protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        initialize();
    }

    /**
     * Inicjalizuje widok, w tym kontrolki.
     */
    private void initialize()
    {
        initRefreshLinearLayout();
    }

    /**
     * Inicjalizuje odświeżacz czyli kontener z onclickiem
     * z efektem odświeżenia.
     */
    private void initRefreshLinearLayout()
    {
        refreshLinearLayout = (LinearLayout)findViewById(
                R.id.refresh_linearlayout);

        refreshLinearLayout.setOnClickListener(
                new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                final Intent intent = new Intent(
                        SomethingWrongActivity.this,
                        SplashScreenActivity.class);
                startActivity(intent);
            }
        });
    }
}
