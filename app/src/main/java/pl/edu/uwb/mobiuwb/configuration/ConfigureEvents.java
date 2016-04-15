package pl.edu.uwb.mobiuwb.configuration;

/**
 * Reprezentuje wydarzenia związane z konfiguracją.
 */
public interface ConfigureEvents
{
    /**
     * Dzieje się gdy ukończona zostanie konfiguracja.
     * @param succeeded Czy konfiguracja się powiodła.
     */
    void onConfigurationFinished(boolean succeeded);
}
