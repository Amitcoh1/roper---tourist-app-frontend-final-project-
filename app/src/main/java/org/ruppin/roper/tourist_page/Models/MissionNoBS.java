package org.ruppin.roper.tourist_page.Models;

public class MissionNoBS {
    private String name;
    private String difficulty;
    private String description;
    private String status;
    private String id;

    public MissionNoBS(String id){this.id = id;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public MissionNoBS() {
    }

    public MissionNoBS(String name, String difficulty, String description, String status) {
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + '\"' +
                ", \"difficulty\":\"" + difficulty + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"status\":\"" + status + '\"' +
                '}';
    }
}
