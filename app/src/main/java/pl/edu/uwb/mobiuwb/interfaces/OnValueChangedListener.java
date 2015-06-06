package pl.edu.uwb.mobiuwb.interfaces;

/**
 * Created by Tunczyk on 2015-05-01.
 */
public interface OnValueChangedListener<T>
{
    void notifyChanged(T oldValue, T newValue);
    //void preValueChanged(T oldValue,T newValue);
}
