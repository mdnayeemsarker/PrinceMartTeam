package com.princemartbd.team.model;

public class StatementModel {

    private final String id;
    private final String paid_to;
    private final String paid_from;
    private final String trx_type;
    private final String trx;
    private final String amount;
    private final String post_balance;
    private final String status;
    private final String date_created;
    private final String last_updated;

    public StatementModel(String id, String paid_to, String paid_from, String trx_type, String trx, String amount, String post_balance, String status, String date_created, String last_updated) {
        this.id = id;
        this.paid_to = paid_to;
        this.paid_from = paid_from;
        this.trx_type = trx_type;
        this.trx = trx;
        this.amount = amount;
        this.post_balance = post_balance;
        this.status = status;
        this.date_created = date_created;
        this.last_updated = last_updated;
    }

    public String getId() {
        return id;
    }

    public String getPaid_to() {
        return paid_to;
    }

    public String getPaid_from() {
        return paid_from;
    }

    public String getTrx_type() {
        return trx_type;
    }

    public String getTrx() {
        return trx;
    }

    public String getAmount() {
        return amount;
    }

    public String getPost_balance() {
        return post_balance;
    }

    public String getStatus() {
        return status;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getLast_updated() {
        return last_updated;
    }
}
