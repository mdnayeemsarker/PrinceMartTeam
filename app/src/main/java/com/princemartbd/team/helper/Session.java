package com.princemartbd.team.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

import com.princemartbd.team.R;
import com.princemartbd.team.activity.Base.LoginActivity;
import com.princemartbd.team.activity.Manager.ManagerActivity;
import com.princemartbd.team.activity.Marketer.MarketerActivity;

@SuppressWarnings("UnusedReturnValue")
public class Session {
    public static final String PREFER_NAME = "abmnPrinceMath";
    final int PRIVATE_MODE = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _activity;

    public Session(Context activity) {
        try {
            this._activity = activity;
            pref = _activity.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Boolean IS_LOGGED_IN(Activity activity) {
        if (!pref.getBoolean(Constant.IS_USER_LOGIN, false)) {
            Intent i = new Intent(activity, LoginActivity.class);
            activity.startActivity(i);
            activity.finish();
        }
        return pref.getBoolean(Constant.IS_USER_LOGIN, false);
    }

    public String getData(String id) {
        return pref.getString(id, "");
    }
    public String getTypeUser() {
        return pref.getString(Constant.TYPE_USER, "");
    }
    public String getUserId() {
        return pref.getString(Constant.USER_ID, "");
    }

//    public String getCoordinates(String id) {
//        return pref.getString(id, "0");
//    }

    public void setData(String id, String val) {
        editor.putString(id, val);
        editor.commit();
    }

    public void setBoolean(String id, boolean val) {
        editor.putBoolean(id, val);
        editor.commit();
    }

    public boolean getBoolean(String id) {
        return pref.getBoolean(id, false);
    }

    public boolean getIsLog(){
        return pref.getBoolean(Constant.IS_USER_LOGIN, false);
    }

    public void createUserLoginSession(String name, String email, String mobile, String password, String countryCode, String referCode) {
//        editor.putBoolean(Constant.IS_USER_LOGIN, true);
        editor.putString(Constant.NAME, name);
        editor.putString(Constant.EMAIL, email);
        editor.putString(Constant.MOBILE, mobile);
        editor.putString(Constant.PASSWORD, password);
        editor.putString(Constant.COUNTRY_CODE, countryCode);
        editor.putString(Constant.REFERRAL_CODE, referCode);
        editor.commit();
    }

    public void setUserData(String user_id, String added_by, String name, String email, String country_code, String mobile, String balance, String referral_code, String fcm_id, String status, String type, String date_created) {
        editor.putString(Constant.USER_ID, user_id);
        editor.putString(Constant.ADDED_BY, added_by);
        editor.putString(Constant.NAME, name);
        editor.putString(Constant.EMAIL, email);
        editor.putString(Constant.COUNTRY_CODE, country_code);
        editor.putString(Constant.MOBILE, mobile);
        editor.putString(Constant.BALANCE, balance);
        editor.putString(Constant.REFERRAL_CODE, referral_code);
        editor.putString(Constant.FCM_ID, fcm_id);
        editor.putString(Constant.STATUS, status);
        editor.putString(Constant.TYPE, type);
        editor.putString(Constant.DATE_CREATED, date_created);
        editor.commit();
    }

    public void logoutUser(Activity activity) {

        String mobile = getData(Constant.MOBILE);
        String password = getData(Constant.PASSWORD);

        editor.clear();
        editor.commit();

        setData(Constant.MOBILE, mobile);
        setData(Constant.PASSWORD, password);

        new Session(_activity).setBoolean("is_first_time", true);

        Intent i = new Intent(activity, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constant.FROM, "");
        activity.startActivity(i);
        activity.finish();
    }

    public void logoutUserConfirmation(final Activity activity) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(_activity);
        alertDialog.setTitle(R.string.logout);
        alertDialog.setMessage(R.string.logout_msg);
        alertDialog.setCancelable(false);
        final AlertDialog alertDialog1 = alertDialog.create();

        // Setting OK Button
        alertDialog.setPositiveButton(R.string.yes, (dialog, which) -> logoutUser(activity));

        alertDialog.setNegativeButton(R.string.no, (dialog, which) -> alertDialog1.dismiss());
        // Showing Alert Message
        alertDialog.show();

    }

    public void setLogInData(String affiliater_type, String user_id, String mobile, String password) {
        editor.putBoolean(Constant.IS_USER_LOGIN, true);
        editor.putString(Constant.USER_ID, user_id);
        editor.putString(Constant.MOBILE, mobile);
        editor.putString(Constant.PASSWORD, password);
        editor.putString(Constant.TYPE_USER, affiliater_type);
        editor.commit();
    }

    public void goBackWithData(Activity activity) {
        Intent intent = new Intent();
        if (getTypeUser().equals(Constant.MANAGER)) {
            intent = new Intent(activity, ManagerActivity.class);
        } else  if (getTypeUser().equals(Constant.MARKETER)) {
            intent = new Intent(activity, MarketerActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}