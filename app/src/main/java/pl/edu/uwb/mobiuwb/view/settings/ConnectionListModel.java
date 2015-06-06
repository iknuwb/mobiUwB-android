package pl.edu.uwb.mobiuwb.view.settings;

/**
 * Created by sennajavie on 2015-05-09.
 */
public class ConnectionListModel
{
    private String displayText;
    public String getDisplayText()
    {
        return displayText;
    }
    public void setDisplayText(String displayText)
    {
        this.displayText = displayText;
    }

    private boolean enabled;
    public boolean isEnabled()
    {
        return enabled;
    }
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public ConnectionListModel(String displayText, boolean enabled)
    {
        this.displayText = displayText;
        this.enabled = enabled;
    }

    @Override public String toString()
    {
        return displayText;
    }
}
