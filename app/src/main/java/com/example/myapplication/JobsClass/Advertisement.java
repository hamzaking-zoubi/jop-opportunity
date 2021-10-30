package com.example.myapplication.JobsClass;

import java.io.Serializable;

public class Advertisement implements Serializable {
    private String imageURL;
    private String title;
    private String fullDetail;
    private long timeAccpt;
    private long timeSendMaseege;
    private String fewDetail;
    private String idCompany;
    private String lnk;
    private String nameVidio;
    private String videoUrl;
    private String category;
    private String id;
    private String department, jopDetails, trainingCourses, personalSkills, keyWords,
            workplace,//option
            moreQualifications,//option
            workTime,//filter
            salary,//filter
            educationLevel,//filter
            experienceYears,//filter
            gender//filter //option
                    ;//from id get admin info like number or name

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJopDetails() {
        return jopDetails;
    }

    public void setJopDetails(String jopDetails) {
        this.jopDetails = jopDetails;
    }

    public String getTrainingCourses() {
        return trainingCourses;
    }

    public void setTrainingCourses(String trainingCourses) {
        this.trainingCourses = trainingCourses;
    }

    public String getPersonalSkills() {
        return personalSkills;
    }

    public void setPersonalSkills(String personalSkills) {
        this.personalSkills = personalSkills;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getMoreQualifications() {
        return moreQualifications;
    }

    public void setMoreQualifications(String moreQualifications) {
        this.moreQualifications = moreQualifications;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(String experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Advertisement(String imageURL, String title, String fullDetail, long timeAccpt, long timeSendMaseege, String fewDetail, String idCompany, String lnk, String nameVidio, String videoUrl, String category, String id, String department, String jopDetails, String trainingCourses, String personalSkills, String workplace, String moreQualifications, String keyWords, String workTime, String salary, String educationLevel, String experienceYears, String gender) {
        this.imageURL = imageURL;
        this.title = title;
        this.fullDetail = fullDetail;
        this.timeAccpt = timeAccpt;
        this.timeSendMaseege = timeSendMaseege;
        this.fewDetail = fewDetail;
        this.idCompany = idCompany;
        this.lnk = lnk;
        this.nameVidio = nameVidio;
        this.videoUrl = videoUrl;
        this.category = category;
        this.id = id;
        this.department = department;
        this.jopDetails = jopDetails;
        this.trainingCourses = trainingCourses;
        this.personalSkills = personalSkills;
        this.workplace = workplace;
        this.moreQualifications = moreQualifications;
        this.keyWords = keyWords;
        this.workTime = workTime;
        this.salary = salary;
        this.educationLevel = educationLevel;
        this.experienceYears = experienceYears;
        this.gender = gender;
    }





    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }

    public String getNameVidio() {
        return nameVidio;
    }

    public void setNameVidio(String nameVidio) {
        this.nameVidio = nameVidio;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public long getTimeSendMaseege() {
        return timeSendMaseege;
    }

    public void setTimeSendMaseege(long timeSendMaseege) {
        this.timeSendMaseege = timeSendMaseege;
    }

    public String getLnk() {
        return lnk;
    }

    public void setLnk(String lnk) {
        this.lnk = lnk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Advertisement(String imageURL, String title, String fullDetail, long timeAccpt, String fewDetail, String category, String id) {
        this.imageURL = imageURL;
        this.title = title;
        this.fullDetail = fullDetail;
        this.timeAccpt = timeAccpt;
        this.fewDetail = fewDetail;

        this.category = category;

        this.id = id;
    }

    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullDetail() {
        return fullDetail;
    }

    public void setFullDetail(String fullDetail) {
        this.fullDetail = fullDetail;
    }

    public long getTimeAccpt() {
        return timeAccpt;
    }

    public void setTimeAccpt(long timeAccpt) {
        this.timeAccpt = timeAccpt;
    }

    public String getFewDetail() {
        return fewDetail;
    }

    public void setFewDetail(String fewDetail) {
        this.fewDetail = fewDetail;
    }


//    public Jobs(String id, String nameVidio, String videoUrl, long timeSendMaseege, String imageURL, String title, String fullDetail, String timeAccpt, String fewDetail, String lnk, String category) {
//        this.imageURL = imageURL;
//        this.title = title;
//
//        this.fullDetail = fullDetail;
//        this.timeAccpt = timeAccpt;
//        this.fewDetail = fewDetail;
//        this.id = id;
//        this.category = category;
//        this.lnk = lnk;
//        this.timeSendMaseege = timeSendMaseege;
//        this.videoUrl = videoUrl;
//        this.nameVidio = nameVidio;
//
//    }

    public Advertisement() {
    }
////////////////////1
    public Advertisement(String id, String idCompany, long timeSendMaseege, String imageURL, String title,
                String fullDetail, long timeAccpt, String fewDetail, String lnk, String category,String videoUrl) {
        this.imageURL = imageURL;
        this.title = title;
        this.videoUrl=videoUrl;
        this.idCompany = idCompany;
        this.fullDetail = fullDetail;
        this.timeAccpt = timeAccpt;
        this.fewDetail = fewDetail;
        this.id = id;
        this.category = category;
        this.lnk = lnk;
        this.timeSendMaseege = timeSendMaseege;


    }
    public Advertisement(String id, String idCompany, long timeSendMaseege, String imageURL, String title,
                String fullDetail, long timeAccpt, String fewDetail, String lnk, String category) {
        this.imageURL = imageURL;
        this.title = title;
        this.idCompany = idCompany;
        this.fullDetail = fullDetail;
        this.timeAccpt = timeAccpt;
        this.fewDetail = fewDetail;
        this.id = id;
        this.category = category;
        this.lnk = lnk;
        this.timeSendMaseege = timeSendMaseege;


    }

}
