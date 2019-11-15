package org.ruppin.roper.bussines_owner_page.Models;

import java.util.ArrayList;

public class BusinessOwner {
    private String creationDate;
    private String id;
    private String dateOfBirthStr;
    private String email;
    private String loginName;
    private String firstName;
    private String lastName;
    private String password;
    private ArrayList<BusinessBO> businesses= new ArrayList<BusinessBO>();
    private BusinessBO businessBO;

    public BusinessOwner(String id, ArrayList<BusinessBO> businessBO)
    {
        this.id = id;
        this.businesses = (ArrayList<BusinessBO>)businessBO.clone();
    }

    public BusinessOwner(String creationDate,
                         String id,
                         String dateOfBirthStr,
                         String email,
                         String loginName,
                         String firstName,
                         String lastName,
                         String password,
                         BusinessBO businessBO
                         )
    {
        this.creationDate = creationDate;
        this.id = id;
        this.dateOfBirthStr = dateOfBirthStr;
        this.email = email;
        this.loginName = loginName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.businessBO = businessBO;
    }
    public BusinessOwner(String creationDate,
                         String id,
                         String dateOfBirthStr,
                         String email,
                         String loginName,
                         String firstName,
                         String lastName,
                         String password
    )
    {
        this.creationDate = creationDate;
        this.id = id;
        this.dateOfBirthStr = dateOfBirthStr;
        this.email = email;
        this.loginName = loginName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateOfBirthStr() {
        return dateOfBirthStr;
    }

    public void setDateOfBirthStr(String dateOfBirthStr) {
        this.dateOfBirthStr = dateOfBirthStr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<BusinessBO> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<BusinessBO> businesses) {
        this.businesses = businesses;
    }

    public BusinessBO getBusinessBO() {
        return businessBO;
    }

    public void setBusinessBO(BusinessBO businessBO) {
        this.businessBO = businessBO;
    }
}
