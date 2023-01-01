package com.princemartbd.team.activity.Marketer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.princemartbd.team.BuildConfig;
import com.princemartbd.team.R;
import com.princemartbd.team.activity.Base.WebViewActivity;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MarketerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Activity activity;
    public Session session;

    private TextView totalBalanceTextView;
    private TextView todayBalanceTextView;
    private TextView yesterdayBalanceTextView;
    private TextView weeklyBalanceTextView;
    private TextView monthlyBalanceTextView;
    private TextView lastMonthBalanceTextView;
    private TextView last3MonthBalanceTextView;
    private TextView yearlyBalanceTextView;
    private TextView lastYearBalanceTextView;

    private LinearLayout earningLayout;

    private Boolean isEarning = false;
    private Intent intent;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Coordinator Earning");
        }

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        activity = this;
        session = new Session(activity);

        session.IS_LOGGED_IN(activity);

        CardView earningCardView = findViewById(R.id.earningCardViewId);

        earningLayout = findViewById(R.id.earningLayoutId);

        totalBalanceTextView = findViewById(R.id.totalBalanceTextViewId);
        todayBalanceTextView = findViewById(R.id.todayBalanceTextViewId);
        yesterdayBalanceTextView = findViewById(R.id.yesterdayBalanceTextViewId);
        weeklyBalanceTextView = findViewById(R.id.weeklyBalanceTextViewId);
        monthlyBalanceTextView = findViewById(R.id.monthlyBalanceTextViewId);
        lastMonthBalanceTextView = findViewById(R.id.lastMonthBalanceTextViewId);
        last3MonthBalanceTextView = findViewById(R.id.last3MonthBalanceTextViewId);
        yearlyBalanceTextView = findViewById(R.id.yearlyBalanceTextViewId);
        lastYearBalanceTextView = findViewById(R.id.lastYearBalanceTextViewId);

        if (ApiConfig.isConnected(activity)) {
            findViewById(R.id.transactionCardViewId).setOnClickListener(v -> nextPage(Constant.MarTransactionsActivity));
            findViewById(R.id.withdrawRequestCardId).setOnClickListener(v -> nextPage(Constant.MarWithdrawActivity));
            findViewById(R.id.withdrawHistoryCardId).setOnClickListener(v -> nextPage(Constant.MarWithHistoryActivity));

            earningCardView.setOnClickListener(v -> taskClick(Constant.EARNING));

            getData();
            getPersonalData();
        }
        getOnlineVersion();
    }

    private void getOnlineVersion() {
        Map<String, String> params = new HashMap<>();
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String onlinePackageID = data.getString("packageID");
                        String onlineVersionName = data.getString("versionName");
                        if (activity.getPackageName().equals(onlinePackageID)){
                            String versionName = BuildConfig.VERSION_NAME;
                            if (!onlineVersionName.equals(versionName)){
                                ApiConfig.newUpdate(activity);
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.d("app_update", e.getMessage());
                }
            }
        }, activity, Constant.APP_UPDATE_URL, params, false);
    }

    private void getPersonalData() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, Constant.PROFILE);
        params.put(Constant.USER_ID, session.getUserId());
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        JSONObject data = jsonObject.getJSONObject(Constant.PROFILE);

                        String id = data.getString("id");
                        String added_by = data.getString("added_by");
                        String name = data.getString("name");
                        String email = data.getString("email");
                        String country_code = data.getString("country_code");
                        String mobile = data.getString("mobile");
                        String balance = data.getString("balance");
                        String referral_code = data.getString("referral_code");
                        String fcm_id = data.getString("fcm_id");
                        String status = data.getString("status");
                        String type = data.getString("type");
                        String date_created = data.getString("date_created");

                        session.setUserData(id, added_by, name, email, country_code, mobile, balance, referral_code, fcm_id, status, type, date_created);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("profile_exception", e.getMessage());
                }
            }
            Log.d("profile_response", response);
        }, activity, Constant.DASHBOARD, params, true);
    }

    private void taskClick(@NonNull String type) {
        if (type.equals(Constant.EARNING)) {
            earningView();
        }
    }

    private void earningView() {
        if (!isEarning) {
            isEarning = true;
            earningLayout.setVisibility(View.VISIBLE);
        } else {
            isEarning = false;
            earningLayout.setVisibility(View.GONE);
        }
    }

    private void earningViewClose() {
        isEarning = false;
        earningLayout.setVisibility(View.GONE);
    }

    private void getData() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, Constant.DATA);
        params.put(Constant.USER_ID, session.getUserId());
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        JSONObject data = jsonObject.getJSONObject(Constant.DATA);

                        String totalBalance = data.getString("totalBalance");
                        String todayEarning = data.getString("todayEarning");
                        String yesterdayEarning = data.getString("yesterdayEarning");
                        String weeklyEarning = data.getString("weeklyEarning");
                        String monthlyEarning = data.getString("monthlyEarning");
                        String lastMonthlyEarning = data.getString("lastMonthlyEarning");
                        String last3MonthlyEarning = data.getString("last3MonthlyEarning");
                        String yearlyEarning = data.getString("yearlyEarning");
                        String lastYearlyEarning = data.getString("lastYearlyEarning");

                        if (!totalBalance.equals("null")) {
                            totalBalanceTextView.setText(totalBalance);
                            session.setData("balance", totalBalance);
                        }

                        if (!todayEarning.equals("null")) {
                            todayBalanceTextView.setText(todayEarning);
                        }
                        if (!yesterdayEarning.equals("null")) {
                            yesterdayBalanceTextView.setText(yesterdayEarning);
                        }
                        if (!weeklyEarning.equals("null")) {
                            weeklyBalanceTextView.setText(weeklyEarning);
                        }
                        if (!monthlyEarning.equals("null")) {
                            monthlyBalanceTextView.setText(monthlyEarning);
                        }
                        if (!lastMonthlyEarning.equals("null")) {
                            lastMonthBalanceTextView.setText(lastMonthlyEarning);
                        }
                        if (!last3MonthlyEarning.equals("null")) {
                            last3MonthBalanceTextView.setText(last3MonthlyEarning);
                        }
                        if (!yearlyEarning.equals("null")) {
                            yearlyBalanceTextView.setText(yearlyEarning);
                        }
                        if (!lastYearlyEarning.equals("null")) {
                            lastYearBalanceTextView.setText(lastYearlyEarning);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException", e.getMessage());
                }
            }
            Log.d("data", response);
        }, activity, Constant.DASHBOARD, params, true);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navProfileId:
                drawer.closeDrawer(GravityCompat.START);
                if (ApiConfig.isConnected(activity)) {
                    startActivity(new Intent(activity, MarProfileActivity.class));
                }
                break;
            case R.id.navWithdrawRequestId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.MarWithdrawActivity);
                break;
            case R.id.navWithdrawHistoryId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.MarWithHistoryActivity);
                break;
            case R.id.navWithdrawDashboardId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.MarWithDashActivity);
                break;
            case R.id.navTransactionHistoryId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.MarTransactionsActivity);
                break;
            case R.id.navPrivacyPolicyId:
                drawer.closeDrawer(GravityCompat.START);
                if (ApiConfig.isConnected(activity)) {
                    startActivity(new Intent(activity, WebViewActivity.class).putExtra("type", "Privacy Policy"));
                }
                break;
            case R.id.navTermsAndConditionId:
                drawer.closeDrawer(GravityCompat.START);
                if (ApiConfig.isConnected(activity)) {
                    startActivity(new Intent(activity, WebViewActivity.class).putExtra("type", "Terms & Conditions"));
                }
                break;
            case R.id.navAboutUsId:
                drawer.closeDrawer(GravityCompat.START);
                if (ApiConfig.isConnected(activity)) {
                    startActivity(new Intent(activity, WebViewActivity.class).putExtra("type", "About Us"));
                }
                break;
            case R.id.navLogOutId:
                drawer.closeDrawer(GravityCompat.START);
                if (ApiConfig.isConnected(activity)) {
                    session.logoutUserConfirmation(activity);
                }
                break;
        }
        return false;
    }

    private void nextPage(@NonNull String data) {
        if (ApiConfig.isConnected(activity)) {
            switch (data) {
                case Constant.MarTransactionsActivity:
                    intent = new Intent(activity, MarTransactionsActivity.class);
                    break;
                case Constant.MarWithdrawActivity:
                    intent = new Intent(activity, MarWithdrawActivity.class);
                    break;
                case Constant.MarWithHistoryActivity:
                    intent = new Intent(activity, MarWithHistoryActivity.class);
                    break;
                case Constant.MarWithDashActivity:
                    intent = new Intent(activity, MarWithDashActivity.class);
                    break;
            }
            startActivity(intent);
            earningViewClose();
        }
    }
}