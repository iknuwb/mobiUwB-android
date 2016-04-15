package pl.edu.uwb.mobiuwb.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import pl.edu.uwb.mobiuwb.utillities.UnitConverter;

/**
 * Jest to główna kontrolka okna Opcji.
 * Odpowiada ona za funkcjonalność rozwijania oraz zwijania opcji.
 * W programie jest takich wiele, każda zagnieżdżona w poprzednią.
 * Obejmują one liczne adaptery, które posiadają
 */
public class MyExpandableListView extends ExpandableListView
{
    /**
     * Inicjalizuje pole.
     * @param context Kontekst aplikacji lub widoku.
     */
    public MyExpandableListView(Context context)
    {
        super(context);
    }

    /**
     * Inicjalizuje pola.
     * @param context Kontekst aplikacji lub widoku.
     * @param attrs Atrybuty.
     */
    public MyExpandableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Inicjalizuje pola.
     * @param context Kontekst aplikacji lub widoku.
     * @param attrs Atrybuty.
     * @param defStyle Definicja widoku.
     */
    public MyExpandableListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    /**
     * Zamyka zagnieżdżoną grupę kontrolek.
     * Pobiera odpowiednie wysokości oraz odpowiedni adapter.
     * @param groupPos Pozycja grupy do zamknięcia.
     * @return Czy zamknięto grupę.
     */
    @Override
    public boolean collapseGroup(int groupPos)
    {
        BaseExpandableListAdapter listAdapter =
                (BaseExpandableListAdapter) this.getExpandableListAdapter();
        this.getLayoutParams().height =
                UnitConverter.toDpi(50) * listAdapter.getGroupCount() + UnitConverter.toDpi(1);
        return super.collapseGroup(groupPos);
    }

    /**
     * Otwiera zagnieżdżoną grupę kontrolek.
     * Pobiera odpowiednie wysokości oraz odpowiedni adapter.
     * @param groupPos Pozycja grupy do otwarcia.
     * @return Czy otwarto grupę.
     */
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

    /**
     * Otwiera zagnieżdżoną grupę kontrolek.
     * Pobiera odpowiednie wysokości oraz odpowiedni adapter.
     * @param groupPos Pozycja grupy do otwarcia.
     * @param animate Czy wywołać animację otwarcia.
     * @return Czy otwarto grupę.
     */
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
