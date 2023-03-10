package com.princemartbd.team.activity.Marketer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.princemartbd.team.R;
import com.princemartbd.team.adapter.SummeryAdapter;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.model.SummeryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MarSummeryActivity extends AppCompatActivity {

    private Activity activity;
    private Session session;
    private RecyclerView summeryRV;

    private String startDate;
    private String endDate;
    private String userType;
    private TextView historyMerTV, historyCusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_summery);

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        summeryRV = findViewById(R.id.summeryRV);
        historyMerTV = findViewById(R.id.historyMerTV);
        historyCusTV = findViewById(R.id.historyCusTV);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        userType = getIntent().getStringExtra("userType");

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        summeryRV.setLayoutManager(layoutManager);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH)+1; // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        startDate = mYear + "-" + mMonth + "-" + mDay;
        if (mMonth != 1)
            endDate = mYear + "-" + (mMonth - 1) + "-" + mDay;
        else
            endDate = mYear + "-1-" + mDay;

        viewChange(historyMerTV, historyCusTV, "sellers", "Merchant list");
        historyMerTV.setOnClickListener(v -> viewChange(historyMerTV, historyCusTV, "sellers", "Merchant list"));
        historyCusTV.setOnClickListener(v -> viewChange(historyCusTV, historyMerTV, "users", "Customer list"));

    }

    private void viewChange(TextView textView1, TextView textView2, String pageType, String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        getData(pageType);
        textView1.setClickable(false);
        textView2.setClickable(true);
        textView1.setTextColor(getColor(R.color.white));
        textView1.setBackgroundColor(getColor(R.color.gray));
        textView2.setTextColor(getColor(R.color.gray));
        textView2.setBackgroundColor(getColor(R.color.white));

    }

    private void getData(String page) {

        ArrayList<SummeryModel> summeryModelArrayList = new ArrayList<>();
        String userId;
        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        if (userType.equals("self")) {
            userId = session.getUserId();
            params.put(Constant.USER_ID, session.getUserId());
        } else {
            userId = userType;
            params.put(Constant.USER_ID, userType);
        }
        params.put("start_date", startDate);
        params.put("end_date", endDate);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        Log.d("statement", "Try if: " + jsonObject);
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            if (page.equals("sellers"))
                                summeryModelArrayList.add(new SummeryModel(data.getString(Constant.ID), data.getString(Constant.NAME), data.getString("logo"), data.getString(Constant.BALANCE), data.getString("income"), "seller", userId));
                            else
                                summeryModelArrayList.add(new SummeryModel(data.getString(Constant.ID), data.getString(Constant.NAME), data.getString("profile"), data.getString(Constant.BALANCE), data.getString("income"), "users", userId));
                        }

                        SummeryAdapter adapter = new SummeryAdapter(activity, summeryModelArrayList);
                        summeryRV.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("statement", "Catch: " + e.getMessage());
                }
            }
        }, activity, Constant.SUMMARY_URL, params, true);
    }

}