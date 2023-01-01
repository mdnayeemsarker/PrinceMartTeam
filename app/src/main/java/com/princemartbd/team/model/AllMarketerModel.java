package com.princemartbd.team.model;

public class AllMarketerModel {

    private final String id;
    private final String added_by;
    private final String name;
    private final String email;
    private final String country_code;
    private final String mobile;
    private final String balance;
    private final String referral_code;
    private final String fcm_id;
    private final String status;
    private final String type;
    private final String date_create;

    public AllMarketerModel(String id, String added_by, String name, String email, String country_code, String mobile, String balance, String referral_code, String fcm_id, String status, String type, String date_create) {
        this.id = id;
        this.added_by = added_by;
        this.name = name;
        this.email = email;
        this.country_code = country_code;
        this.mobile = mobile;
        this.balance = balance;
        this.referral_code = referral_code;
        this.fcm_id = fcm_id;
        this.status = status;
        this.type = type;
        this.date_create = date_create;
    }

    public String getId() {
        return id;
    }

    public String getAdded_by() {
        return added_by;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBalance() {
        return balance;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getDate_create() {
        return date_create;
    }
}
