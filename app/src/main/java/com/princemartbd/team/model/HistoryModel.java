package com.princemartbd.team.model;

public class HistoryModel {

    private final String id;
    private final String name;
    private final String store_name;
    private final String logo;
    private final String email;
    private final String mobile;
    private final String status;

    public HistoryModel(String id, String name, String store_name, String logo, String email, String mobile, String status) {
        this.id = id;
        this.name = name;
        this.store_name = store_name;
        this.logo = logo;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getLogo() {
        return logo;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getStatus() {
        return status;
    }
}
