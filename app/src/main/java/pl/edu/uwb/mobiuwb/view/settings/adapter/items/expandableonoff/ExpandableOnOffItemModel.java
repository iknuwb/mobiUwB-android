package pl.edu.uwb.mobiuwb.view.settings.adapter.items.expandableonoff;

import android.os.Parcel;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public class ExpandableOnOffItemModel extends ItemModel
{
    public ExpandableOnOffItemModel()
    {
    }

    private boolean expanded;

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }


    @Override
    protected int initLayout()
    {
        return R.layout.settings_adapter_expandable_onoff_item;
    }

    @Override
    public Item createStrategy()
    {
        return new ExpandableOnOffItem(this);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (expanded ? 1 : 0));
    }
}
