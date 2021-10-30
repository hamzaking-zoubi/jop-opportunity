package com.example.myapplication.Notification;

public class Data {
    private static final int NEW_ADV_NOTIFICATION = 1;///new ADv
    private static final int NEW_MESSAGE_ACCEPT_NOTIFICATION = 2;///accept 2
    private static final int NEW_APPLICANT_NOTIFICATION = 3;// 2 تقد
    private int notificationType;
    private String Title;
    private String Message;
    private String advId;
    private String CompanyId;

    public static int getNewApplicantNotification() {
        return NEW_APPLICANT_NOTIFICATION;
    }


    public Data(String title, String message, String advId, String companyId) {
        Title = title;
        Message = message;
        this.advId = advId;
        CompanyId = companyId;

    }

    public static int getNewAdvNotification() {
        return NEW_ADV_NOTIFICATION;
    }


    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
        this.advId = advId;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }


    public Data(String title, String message) {
        Title = title;
        Message = message;

    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Data(String title, String message, String advId, String companyId, int notificationType) {
        this.Title = title;
        this.Message = message;
        this.advId = advId;
        this.CompanyId = companyId;
        this.notificationType = notificationType;
    }

    public static int getNewMessageAcceptNotification() {
        return NEW_MESSAGE_ACCEPT_NOTIFICATION;
    }

}
