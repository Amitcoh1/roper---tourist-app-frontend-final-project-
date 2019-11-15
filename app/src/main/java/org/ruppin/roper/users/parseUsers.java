package org.ruppin.roper.users;

import java.util.List;

public class parseUsers {

    private String status;
    private String totalResults;
    private List<usersEntity> listOfUsers;

    public parseUsers(){};

    public parseUsers(String status, String totalResults, List<usersEntity> listOfUsers) {
        this.status = status;
        this.totalResults = totalResults;
        this.listOfUsers = listOfUsers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<usersEntity> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<usersEntity> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }


}
