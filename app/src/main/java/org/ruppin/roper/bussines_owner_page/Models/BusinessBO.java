package org.ruppin.roper.bussines_owner_page.Models;

import java.util.ArrayList;
import java.util.stream.Stream;

public class BusinessBO {
    private String creationDate;
    private String id;
    private String latitude;
    private String longitude;
    private String name;
    private String onPromotion;
    private String address;
    private String promotionInfo;
    private String type;
    private String missionToken;
    //

    public BusinessBO() {}
    public BusinessBO(String id)
    {
        this.id = id;
    }
    public BusinessBO(String creationDate, String id, String latitude, String longitude, String name, String onPromotion, String address,String promotionInfo,String type,String missionToken) {
        this.creationDate = creationDate;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.onPromotion = onPromotion;
        this.address = address;
        this.promotionInfo = promotionInfo;
        this.type = type;
        this.missionToken = missionToken;
    }
    public BusinessBO(String missionToken,String id, String latitude, String longitude, String name, String onPromotion, String address,String promotionInfo,String type) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.onPromotion = onPromotion;
        this.address = address;
        this.promotionInfo = promotionInfo;
        this.type = type;
        this.missionToken = missionToken;
    }
    //
    public String getMissionToken() {
        return missionToken;
    }

    public void setMissionToken(String missionToken) {
        this.missionToken = missionToken;
    }

    public String getPromotionInfo() {
        return promotionInfo;
    }

    public void setPromotionInfo(String promotionInfo) {
        this.promotionInfo = promotionInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnPromotion() {
        return onPromotion;
    }

    public void setOnPromotion(String onPromotion) {
        this.onPromotion = onPromotion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
