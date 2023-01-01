package com.princemartbd.team.activity.Marketer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
//import android.widget.Toast;

import com.princemartbd.team.R;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MarWithDashActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_with_dash);

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Coordinator Withdraw Dashboard");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        session.IS_LOGGED_IN(activity);

        totalBalanceTextView = findViewById(R.id.totalBalanceTextViewId);
        todayBalanceTextView = findViewById(R.id.todayBalanceTextViewId);
        yesterdayBalanceTextView = findViewById(R.id.yesterdayBalanceTextViewId);
        weeklyBalanceTextView = findViewById(R.id.weeklyBalanceTextViewId);
        monthlyBalanceTextView = findViewById(R.id.monthlyBalanceTextViewId);
        lastMonthBalanceTextView = findViewById(R.id.lastMonthBalanceTextViewId);
        last3MonthBalanceTextView = findViewById(R.id.last3MonthBalanceTextViewId);
        yearlyBalanceTextView = findViewById(R.id.yearlyBalanceTextViewId);
        lastYearBalanceTextView = findViewById(R.id.lastYearBalanceTextViewId);

        if (ApiConfig.isConnected(activity)){
            getData();
        }
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

                        totalBalanceTextView.setText(totalBalance);

                        if (!todayEarning.equals("null")){
                            todayBalanceTextView.setText(todayEarning);
                        }
                        if (!yesterdayEarning.equals("null")){
                            yesterdayBalanceTextView.setText(yesterdayEarning);
                        }
                        if (!weeklyEarning.equals("null")){
                            weeklyBalanceTextView.setText(weeklyEarning);
                        }
                        if (!monthlyEarning.equals("null")){
                            monthlyBalanceTextView.setText(monthlyEarning);
                        }
                        if (!lastMonthlyEarning.equals("null")){
                            lastMonthBalanceTextView.setText(lastMonthlyEarning);
                        }
                        if (!last3MonthlyEarning.equals("null")){
                            last3MonthBalanceTextView.setText(last3MonthlyEarning);
                        }
                        if (!yearlyEarning.equals("null")){
                            yearlyBalanceTextView.setText(yearlyEarning);
                        }
                        if (!lastYearlyEarning.equals("null")){
                            lastYearBalanceTextView.setText(lastYearlyEarning);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Exception", e.getMessage());
                }
            }
            Log.d("data_get_requests", response);
        }, activity, Constant.WITHDRAW_REQUEST, params, true);
    }
}