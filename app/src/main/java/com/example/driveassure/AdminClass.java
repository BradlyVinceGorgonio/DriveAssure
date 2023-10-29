package com.example.driveassure;

public class AdminClass
{
    private String profilePictureUrl;

    private String name;

    private String uid;

    private String licenseNum;

    private String licenseExp;

    private String contactNum;



    public AdminClass(String profilePictureUrl, String name, String uid, String licenseNum, String licenseExp, String contactNum) {
        this.profilePictureUrl = profilePictureUrl;
        this.name = name;
        this.uid = uid;
        this.licenseNum = licenseNum;
        this.licenseExp = licenseExp;
        this.contactNum = contactNum;
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

    public String getLicenseNum() {
        return licenseNum;
    }

    public String getLicenseExp() {
        return licenseExp;
    }

    public String getContactNum() {
        return contactNum;
    }
}
