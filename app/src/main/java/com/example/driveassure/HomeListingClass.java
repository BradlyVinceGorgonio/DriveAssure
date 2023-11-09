package com.example.driveassure;

public class HomeListingClass {

    private String profilePictureUrl;

    private String name;

    private String uid;

    private String carPrice;

    private String carLocation;

    private String carTransmission;

    private String carpostUID;





    public HomeListingClass(String profilePictureUrl, String name, String uid, String carPrice, String carLocation, String carTransmission, String carpostUID) {
        this.profilePictureUrl = profilePictureUrl;
        this.name = name;
        this.uid = uid;
        this.carPrice = carPrice;
        this.carLocation = carLocation;
        this.carTransmission = carTransmission;
        this.carpostUID = carpostUID;

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

    public String getCarPrice() {
        return carPrice;
    }

    public String getCarLocation() {
        return carLocation;
    }

    public String getCarTransmission() {
        return carTransmission;
    }

    public String getCarpostUID(){return carpostUID;}

}
