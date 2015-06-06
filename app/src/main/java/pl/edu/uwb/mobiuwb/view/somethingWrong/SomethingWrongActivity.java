package pl.edu.uwb.mobiuwb.view.somethingWrong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.splash.SplashScreenActivity;

public class SomethingWrongActivity extends Activity
{
    private LinearLayout refreshLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_something_wrong);
    }

    @Override protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        initialize();
    }

    private void initialize()
    {
        initRefreshLinearLayout();
    }

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
