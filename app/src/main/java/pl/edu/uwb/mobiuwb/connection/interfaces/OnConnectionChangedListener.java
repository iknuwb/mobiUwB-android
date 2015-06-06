package pl.edu.uwb.mobiuwb.connection.interfaces;

import pl.edu.uwb.mobiuwb.connection.CheckingType;

/**
 * Created by Tunczyk on 2015-05-05.
 */
public interface OnConnectionChangedListener
{
    void connectionChanged(CheckingType checkingType);
}
