package pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog;

import android.os.Parcel;

import pl.edu.uwb.mobiuwb.controls.dialogs.DialogCreator;
import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 * Ten model odpowiada za model Dialoga.
 */
public abstract class DialogItemModel<T> extends ItemModel
{
    /**
     * ID kontrolki z tekstem.
     */
    private int textControlId;

    /**
     * Pobiera ID kontrolki z tekstem.
     * @return ID kontrolki z tekstem.
     */
    public int getTextControlId()
    {
        return textControlId;
    }

    /**
     * Nadaje ID kontrolki z tekstem.
     * @param textControlId ID kontrolki z tekstem.
     */
    public void setTextControlId(int textControlId)
    {
        this.textControlId = textControlId;
    }

    /**
     * Wartość z dialoga.
     */
    protected T value;

    /**
     * Pobiera wartość z dialoga.
     * @return Wartość z dialoga.
     */
    public T getValue()
    {
        return value;
    }

    /**
     * Nadaje wartość z dialoga.
     * Wywołuje wydarzenia związane ze zmianą tej wartości.
     * @param value Wartość z dialoga.
     */
    public void setValue(T value)
    {
        if (valueChangeListeners != null && !this.value.equals(value))
        {
            for (OnValueChangedListener valueChange : valueChangeListeners)
            {
                valueChange.notifyChanged(this.value, value);
            }
        }
        this.value = value;
    }

    /**
     * Tytuł.
     */
    private String title;

    /**
     * Pobiera tytuł.
     * @return Tytuł.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Nadaje tytuł.
     * @param title Tytuł.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * ładnie wypisuje wartość.
     * @param value Wartość.
     * @return ładnie wypisana wartość.
     */
    public abstract String prettyPrintValue(T value);


    /**
     * ładnie wypisuje wartość.
     * @return ładnie wypisana wartość.
     */
    public String prettyPrintValue()
    {
        return prettyPrintValue(value);
    }

    /**
     * Nasłuchiwacze na zmianę wartości.
     * Wydarzają się, gdy zmieni się wartość w okienku dialogowym.
     */
    private List<OnValueChangedListener> valueChangeListeners;

    /**
     * Nadaje nowy nasłuchiwacz na zmianę wartości.
     * @param valueChange Nowy nasłuchiwacz.
     */
    public void setOnValueChangeListener(OnValueChangedListener valueChange)
    {
        if (valueChangeListeners == null)
        {
            valueChangeListeners = new ArrayList<OnValueChangedListener>();
        }
        valueChangeListeners.add(valueChange);
    }

    /**
     * Strategia tworzenia dialogów.
     * Jej podklasy enkapsulują zasady tworzenia dialogów.
     */
    protected DialogCreator dialogCreationStrategy;

    /**
     * Nadaje zmienne w klasie.
     * @param dialogCreationStrategy Strategia tworzenia dialogów.
     */
    public DialogItemModel(DialogCreator dialogCreationStrategy)
    {
        super();
        this.dialogCreationStrategy = dialogCreationStrategy;
        this.dialogCreationStrategy.setModel(this);
    }

    /**
     * Tworzy nową strategię tworzenia dialogów.
     * Jej podklasy enkapsulują zasady tworzenia dialogów.
     * @return
     */
    @Override
    public Item createStrategy()
    {
        return new DialogItem(this, dialogCreationStrategy);
    }

    /**
     * {@inheritDoc}
     */
    @Override public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeValue(value);
    }
}
