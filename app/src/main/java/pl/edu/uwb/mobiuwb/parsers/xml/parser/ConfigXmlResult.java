package pl.edu.uwb.mobiuwb.parsers.xml.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje to, co zostanie zwrócone po przeanalizowaniu pliku Konfiguracji.
 */
public class ConfigXmlResult
{
    /**
     * Jednostka uniwersytetu. Aktualnie wybrana przez użytkownika.
     */
    private UniversityUnit currentUniversityUnit;

    /**
     * Pobiera jednostkę uniwersytetu z listy jednostek.
     * @return Jednostka uniwersytetu.
     */
    public UniversityUnit getCurrentUniversityUnit()
    {
        //return currentUniversityUnit;
        return universityUnits.get(0);
    }

    /**
     * Lista jednostek uniwersytetu.
     */
    public List<UniversityUnit> universityUnits;

    /**
     * Autorzy.
     */
    public List<String> authors;

    /**
     * Nadaje pola w klasie.
     */
    public ConfigXmlResult()
    {
        this.universityUnits = new ArrayList<UniversityUnit>();
        this.authors = new ArrayList<String>();
    }
}
