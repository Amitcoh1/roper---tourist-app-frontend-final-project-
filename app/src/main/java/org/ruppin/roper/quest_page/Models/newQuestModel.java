package org.ruppin.roper.quest_page.Models;

public class newQuestModel {
    private String difficulty;

    private String name;

    private String description;

    private String status;

    public String getDifficulty ()
    {
        return difficulty;
    }

    public void setDifficulty (String difficulty)
    {
        this.difficulty = difficulty;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [difficulty = "+difficulty+", name = "+name+", description = "+description+", status = "+status+"]";
    }
}
