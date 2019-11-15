package org.ruppin.roper.tourist_page.Models;

import java.util.ArrayList;

public class questNoBS {
    private String name;
    private String difficulty;
    private String description;
    private String status;
    private String id;

    private ArrayList<MissionNoBS> missions = new ArrayList<MissionNoBS>();

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

    public ArrayList<MissionNoBS> getMissions() {
        return missions;
    }

    public void setMissions(ArrayList<MissionNoBS> missions) {
        this.missions = missions;
    }

    public questNoBS() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void copyQuest(questNoBS q)
    {
        this.setStatus(q.getStatus());
        this.setId(q.getId());
        this.setDescription(q.getDescription());
        this.setDifficulty(q.getDifficulty());
        this.setMissions(this.getMissions());
        this.setName(this.getName());

    }

    @Override
    public String toString() {
        return "Quest{" +
                "name='" + name + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", missions=" + missions +
                '}';
    }
}
