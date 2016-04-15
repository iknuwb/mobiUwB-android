package pl.edu.uwb.mobiuwb.utillities;

import android.util.DisplayMetrics;

import pl.edu.uwb.mobiuwb.MobiUwbApp;

/**
 * Ma w sobie metody konwertujące różne wartości na inne.
 */
public class UnitConverter
{
    /**
     * Zawiera właściwości związane z ekranem urządzenia.
     */
    public static DisplayMetrics displayMetrics;

    /**
     * Ten inicjalizator inicjuje zmienne w klasie.
     */
    static
    {
        displayMetrics = MobiUwbApp.getContext().getResources().getDisplayMetrics();
    }

    /**
     * Przekształca pixele na dpi.
     * @param px Pixele.
     * @return Dpi.
     */
    public static int toDpi(int px)
    {
        return (int) ((px * displayMetrics.density) + 0.5);
    }

    /**
     * Przekształca dpi na pixele.
     * @param dp Dpi.
     * @return Pixele.
     */
    public static int toPx(int dp)
    {
        return (int) ((dp / displayMetrics.density) + 0.5);
    }
}
