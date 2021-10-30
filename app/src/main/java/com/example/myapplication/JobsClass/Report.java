package com.example.myapplication.JobsClass;

import java.io.Serializable;

public class Report implements Serializable {

private String UserUid,CardId,CompanyId,report;

    public String getUserUid() {
        return UserUid;
    }

    public void setUserUid(String userUid) {
        UserUid = userUid;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Report(String userUid, String cardId, String companyId, String report) {
        this.UserUid = userUid;
        this.CardId = cardId;
        this.CompanyId = companyId;
        this.report = report;
    }

    public Report() {
    }
}
