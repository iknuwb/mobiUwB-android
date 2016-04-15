package pl.edu.uwb.mobiuwb.interfaces;

import android.content.SharedPreferences;

/**
 * Opisuje obiekt, który może zostać zserializowany.
 */
public interface Storable<T>
{
    /**
     * Serializuje obiekt za pomocą preferencji Androida.
     * @param sharedPreferences Preferencje potrzebne do serializacji.
     */
    void store(SharedPreferences.Editor sharedPreferences);

    /**
     * Deserialiyuje obiekt za pomocą preferencji Androida.
     * @param sharedPreferences Preferencje potrzebne do deserializacji.
     */
    void restore(SharedPreferences sharedPreferences);

}
