package org.ruppin.roper.users;

public class usersEntity {
    private String username;
    private String password;
    private String type;

    public usersEntity(){};
    public usersEntity(String username, String pass) {
        this.username = username;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class addUserEntity {
        private String userName;
        private String password;
        private String fName;
        private String lName;
        private String DOB;
        private String email;
        private String type;
        private boolean moreInfo;
        private String age;
        private String spokenLen;
        private String gender;
        private String vicType;
        private boolean meetPeople;
        private String radiusThreshold;
        private String ctgrsTypes;

        public String getCtgrsTypes() {
            return ctgrsTypes;
        }
        public void setCtgrsTypes(String ctgrsTypes) {
            this.ctgrsTypes = ctgrsTypes;
        }

        public boolean isMeetPeople() {
            return meetPeople;
        }
        public void setMeetPeople(boolean meetPeople) {
            this.meetPeople = meetPeople;
        }

        public String getRadiusThreshold() {
            return radiusThreshold;
        }
        public void setRadiusThreshold(String radiusThreshold) {
            this.radiusThreshold = radiusThreshold;
        }

        public String getAge() {
            return age;
        }
        public void setAge(String age) {
            this.age = age;
        }

        public String getSpokenLen() {
            return spokenLen;
        }
        public void setSpokenLen(String spokenLen) {
            this.spokenLen = spokenLen;
        }

        public String getGender() {
            return gender;
        }
        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getVicType() {
            return vicType;
        }
        public void setVicType(String vicType) {
            this.vicType = vicType;
        }

        public boolean isMoreInfo() {
            return moreInfo;
        }
        public void setMoreInfo(boolean moreInfo) {
            this.moreInfo = moreInfo;
        }

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        public String getfName() {
            return fName;
        }
        public void setfName(String fName) {
            this.fName = fName;
        }

        public String getlName() {
            return lName;
        }
        public void setlName(String lName) {
            this.lName = lName;
        }

        public String getDOB() {
            return DOB;
        }
        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
    }
}
