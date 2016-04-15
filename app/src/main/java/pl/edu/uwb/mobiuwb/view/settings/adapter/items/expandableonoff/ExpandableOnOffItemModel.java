package pl.edu.uwb.mobiuwb.view.settings.adapter.items.expandableonoff;

import android.os.Parcel;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

/**
 * {@inheritDoc}
 * Jest to model kontrolki odpowiedzialnej za rozszerzenie podgrupy.
 */
public class ExpandableOnOffItemModel extends ItemModel
{
    /**
     * Tworzy obiekt.
     */
    public ExpandableOnOffItemModel()
    {
    }

    /**
     * Stwierdza, czy obsługiwana podgrupa jest rozszerzona czy zwinięta.
     */
    private boolean expanded;

    /**
     * Stwierdza, czy obsługiwana podgrupa jest rozszerzona czy zwinięta.
     * @return Czy podgrupa jest rozszerzona czy zwinięta.
     */
    public boolean isExpanded()
    {
        return expanded;
    }

    /**
     * Nadaje wartość, która stwierdza, czy obsługiwana podgrupa jest rozszerzona czy zwinięta.
     * @param expanded Czy podgrupa jest rozszerzona czy zwinięta.
     */
    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int initLayout()
    {
        return R.layout.settings_adapter_expandable_onoff_item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item createStrategy()
    {
        return new ExpandableOnOffItem(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (expanded ? 1 : 0));
    }
}
