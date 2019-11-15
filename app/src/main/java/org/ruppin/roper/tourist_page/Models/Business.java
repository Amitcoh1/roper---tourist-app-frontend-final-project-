package org.ruppin.roper.tourist_page.Models;

public class Business {
    private String name;
    private String types;
    private String type;
    private Boolean onPromotion;
    private Boolean openNow;
    private String promotionInfo;
    private String longitude;
    private String latitude;
    private String address;
    private String icon;
    private String priceLevel;
    private String rating;
    private String userRatingsTotal;
    private String missionToken;

    public Business(String name, String types, String type, Boolean onPromotion, Boolean openNow, String promotionInfo, String longitude, String latitude, String address, String icon, String priceLevel, String rating, String userRatingsTotal,String missionToken) {
        this.name = name;
        this.types = types;
        this.type = type;
        this.onPromotion = onPromotion;
        this.openNow = openNow;
        this.promotionInfo = promotionInfo;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.icon = icon;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.userRatingsTotal = userRatingsTotal;
        this.missionToken = missionToken;
    }

    public String getMissionToken() {
        return missionToken;
    }

    public void setMissionToken(String missionToken) {
        this.missionToken = missionToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getOnPromotion() {
        return onPromotion;
    }

    public void setOnPromotion(Boolean onPromotion) {
        this.onPromotion = onPromotion;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public String getPromotionInfo() {
        return promotionInfo;
    }

    public void setPromotionInfo(String promotionInfo) {
        this.promotionInfo = promotionInfo;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(String userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }
}
