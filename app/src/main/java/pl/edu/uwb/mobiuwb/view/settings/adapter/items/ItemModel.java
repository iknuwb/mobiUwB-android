package pl.edu.uwb.mobiuwb.view.settings.adapter.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public abstract class ItemModel implements Parcelable
{

    public List<ItemModel> nestedModels;

    private String text;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    private int layout;

    public int getLayout()
    {
        return layout;
    }

    protected ItemModel()
    {
        layout = initLayout();
        nestedModels = new ArrayList<ItemModel>();
    }


    protected abstract int initLayout();

    public abstract Item createStrategy();

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeList(nestedModels);
        dest.writeString(text);
        dest.writeInt(layout);
    }
}
