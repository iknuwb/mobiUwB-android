package pl.edu.uwb.mobiuwb.parsers.xml.parser;

import java.util.ArrayList;
import java.util.List;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.UnitAddress;

/**
 * Created by Tunczyk on 2015-05-11.
 */
public class UniversityUnit
{
    public static final String DOT_SIGN_CODE = "(kropka)";
    public static final String AT_SIGN_CODE = "(malpa)";

    private MobiUwBMap mobiUwBMap;
    public void setMobiUwBMap(MobiUwBMap mobiUwBMap)
    {
        this.mobiUwBMap = mobiUwBMap;
    }
    public MobiUwBMap getMobiUwBMap()
    {
        return mobiUwBMap;
    }

    private String name;
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    private String fullName;
    public String getFullName()
    {
        return fullName;
    }
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    private String apiUrl;
    public String getApiUrl()
    {
        return apiUrl;
    }
    public void setApiUrl(String apiUrl)
    {
        this.apiUrl = apiUrl;
    }

    private List<Section> sections;
    public List<Section> getSections()
    {
        return sections;
    }
    public void setSections(List<Section> sections)
    {
        this.sections = sections;
    }

    private List<Section> staticSections;
    public List<Section> getStaticSections()
    {
        return staticSections;
    }
    public void setStaticSections(List<Section> staticSections)
    {
        this.staticSections = staticSections;
    }

    private String email;
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = bautifyEmail(email);
    }

    private String tel1;
    public String getTel1()
    {
        return tel1;
    }
    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    private String tel2;
    public String getTel2()
    {
        return tel2;
    }
    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    private String fax;
    public String getFax()
    {
        return fax;
    }
    public void setFax(String fax)
    {
        this.fax = fax;
    }

    private UnitAddress address;
    public UnitAddress getAddress()
    {
        return address;
    }
    public void setAddress(UnitAddress address)
    {
        this.address = address;
    }

    public UniversityUnit()
    {
        this.sections = new ArrayList<Section>();
        this.staticSections = new ArrayList<Section>();
    }

    private String bautifyEmail(String uglyEmail)
    {
        String beautifulEmail = uglyEmail.
                replace(DOT_SIGN_CODE, ".").
                replace(AT_SIGN_CODE, "@");
        return beautifulEmail;
    }

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
