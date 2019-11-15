package org.ruppin.roper.admin_page;

public class AdminDto {
    private String defaultRadius;
    private String defalutTypes;
    private String googlePlacesKey;
    private String id;
    private String creationDate;
    public String getDefaultRadius ()
    {
        return defaultRadius;
    }

    public void setDefaultRadius (String defaultRadius)
    {
        this.defaultRadius = defaultRadius;
    }

    public String getDefalutTypes ()
    {
        return defalutTypes;
    }

    public void setDefalutTypes (String defalutTypes)
    {
        this.defalutTypes = defalutTypes;
    }

    public String getGooglePlacesKey ()
    {
        return googlePlacesKey;
    }

    public void setGooglePlacesKey (String googlePlacesKey)
    {
        this.googlePlacesKey = googlePlacesKey;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate (String creationDate)
    {
        this.creationDate = creationDate;
    }
}
