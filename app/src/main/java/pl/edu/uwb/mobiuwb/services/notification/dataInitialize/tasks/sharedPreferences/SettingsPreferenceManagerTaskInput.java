package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.sharedPreferences;

import android.content.Context;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * {@inheritDoc}
 * Są to dane wejściowe dla zadanie związanego z pozyskiwaniem informacji z
 * preferencji ekranu Ustawień/Opcji.
 */
public class SettingsPreferenceManagerTaskInput extends TaskInput
{
    /**
     * Kontekst aplikacji bądź też widoku.
     */
    private Context baseContext;

    /**
     * Zwraca kontekst aplikacji bądź też widoku.
     * @return Kontekst aplikacji bądź też widoku.
     */
    public Context getBaseContext()
    {
        return baseContext;
    }

    /**
     * Nadaje pola.
     * @param baseContext Kontekst aplikacji bądź też widoku.
     */
    public SettingsPreferenceManagerTaskInput(Context baseContext)
    {
        this.baseContext = baseContext;
    }
}
