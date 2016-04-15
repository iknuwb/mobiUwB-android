package pl.edu.uwb.mobiuwb.tasks.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Oznacza dane wyjściowe, które wypełnia Zadanie w aplikacji.
 */
public abstract class TaskOutput
{
    /**
     * Lista ta zawiera możliwe komunikaty o błędach/wszystkie błędy.
     */
    private List<String> errors;

    /**
     * Inicjalizuje pola w klasie.
     */
    protected TaskOutput()
    {
        errors = new ArrayList<String>();
    }

    /**
     * Dodaje błąd do listy błędów.
     * @param errorMessage Treść błędu.
     */
    public void addError(String errorMessage)
    {
        errors.add(errorMessage);
    }

    /**
     * Metoda ta dodaje błędy do listy błedów.
     * @param errorMessages Błędy do dodania do listy błędów.
     */
    public void addErrors(List<String> errorMessages)
    {
        errors.addAll(errorMessages);
    }

    /**
     * Pobiera listę komunikatów o błędach/błędów.
     * @return Lista komunikatów o błędach/błędów
     */
    public List<String> getErrors()
    {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Sprawdza, czy te dane wyjściowe są poprawne. Determinuje to poprzez
     * sprawdzenie, czy lista błędów jest pusta.
     * @return Czy te dane wyjściowe są poprawne.
     */
    public boolean isValid()
    {
        return errors.isEmpty();
    }
}
