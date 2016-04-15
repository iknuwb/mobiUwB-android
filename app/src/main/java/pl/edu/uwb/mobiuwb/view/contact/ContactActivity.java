package pl.edu.uwb.mobiuwb.view.contact;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.configuration.StartupConfig;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.Coordinates;

/**
 * Jest to widok przedstawiający informacje kontaktowe.
 */
public class ContactActivity extends Activity
{
    /**
     * Kontrolka z nazwą jednostki.
     */
    private TextView unitName;

    /**
     * Kontrolka z kodem pocztowym i miastem.
     */
    private TextView postalCodeAndCity;

    /**
     * Kontrolka z ulicą i numerem.
     */
    private TextView streetAndNumber;

    /**
     * Przycisk uruchamiający e-mail.
     */
    private LinearLayout emailButton;

    /**
     * Kontrolka z e-mailem.
     */
    private TextView emailTextView;

    /**
     * Przycisk odpowiedzialny za telefon nr 1.
     */
    private LinearLayout call1Button;

    /**
     * Kontrolka z tekstem telefonu nr 1.
     */
    private TextView call1TextView;

    /**
     * Przycisk odpowiedzialny za telefon nr 2.
     */
    private LinearLayout call2Button;

    /**
     * Kontrolka z tekstem telefonu nr 1.
     */
    private TextView call2TextView;

    /**
     * Przycisk odpowiedzialny za fax.
     */
    private LinearLayout faxButton;

    /**
     * Kontrolka z tekstem fax.
     */
    private TextView faxTextView;

    /**
     * Przycisk odpowiedzialny za uruchomienie aplikacji mapa.
     */
    private LinearLayout mapButton;

    /**
     * Kontrolka z tekstem mapy.
     */
    private TextView mapTextView;


