package com.princemartbd.team.model;

public class SummeryModel {
    private final String id;
    private final String name;
    private final String profile;
    private final String balance;
    private final String income;
    private final String type;
    private final String userId;

    public SummeryModel(String id, String name, String profile, String balance, String income, String type, String userId) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.balance = balance;
        this.income = income;
        this.type = type;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public String getBalance() {
        return balance;
    }

    public String getIncome() {
        return income;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }
}
