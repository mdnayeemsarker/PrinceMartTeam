package com.princemartbd.team.activity.Manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class ManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    private LinearLayout manageLayout;
    private LinearLayout earningLayout;
    private LinearLayout withdrawLayout;

    private Boolean isBalance = false;
    private Boolean isMTeam = false;
    private Boolean isEarning = false;
    private Boolean isWithdraw = false;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Manager Earning");
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

        CardView totalBalanceCardView = findViewById(R.id.totalBalanceCardViewId);
        CardView manageTeamCardView = findViewById(R.id.manageTeamCardViewId);
        CardView earningCardView = findViewById(R.id.earningCardViewId);
        CardView withdrawCardView = findViewById(R.id.withdrawCardViewId);

        manageLayout = findViewById(R.id.manageLayoutId);
        earningLayout = findViewById(R.id.earningLayoutId);
        withdrawLayout = findViewById(R.id.withdrawLayoutId);

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
            findViewById(R.id.addMarketerCardViewId).setOnClickListener(v -> nextPage(Constant.ADD_MARKETER));
            findViewById(R.id.allMarketerCardViewId).setOnClickListener(v -> nextPage(Constant.ALL_AFFILIATERS));
            findViewById(R.id.transactionCardViewId).setOnClickListener(v -> nextPage(Constant.TRANSACTION));
            findViewById(R.id.withdrawRequestCardId).setOnClickListener(v -> nextPage(Constant.WITHDRAW));
            findViewById(R.id.withdrawHistoryCardId).setOnClickListener(v -> nextPage(Constant.MAN_WITHDRAW));

            totalBalanceCardView.setOnClickListener(v -> taskClick(Constant.TOTAL_BALANCE));
            manageTeamCardView.setOnClickListener(v -> taskClick(Constant.MANAGE_TEAM));
            earningCardView.setOnClickListener(v -> taskClick(Constant.EARNING));
            withdrawCardView.setOnClickListener(v -> taskClick(Constant.WITHDRAW));

            getData();

            timerCloseView();
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

    private void taskClick(String type) {

        switch (type) {
            case Constant.TOTAL_BALANCE:
                balanceView();
                manageTeamClose();
                earningViewClose();
                withdrawViewClose();
                break;
            case Constant.MANAGE_TEAM:
                manageTeamView();
                balanceViewClose();
                earningViewClose();
                withdrawViewClose();
                break;
            case Constant.EARNING:
                earningView();
                balanceViewClose();
                manageTeamClose();
                withdrawViewClose();
                break;
            case Constant.WITHDRAW:
                withdrawView();
                balanceViewClose();
                manageTeamClose();
                earningViewClose();
                break;
        }
    }

    private void timerCloseView() {
        new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                totalBalanceTextView.setVisibility(View.GONE);
                manageLayout.setVisibility(View.GONE);
                earningLayout.setVisibility(View.GONE);
            }
        }.start();
    }

    private void withdrawView() {
        if (!isWithdraw) {
            isWithdraw = true;
            withdrawLayout.setVisibility(View.VISIBLE);
        } else {
            isWithdraw = false;
            withdrawLayout.setVisibility(View.GONE);
        }
    }

    private void withdrawViewClose() {
        isWithdraw = false;
        withdrawLayout.setVisibility(View.GONE);
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

    private void manageTeamView() {
        if (!isMTeam) {
            isMTeam = true;
            manageLayout.setVisibility(View.VISIBLE);
        } else {
            isMTeam = false;
            manageLayout.setVisibility(View.GONE);
        }
    }

    private void manageTeamClose() {
        isMTeam = false;
        manageLayout.setVisibility(View.GONE);
    }

    private void balanceView() {
        if (!isBalance) {
            isBalance = true;
            totalBalanceTextView.setVisibility(View.VISIBLE);
        } else {
            isBalance = false;
            totalBalanceTextView.setVisibility(View.GONE);
        }
    }

    private void balanceViewClose() {
        isBalance = false;
        totalBalanceTextView.setVisibility(View.GONE);
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
            case R.id.navAddMarketerId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.ADD_MARKETER);
                break;
            case R.id.navAllMarketerId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.ALL_AFFILIATERS);
                break;
            case R.id.navWithdrawRequestId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.WITHDRAW);
                break;
            case R.id.navWithdrawDashboardId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.ManWithDashboard);
                break;
            case R.id.navWithdrawHistoryId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.MAN_WITHDRAW);
                break;
            case R.id.navTransactionHistoryId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.TRANSACTION);
                break;
            case R.id.navProfileId:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.ManProfileActivity);
                break;
            case R.id.navStatement:
                drawer.closeDrawer(GravityCompat.START);
                nextPage(Constant.MANSTATEMENTActivity);
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

    private void nextPage(String type) {

        if (ApiConfig.isConnected(activity)) {

            Intent intent = null;
            switch (type) {
                case Constant.TRANSACTION:
                    intent = new Intent(activity, ManTransactionsActivity.class);
                    break;
                case Constant.ALL_AFFILIATERS:
                    intent = new Intent(activity, AllMarketerActivity.class);
                    break;
                case Constant.ADD_MARKETER:
                    intent = new Intent(activity, AddMarketerActivity.class);
                    break;
                case Constant.WITHDRAW:
                    intent = new Intent(activity, ManWithdrawActivity.class);
                    break;
                case Constant.MAN_WITHDRAW:
                    intent = new Intent(activity, ManWithHistoryActivity.class);
                    break;
                case Constant.ManWithDashboard:
                    intent = new Intent(activity, ManWithDashActivity.class);
                    break;
                case Constant.ManProfileActivity:
                    intent = new Intent(activity, ManProfileActivity.class);
                    break;
                case Constant.MANSTATEMENTActivity:
                    intent = new Intent(activity, ManSummeryActivity.class);
                    break;
            }
            startActivity(intent);
            balanceViewClose();
            manageTeamClose();
            earningViewClose();
            withdrawViewClose();
        }
    }
}