    /**
     * Wydarza się gdy Android tworzy ten widok.
     * Wydarzenie to nadaje widokowi wygląd z XML.
     * @param savedInstanceState Zapisany stan widoku.
     */
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    /**
     * Następuje po utworzeniu danego widoku.
     * Wywołuje metodę inicjalizacyjną.
     * @param savedInstanceState Zapisany stan widoku.
     */
    @Override protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        init();
    }

    /**
     * inicjalizuje kontrolki.
     */
    private void init()
    {
        initContactFields();

        initEmailElement();

        initPhone1Element();

        initPhone2Element();

        initFaxElement();

        initMapElement();
    }

    /**
     * Inicjalizuje kontrolki mapy.
     */
    private void initMapElement()
    {
        mapButton = (LinearLayout)findViewById(R.id.map_button);
        mapTextView = (TextView)findViewById(R.id.map_text_view);
        mapTextView.setText(getString(R.string.contact_map_element_text));
        mapButton.setOnClickListener(mapOnClickListener);
    }

    /**
     * Jest to wydarzenie kliknięcia na przycisk mapy.
     */
    View.OnClickListener mapOnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            Coordinates coords = StartupConfig.
                    configXmlResult.
                    getCurrentUniversityUnit().
                    getMobiUwBMap().
                    getCoordinates();
            Uri gmmIntentUri = Uri.parse("geo:"+coords.getLongtitude()+","+coords.getLattitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }
    };

    /**
     * Inicjalizuje kontrolki odpowiedzialne za fax.
     */
    private void initFaxElement()
    {
        faxButton = (LinearLayout)findViewById(R.id.fax_button);
        faxTextView = (TextView)findViewById(R.id.fax_text_view);
        faxTextView.setText(StartupConfig.configXmlResult.getCurrentUniversityUnit().getFax());
        faxButton.setOnClickListener(faxOnClickListener);
    }

    /**
     * Wydarza się gdy dotkniemy przycisku z faxem.
     * Uruchamia odpowiednią akcję w systemie Android.
     */
    private View.OnClickListener faxOnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            call(StartupConfig.configXmlResult.getCurrentUniversityUnit().getFax());
        }
    };

    /**
     * Inicjalizuje elementy odpowiedzialne za telefon 1.
     */
    private void initPhone1Element()
    {
        call1Button = (LinearLayout)findViewById(R.id.call_1_button);
        call1TextView = (TextView)findViewById(R.id.call_1_text_view);
        call1TextView.setText(StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel1());
        call1Button.setOnClickListener(phone1OnClickListener);
    }

    /**
     * Wydarza się gdy dotkniemy przycisku z telefonem 1.
     * Wykonuje się wtedy akcja otwarcia aplikacji do dzwonienia.
     */
    private View.OnClickListener phone1OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            call(StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel1());
        }
    };

    /**
     * Inicjalizuje elementy odpowiedzialne za telefon 2.
     */
    private void initPhone2Element()
    {
        if (StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel2() != null)
        {
            call2Button = (LinearLayout) findViewById(R.id.call_2_button);
            call2TextView = (TextView) findViewById(R.id.call_2_text_view);
            call2TextView.setText(StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel2());
            call2Button.setOnClickListener(phone2OnClickListener);
            call2Button.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Wydarza się gdy dotkniemy przycisku z telefonem 2.
     * Wykonuje się wtedy akcja otwarcia aplikacji do dzwonienia.
     */
    private View.OnClickListener phone2OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            call(StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel2());
        }
    };

    /**
     * Inicjalizuje elementy odpowiedzialne za e-mail.
     */
    private void initEmailElement()
    {
        emailButton = (LinearLayout)findViewById(R.id.email_button);
        emailTextView = (TextView)findViewById(R.id.email_text_view);
        emailTextView.setText(StartupConfig.configXmlResult.getCurrentUniversityUnit().getEmail());

        emailButton.setOnClickListener(emailOnClickListener);
    }

    /**
     * Wydarza się gdy dotkniemy przycisku z e-mailem.
     * Wykonuje się wtedy akcja otwarcia aplikacji do e-maili.
     */
    private View.OnClickListener emailOnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            String[] adresses = new String[1];
            adresses[0] = StartupConfig.configXmlResult.getCurrentUniversityUnit().getEmail();
            composeEmail(adresses,"");
        }
    };

    /**
     * Inicjuje pola odpowiedzialne za kontakt.
     */
    private void initContactFields()
    {
        unitName = (TextView)findViewById(R.id.unit_name);
        String unitNameString = StartupConfig.
                configXmlResult.
                getCurrentUniversityUnit().
                getName();
        unitName.setText(unitNameString);

        postalCodeAndCity = (TextView)findViewById(R.id.postal_code_and_city);
        String unitPostalCodeString = StartupConfig.
                configXmlResult.
                getCurrentUniversityUnit().
                getAddress().
                getPostalCode();
        String unitCityString = StartupConfig.
                configXmlResult.
                getCurrentUniversityUnit().
                getAddress().
                getCity();
        postalCodeAndCity.setText(unitPostalCodeString + " " + unitCityString);


        streetAndNumber = (TextView)findViewById(R.id.street_and_number);
        String unitStreetString = StartupConfig.
                configXmlResult.
                getCurrentUniversityUnit().
                getAddress().
                getStreet();
        String unitNumberString = StartupConfig.
                configXmlResult.
                getCurrentUniversityUnit().
                getAddress().
                getNumber();
        streetAndNumber.setText(unitStreetString + " " + unitNumberString);
    }

    /**
     * Otwiera aplikację odpowiedzialną za e-maile.
     * Nadaje jej adresy oraz temat.
     * @param addresses Adresy e-mail.
     * @param subject Temat.
     */
    public void composeEmail(String[] addresses, String subject)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Uruchamia aplikację odpowiedzialną za telefon.
     * Nadaje jej od razu wpisany telefon.
     * @param phone Numer telefonu.
     */
    public void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

}
