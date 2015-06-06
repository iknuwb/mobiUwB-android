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
 * Created by sennajavie on 2015-05-15.
 */
public class ContactActivity extends Activity
{
    private TextView unitName;
    private TextView postalCodeAndCity;
    private TextView streetAndNumber;

    private LinearLayout emailButton;
    private TextView emailTextView;

    private LinearLayout call1Button;
    private TextView call1TextView;

    private LinearLayout call2Button;
    private TextView call2TextView;

    private LinearLayout faxButton;
    private TextView faxTextView;

    private LinearLayout mapButton;
    private TextView mapTextView;


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    @Override protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        init();
    }

    private void init()
    {
        initContactFields();

        initEmailElement();

        initPhone1Element();

        initPhone2Element();

        initFaxElement();

        initMapElement();
    }

    private void initMapElement()
    {
        mapButton = (LinearLayout)findViewById(R.id.map_button);
        mapTextView = (TextView)findViewById(R.id.map_text_view);
        mapTextView.setText(getString(R.string.contact_map_element_text));
        mapButton.setOnClickListener(mapOnClickListener);
    }

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

    private void initFaxElement()
    {
        faxButton = (LinearLayout)findViewById(R.id.fax_button);
        faxTextView = (TextView)findViewById(R.id.fax_text_view);
        faxTextView.setText(StartupConfig.configXmlResult.getCurrentUniversityUnit().getFax());
        faxButton.setOnClickListener(faxOnClickListener);
    }

    private View.OnClickListener faxOnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            call(StartupConfig.configXmlResult.getCurrentUniversityUnit().getFax());
        }
    };

    private void initPhone1Element()
    {
        call1Button = (LinearLayout)findViewById(R.id.call_1_button);
        call1TextView = (TextView)findViewById(R.id.call_1_text_view);
        call1TextView.setText(StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel1());
        call1Button.setOnClickListener(phone1OnClickListener);
    }

    private View.OnClickListener phone1OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            call(StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel1());
        }
    };

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

    private View.OnClickListener phone2OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            call(StartupConfig.configXmlResult.getCurrentUniversityUnit().getTel2());
        }
    };

    private void initEmailElement()
    {
        emailButton = (LinearLayout)findViewById(R.id.email_button);
        emailTextView = (TextView)findViewById(R.id.email_text_view);
        emailTextView.setText(StartupConfig.configXmlResult.getCurrentUniversityUnit().getEmail());

        emailButton.setOnClickListener(emailOnClickListener);
    }
    private View.OnClickListener emailOnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            String[] adresses = new String[1];
            adresses[0] = StartupConfig.configXmlResult.getCurrentUniversityUnit().getEmail();
            composeEmail(adresses,"");
        }
    };

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

    public void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

}
