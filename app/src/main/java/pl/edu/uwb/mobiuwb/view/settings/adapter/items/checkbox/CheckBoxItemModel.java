package pl.edu.uwb.mobiuwb.view.settings.adapter.items.checkbox;

import android.os.Parcel;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

/**
 * {@inheritDoc}
 * Ten model odpowiada za kontrolkę z CheckBoxem.
 */
public class CheckBoxItemModel extends ItemModel
{
    /**
     * Czy zaznaczono.
     */
    private boolean checked;

    /**
     * Czy zaznaczono.
     */
    public boolean isChecked()
    {
        return checked;
    }

    /**
     * Nadaje, że zaznaczono lub nie.
     * @param checked Czy zaznaczono.
     */
    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    /**
     * Identyfikator.
     */
    private String id;

    /**
     * Pobiera identyfikator.
     * @return Identyfikator.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Nadaje identyfikator.
     * @param id Identyfikator.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int initLayout()
    {
        return R.layout.settings_adapter_checkbox_item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item createStrategy()
    {
        return new CheckBoxItem(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (checked ? 1 : 0));

    }
}
