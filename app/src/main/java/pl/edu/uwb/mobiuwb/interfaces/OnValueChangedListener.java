package pl.edu.uwb.mobiuwb.interfaces;

/**
 * Opisuje wydarzenia związane ze zmianą wartości.
 */
public interface OnValueChangedListener<T>
{
    /**
     * Wydarza się, gdy zmienimy wartość.
     * @param oldValue Stara wartość.
     * @param newValue Nowa wartość.
     */
    void notifyChanged(T oldValue, T newValue);
}
