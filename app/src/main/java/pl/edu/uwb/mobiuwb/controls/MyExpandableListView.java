package pl.edu.uwb.mobiuwb.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import pl.edu.uwb.mobiuwb.utillities.UnitConverter;

/**
 * Created by Tunczyk on 2015-04-21.
 */
public class MyExpandableListView extends ExpandableListView
{
    public MyExpandableListView(Context context)
    {
        super(context);
    }

    public MyExpandableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyExpandableListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean collapseGroup(int groupPos)
    {
        BaseExpandableListAdapter listAdapter =
                (BaseExpandableListAdapter) this.getExpandableListAdapter();
        this.getLayoutParams().height =
                UnitConverter.toDpi(50) * listAdapter.getGroupCount() + UnitConverter.toDpi(1);
        return super.collapseGroup(groupPos);
    }


    @Override
    public boolean expandGroup(int groupPos)
    {

        BaseExpandableListAdapter listAdapter =
                (BaseExpandableListAdapter) this.getExpandableListAdapter();
        this.getLayoutParams().height =
                UnitConverter.toDpi(50) *
                        (listAdapter.getGroupCount() + listAdapter.getChildrenCount(groupPos)) +
                        UnitConverter.toDpi(1);

        return super.expandGroup(groupPos);
    }

    @Override
    public boolean expandGroup(int groupPos, boolean animate)
    {

        BaseExpandableListAdapter listAdapter =
                (BaseExpandableListAdapter) this.getExpandableListAdapter();
        this.getLayoutParams().height =
                UnitConverter.toDpi(50) *
                        (listAdapter.getGroupCount() + listAdapter.getChildrenCount(groupPos)) +
                        UnitConverter.toDpi(1);

        return super.expandGroup(groupPos, animate);
    }
}
