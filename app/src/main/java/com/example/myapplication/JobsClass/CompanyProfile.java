package com.example.myapplication.JobsClass;

import java.io.Serializable;

public class CompanyProfile implements Serializable {
    private String adminName,
            adminNumber,
            designation;

    private String companyName,
            companyNumber,
            companySite,
            workFields,
            aboutCompany;

    private String coverImg,
            logoImg,
            elcSiteLink,
            email,
            facebookAccount,
            instagramAccount,
            twitterAccount;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCompanySite() {
        return companySite;
    }

    public void setCompanySite(String companySite) {
        this.companySite = companySite;
    }

    public String getWorkFields() {
        return workFields;
    }

    public void setWorkFields(String workFields) {
        this.workFields = workFields;
    }

    public String getAboutCompany() {
        return aboutCompany;
    }

    public void setAboutCompany(String aboutCompany) {
        this.aboutCompany = aboutCompany;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public String getElcSiteLink() {
        return elcSiteLink;
    }

    public void setElcSiteLink(String elcSiteLink) {
        this.elcSiteLink = elcSiteLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public String getInstagramAccount() {
        return instagramAccount;
    }

    public void setInstagramAccount(String instagramAccount) {
        this.instagramAccount = instagramAccount;
    }

    public String getTwitterAccount() {
        return twitterAccount;
    }

    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }

    public CompanyProfile() {
    }
    public CompanyProfile(String adminName, String adminNumber, String designation, String companyName, String companyNumber, String companySite, String workFields, String aboutCompany, String coverImg, String logoImg, String elcSiteLink, String email, String facebookAccount, String instagramAccount, String twitterAccount) {
        this.adminName = adminName;
        this.adminNumber = adminNumber;
        this.designation = designation;
        this.companyName = companyName;
        this.companyNumber = companyNumber;
        this.companySite = companySite;
        this.workFields = workFields;
        this.aboutCompany = aboutCompany;
        this.coverImg = coverImg;
        this.logoImg = logoImg;
        this.elcSiteLink = elcSiteLink;
        this.email = email;
        this.facebookAccount = facebookAccount;
        this.instagramAccount = instagramAccount;
        this.twitterAccount = twitterAccount;
    }
    public CompanyProfile(String adminName) {
        this.adminName = adminName;
    }



}
