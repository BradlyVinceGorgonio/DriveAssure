package com.example.driveassure;

public class RequestingClass {

    private String profilePictureUrl;

    private String name;

    private String uid;

    private String carName;

    private String daysAmount;

    private String rentDateRange;
    private String carUID;
    private String requestID;



    public RequestingClass(String profilePictureUrl, String name, String uid, String carName, String daysAmount, String rentDateRange, String carUID, String requestID) {
        this.profilePictureUrl = profilePictureUrl;
        this.name = name;
        this.uid = uid;
        this.carName = carName;
        this.daysAmount = daysAmount;
        this.rentDateRange = rentDateRange;
        this.carUID = carUID;
        this.requestID = requestID;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
    public String getCarUID() {
        return carUID;
    }

    public String getCarName() {
        return carName;
    }

    public String getDaysAmount() {
        return daysAmount;
    }

    public String getRentDateRange() {
        return rentDateRange;
    }
    public String getRequestID()
    {
        return requestID;
    }
}
