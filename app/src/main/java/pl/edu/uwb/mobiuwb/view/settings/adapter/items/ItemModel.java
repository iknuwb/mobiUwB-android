package pl.edu.uwb.mobiuwb.view.settings.adapter.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Jest to abstrakcyjny model elementu w ekranie Opcji/Ustawień.
 */
public abstract class ItemModel implements Parcelable
{
    /**
     * Zagnieżdżone modele.
     */
    public List<ItemModel> nestedModels;

    /**
     * Widoczny tekst.
     */
    private String text;

    /**
     * Pobiera widoczny tekst.
     * @return Widoczny tekst.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Nadaje widoczny tekst.
     * @param text Widoczny tekst.
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * ID layoutu z XML.
     */
    private int layout;

    /**
     * Pobiera ID layoutu z XML.
     * @return ID layoutu z XML.
     */
    public int getLayout()
    {
        return layout;
    }

    /**
     * Inicjuje pola.
     * Wywołuje inicjalizację layoutu z XML.
     */
    protected ItemModel()
    {
        layout = initLayout();
        nestedModels = new ArrayList<ItemModel>();
    }

    /**
     * Inicjuje layout z XML.
     * @return Layout z XML.
     */
    protected abstract int initLayout();

    /**
     * Tworzy element graficzny odpowiadający
     * temu modelowi.
     * @return Element graficzny widoku.
     */
    public abstract Item createStrategy();

    /**
     * Pobiera tekstową reprezentację tego obiektu.
     * Zrozumiałą dla użytkownika.
     * @return Tekstowa reprezentacja tego obiektu.
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" +
                "text='" + text + '\'' +
                '}';
    }

    /**
     * Opisuje specjalne typy umieszczone w tym obiekcie.
     * @return Bitowy opis specjalnych typów.
     */
    @Override
    public int describeContents()
    {
        return 0;
    }

    /**
     * Metoda ta wywołuje się gdy system chce
     * zserializować ten obiekt.
     * Metoda serializuje niezbędne elementy tej klasy.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeList(nestedModels);
        dest.writeString(text);
        dest.writeInt(layout);
    }
}
