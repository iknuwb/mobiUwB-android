package pl.edu.uwb.mobiuwb.parsers.xml.parser;

import java.util.ArrayList;
import java.util.List;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.UnitAddress;

/**
 * Reprezentuje jednostkę na Uniwersytecie w Białymstoku.
 */
public class UniversityUnit
{
    /**
     * Używane do przerabiania e-maila.
     */
    public static final String DOT_SIGN_CODE = "(kropka)";

    /**
     * Używane do przerabiania e-maila.
     */
    public static final String AT_SIGN_CODE = "(malpa)";

    /**
     * Obiekt mapy w aplikacji.
     */
    private MobiUwBMap mobiUwBMap;

    /**
     * Nadaje obiekt mapy.
     * @param mobiUwBMap Obiekt mapy w aplikacji.
     */
    public void setMobiUwBMap(MobiUwBMap mobiUwBMap)
    {
        this.mobiUwBMap = mobiUwBMap;
    }

    /**
     * Pobiera obiekt mapy.
     * @return Obiekt mapy.
     */
    public MobiUwBMap getMobiUwBMap()
    {
        return mobiUwBMap;
    }

    /**
     * Nazwa.
     */
    private String name;

    /**
     * Pobiera nazwę.
     * @return Nazwa.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Nadaje nazwę.
     * @param name Nazwa.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Pełna nazwa.
     */
    private String fullName;

    /**
     * Pobiera pełną nazwę.
     * @return Pełna nazwa.
     */
    public String getFullName()
    {
        return fullName;
    }

    /**
     * Nadaje pełną nazwę.
     * @param fullName Pełna nazwa.
     */
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    /**
     * Link do pliku JSON Konfiguracji.
     */
    private String apiUrl;

    /**
     * Pobiera link do pliku JSON Konfiguracji.
     * @return Link do pliku JSON Konfiguracji.
     */
    public String getApiUrl()
    {
        return apiUrl;
    }

    /**
     * Nadaje link do pliku JSON Konfiguracji.
     * @param apiUrl Link do pliku JSON Konfiguracji.
     */
    public void setApiUrl(String apiUrl)
    {
        this.apiUrl = apiUrl;
    }

    /**
     * Lista sekcji/kategorii.
     */
    private List<Section> sections;

    /**
     * Pobiera listę sekcji/kategorii,
     * @return Lista sekcji/kategorii.
     */
    public List<Section> getSections()
    {
        return sections;
    }

    /**
     * Nadaje listę sekcji/kategori.
     * @param sections Lista sekcji/kategorii.
     */
    public void setSections(List<Section> sections)
    {
        this.sections = sections;
    }

    /**
     * Lista sekcji statycznych.
     */
    private List<Section> staticSections;

    /**
     * Pobiera listę sekcji statycznych.
     * @return Lista sekcji statycznych.
     */
    public List<Section> getStaticSections()
    {
        return staticSections;
    }

    /**
     * Nadaje listę sekcji statycznych.
     * @param staticSections Lista sekcji statycznych.
     */
    public void setStaticSections(List<Section> staticSections)
    {
        this.staticSections = staticSections;
    }

    /**
     * E-mail.
     */
    private String email;

    /**
     * Pobiera e-mail.
     * @return E-mail.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Nadaje e-mail. Dodatkowo go upiększa.
     * @param email E-mail.
     */
    public void setEmail(String email)
    {
        this.email = bautifyEmail(email);
    }

    /**
     * Pierwszy numer telefonu.
     */
    private String tel1;

    /**
     * Pobiera pierwszy numer telefonu.
     * @return Pierwszy numer telefonu.
     */
    public String getTel1()
    {
        return tel1;
    }

    /**
     * Nadaje pierwszy numer telefonu.
     * @param tel1 Pierwszy numer telefonu.
     */
    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    /**
     * Drugi numer telefonu.
     */
    private String tel2;

    /**
     * Pobiera drugi numer telefonu.
     * @return Drugi numer telefonu.
     */
    public String getTel2()
    {
        return tel2;
    }

    /**
     * Nadaje drugi numer telefonu.
     * @param tel2 Drugi numer telefonu.
     */
    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    /**
     * Fax.
     */
    private String fax;

    /**
     * Pobiera fax.
     * @return Fax.
     */
    public String getFax()
    {
        return fax;
    }

    /**
     * Nadaje fax.
     * @param fax Fax.
     */
    public void setFax(String fax)
    {
        this.fax = fax;
    }

    /**
     * Obiekt opisujący jednostkę na uczelni.
     */
    private UnitAddress address;

    /**
     * Pobiera obiekt opisujący jednostkę na uczelni.
     * @return Obiekt opisujący jednostkę na uczelni.
     */
    public UnitAddress getAddress()
    {
        return address;
    }

    /**
     * Nadaje obiekt opisujący jednostkę na uczelni.
     * @param address Obiekt opisujący jednostkę na uczelni.
     */
    public void setAddress(UnitAddress address)
    {
        this.address = address;
    }

    /**
     * Nadaje wartości polom.
     */
    public UniversityUnit()
    {
        this.sections = new ArrayList<Section>();
        this.staticSections = new ArrayList<Section>();
    }

    /**
     * Zawiera proces upiększenia e-maila i
     * uczynienia go prawdziwym e-mailem.
     * @param uglyEmail Brzydki e-mail.
     * @return Piękny e-mail.
     */
    private String bautifyEmail(String uglyEmail)
    {
        String beautifulEmail = uglyEmail.
                replace(DOT_SIGN_CODE, ".").
                replace(AT_SIGN_CODE, "@");
        return beautifulEmail;
    }

    /**
     * Pobiera sekcję/kategorię po jej ID.
     * @param categoryId ID sekcji/kategori..
     * @return Sekcja/kategoria o żądanym ID.
     */
    public Section getSectionById(String categoryId)
    {
        for (Section section : sections)
        {
            if(section.id.equals(categoryId))
            {
                return section;
            }
        }
        for (Section section : staticSections)
        {
            if(section.id.equals(categoryId))
            {
                return section;
            }
        }
        return null;
    }
}
