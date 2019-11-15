package org.ruppin.roper.tourist_page.Models;

import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;

import java.util.ArrayList;

public class Mission {
    private String id;
    private String name;
    private String difficulty;
    private String description;
    private String status;
    private BusinessBO business;

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

    public BusinessBO getBusiness() {
        return business;
    }

    public void setBusiness(BusinessBO business) {
        this.business = business;
    }

    public Mission() {}

    public Mission(String name, String difficulty, String description, String status, BusinessBO business) {
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
        this.status = status;
        this.business = business;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "name='" + name + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", businesses=" + business +
                '}';
    }
}
