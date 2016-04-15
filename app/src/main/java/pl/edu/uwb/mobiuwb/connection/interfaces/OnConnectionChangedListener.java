package pl.edu.uwb.mobiuwb.connection.interfaces;

import pl.edu.uwb.mobiuwb.connection.CheckingType;

/**
 * Opisuje zdarzenia odpowiedzialne za zmianę połączenia.
 */
public interface OnConnectionChangedListener
{
    /**
     * Wydarza się, gdy połączenie na urządzeniu się zmieni.
     * @param checkingType Nowy typ połączenia.
     */
    void connectionChanged(CheckingType checkingType);
}
