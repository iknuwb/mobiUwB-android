package pl.edu.uwb.mobiuwb.interfaces;

import android.content.SharedPreferences;

/**
 * Created by Tunczyk on 2015-05-01.
 */
public interface Storable<T>
{
    void store(SharedPreferences.Editor sharedPreferences);

    void restore(SharedPreferences sharedPreferences);

}
