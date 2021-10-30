package com.example.myapplication.JobsClass;

import java.io.Serializable;

public class OrderJob implements Serializable {

   private String userUid;

    private String cardIdA;
    private  String idCompany;
    private String sFirstName, sLastname, sHomeAddress, sEmailAddress, sPhoneNumber;
    private String ssCountry, ssWorkCity, ssCity, ssNationality, ssExperience, ssEducationLevel, ssMilitaryServes, ssExperienceSalary, ssCurrentJobState, ssJobType;
    private String Date, gender, imageUriCv;

    public String getsFirstName() {
        return sFirstName;
    }

    public void setsFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public String getsLastname() {
        return sLastname;
    }

    public void setsLastname(String sLastname) {
        this.sLastname = sLastname;
    }

    public String getsHomeAddress() {
        return sHomeAddress;
    }

    public void setsHomeAddress(String sHomeAddress) {
        this.sHomeAddress = sHomeAddress;
    }

    public String getsEmailAddress() {
        return sEmailAddress;
    }

    public void setsEmailAddress(String sEmailAddress) {
        this.sEmailAddress = sEmailAddress;
    }

    public String getsPhoneNumber() {
        return sPhoneNumber;
    }

    public void setsPhoneNumber(String sPhoneNumber) {
        this.sPhoneNumber = sPhoneNumber;
    }

    public String getSsCountry() {
        return ssCountry;
    }

    public void setSsCountry(String ssCountry) {
        this.ssCountry = ssCountry;
    }

    public String getSsWorkCity() {
        return ssWorkCity;
    }

    public void setSsWorkCity(String ssWorkCity) {
        this.ssWorkCity = ssWorkCity;
    }

    public String getSsCity() {
        return ssCity;
    }

    public void setSsCity(String ssCity) {
        this.ssCity = ssCity;
    }

    public String getSsNationality() {
        return ssNationality;
    }

    public void setSsNationality(String ssNationality) {
        this.ssNationality = ssNationality;
    }

    public String getSsExperience() {
        return ssExperience;
    }

    public void setSsExperience(String ssExperience) {
        this.ssExperience = ssExperience;
    }

    public String getSsEducationLevel() {
        return ssEducationLevel;
    }

    public void setSsEducationLevel(String ssEducationLevel) {
        this.ssEducationLevel = ssEducationLevel;
    }

    public String getSsMilitaryServes() {
        return ssMilitaryServes;
    }

    public void setSsMilitaryServes(String ssMilitaryServes) {
        this.ssMilitaryServes = ssMilitaryServes;
    }

    public String getSsExperienceSalary() {
        return ssExperienceSalary;
    }

    public void setSsExperienceSalary(String ssExperienceSalary) {
        this.ssExperienceSalary = ssExperienceSalary;
    }

    public String getSsCurrentJobState() {
        return ssCurrentJobState;
    }

    public void setSsCurrentJobState(String ssCurrentJobState) {
        this.ssCurrentJobState = ssCurrentJobState;
    }

    public String getSsJobType() {
        return ssJobType;
    }

    public void setSsJobType(String ssJobType) {
        this.ssJobType = ssJobType;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUriCv() {
        return imageUriCv;
    }

    public void setImageUriCv(String imageUriCv) {
        this.imageUriCv = imageUriCv;
    }

    public OrderJob(String userUid, String sFirstName, String sLastname, String sHomeAddress, String sEmailAddress, String sPhoneNumber, String ssCountry, String ssWorkCity, String ssCity, String ssNationality, String ssExperience, String ssEducationLevel, String ssMilitaryServes, String ssExperienceSalary, String ssCurrentJobState, String ssJobType, String date, String gender, String imageUriCv) {
        this.userUid = userUid;

        this.sFirstName = sFirstName;
        this.sLastname = sLastname;
        this.sHomeAddress = sHomeAddress;
        this.sEmailAddress = sEmailAddress;
        this.sPhoneNumber = sPhoneNumber;
        this.ssCountry = ssCountry;
        this.ssWorkCity = ssWorkCity;
        this.ssCity = ssCity;
        this.ssNationality = ssNationality;
        this.ssExperience = ssExperience;
        this.ssEducationLevel = ssEducationLevel;
        this.ssMilitaryServes = ssMilitaryServes;
        this.ssExperienceSalary = ssExperienceSalary;
        this.ssCurrentJobState = ssCurrentJobState;
        this.ssJobType = ssJobType;
        this.Date = date;
        this.gender = gender;
        this.imageUriCv = imageUriCv;
    }

    public String getCardIdA() {
        return cardIdA;
    }

    public void setCardIdA(String cardIdA) {
        this.cardIdA = cardIdA;
    }

    public OrderJob(String userUid, String cardIdA,   String companyId) {
        this.userUid = userUid;

        this.cardIdA=cardIdA;
        this.idCompany =companyId;

    }

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }

    public OrderJob() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }


}
