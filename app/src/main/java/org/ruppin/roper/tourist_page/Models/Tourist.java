package org.ruppin.roper.tourist_page.Models;


import java.util.ArrayList;

public class Tourist
{
    private String lastName;

    private String preferences;

    private String meetPeople;

    private String gender;

    private String vicationType;

    private String dateOfBirth;

    private String creationDate;

    private String type;

    private String dateOfBirthStr;

    private String defaultRadius;

    private String firstName;

    private String password;

    private String moreInformation;

    private String loginName;

    private String defalutTypes;

    private String radiusThreshold;

    private String googlePlacesKey;

    private String spokenLanguages;

    private String id;

    private String email;

    private String age;

    private String finishedQuests;

    private String finishedMissions;

    private String onGoingQuests;

    private String onGoingMissions;

    public Tourist(String id){this.id = id;}

    public Tourist(){}

    private ArrayList<Quest> quests = new ArrayList<>();

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getPreferences ()
    {
        return preferences;
    }

    public void setPreferences (String preferences)
    {
        this.preferences = preferences;
    }

    public String getMeetPeople ()
    {
        return meetPeople;
    }

    public void setMeetPeople (String meetPeople)
    {
        this.meetPeople = meetPeople;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getVicationType ()
    {
        return vicationType;
    }

    public void setVicationType (String vicationType)
    {
        this.vicationType = vicationType;
    }

    public String getDateOfBirth ()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth (String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate (String creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getDateOfBirthStr ()
    {
        return dateOfBirthStr;
    }

    public void setDateOfBirthStr (String dateOfBirthStr)
    {
        this.dateOfBirthStr = dateOfBirthStr;
    }

    public String getDefaultRadius ()
    {
        return defaultRadius;
    }

    public void setDefaultRadius (String defaultRadius)
    {
        this.defaultRadius = defaultRadius;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getMoreInformation ()
    {
        return moreInformation;
    }

    public void setMoreInformation (String moreInformation)
    {
        this.moreInformation = moreInformation;
    }

    public String getLoginName ()
    {
        return loginName;
    }

    public void setLoginName (String loginName)
    {
        this.loginName = loginName;
    }

    public String getDefalutTypes ()
    {
        return defalutTypes;
    }

    public void setDefalutTypes (String defalutTypes)
    {
        this.defalutTypes = defalutTypes;
    }

    public String getRadiusThreshold ()
    {
        return radiusThreshold;
    }

    public void setRadiusThreshold (String radiusThreshold)
    {
        this.radiusThreshold = radiusThreshold;
    }

    public String getGooglePlacesKey ()
    {
        return googlePlacesKey;
    }

    public void setGooglePlacesKey (String googlePlacesKey)
    {
        this.googlePlacesKey = googlePlacesKey;
    }

    public String getSpokenLanguages ()
    {
        return spokenLanguages;
    }

    public void setSpokenLanguages (String spokenLanguages)
    {
        this.spokenLanguages = spokenLanguages;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getAge ()
    {
        return age;
    }

    public void setAge (String age)
    {
        this.age = age;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<Quest> quests) {
        this.quests = quests;
    }

    public String getFinishedQuests() {
        return finishedQuests;
    }

    public void setFinishedQuests(String finishedQuests) {
        this.finishedQuests = finishedQuests;
    }

    public String getFinishedMissions() {
        return finishedMissions;
    }

    public void setFinishedMissions(String finishedMissions) {
        this.finishedMissions = finishedMissions;
    }

    public String getOnGoingQuests() {
        return onGoingQuests;
    }

    public void setOnGoingQuests(String onGoingQuests) {
        this.onGoingQuests = onGoingQuests;
    }

    public String getOnGoingMissions() {
        return onGoingMissions;
    }

    public void setOnGoingMissions(String onGoingMissions) {
        this.onGoingMissions = onGoingMissions;
    }

    @Override
    public String toString()
    {
        return "lastName = "+lastName+", preferences = "+preferences+", meetPeople = "+meetPeople+", gender = "+gender+", vicationType = "+vicationType+", dateOfBirth = "+dateOfBirth+", creationDate = "+creationDate+", type = "+type+", dateOfBirthStr = "+dateOfBirthStr+", defaultRadius = "+defaultRadius+", firstName = "+firstName+", password = "+password+", moreInformation = "+moreInformation+", loginName = "+loginName+", defalutTypes = "+defalutTypes+", radiusThreshold = "+radiusThreshold+", googlePlacesKey = "+googlePlacesKey+", spokenLanguages = "+spokenLanguages+", id = "+id+", email = "+email+", age = "+age;
    }}