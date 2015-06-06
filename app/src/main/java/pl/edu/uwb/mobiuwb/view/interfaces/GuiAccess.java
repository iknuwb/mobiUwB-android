package pl.edu.uwb.mobiuwb.view.interfaces;

import android.content.Context;
import android.content.Intent;

public interface GuiAccess
{
    //TODO do wywalenia
    void finishActivity();

    void refreshWebView();

    void startActivityAccess(Intent intent);

    void startActivityForResultAccess(Intent intent, int RequestCode);

    Context getContextAccess();
}
