package pl.edu.uwb.mobiuwb.parsers.xml.parser;

/**
 * Reprezentuje sekcję w aplikacji.
 * Sekcja jest kategorią zawartośći w serwisie WWW.
 */
public class Section
{
    /**
     * Tytuł.
     */
    public String title;

    /**
     * Identyfikator
     */
    public String id;

    /**
     * Czy pokazywać licznik.
     */
    public boolean counter;

    /**
     * Czy powiadamiać o zmianach w tej kategorii/sekcji.
     */
    public boolean notificate;
}
