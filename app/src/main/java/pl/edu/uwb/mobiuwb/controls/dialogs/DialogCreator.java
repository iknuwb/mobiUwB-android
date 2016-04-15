package pl.edu.uwb.mobiuwb.controls.dialogs;

import android.app.DialogFragment;

import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;


/**
 * Jest to twórca okienek dialogowych.
 */
public abstract class DialogCreator<T>
{
    /**
     * Reprezentuje instancję modelu w ekranie Opcji, który to jest
     * odpowiedzialny za funkcjonalność wyświetlenia okienka.
     */
    protected DialogItemModel model;


    /**
     * Pobiera instancję modelu pochodzącego z ekranu Opcji,
     * który to jest odpowiedzialny za
     * funkcjonalność wyświetlenia okienka.
     * @return Instancja modelu pochodzącego z ekranu Opcji.
     */
    public DialogItemModel getModel()
    {
        return model;
    }

    /**
     * Nadaje instancję modelu pochodzącego z ekranu Opcji,
     * który to jest odpowiedzialny za
     * funkcjonalność wyświetlenia okienka.
     * @param model Instancja modelu pochodzącego z ekranu Opcji.
     */
    public void setModel(DialogItemModel model)
    {
        this.model = model;
    }

    /**
     * Wydarza się wtedy, gdy zmieni się wartość, nadana z okienka.
     */
    OnValueChangedListener valueChange;

    /**
     * Nadaje nasłuchiwacz na zmienioną wartość po wyborze z okienka.
     * @param valueChange Nasłuchiwacz na wartość zmienioną po wyborze z okienka.
     */
    public void setOnValueChangedListener(OnValueChangedListener valueChange)
    {
        this.valueChange = valueChange;
    }

    /**
     * Tworzy instancję.
     */
    protected DialogCreator()
    {
    }

    /**
     * Metoda ta jest wywoływana, gdy chcemy utworzyć Fragment dialoga.
     * @return Fragment, który posłuży do utworzenia dialoga.
     */
    public abstract DialogFragment create();
}
