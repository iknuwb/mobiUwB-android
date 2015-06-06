package pl.edu.uwb.mobiuwb.view.settings.adapter.items.checkbox;

import android.os.Parcel;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public class CheckBoxItemModel extends ItemModel
{
    private boolean checked;
    public boolean isChecked()
    {
        return checked;
    }
    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    private String id;
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }


    @Override
    protected int initLayout()
    {
        return R.layout.settings_adapter_checkbox_item;
    }

    @Override
    public Item createStrategy()
    {
        return new CheckBoxItem(this);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (checked ? 1 : 0));

    }

}
