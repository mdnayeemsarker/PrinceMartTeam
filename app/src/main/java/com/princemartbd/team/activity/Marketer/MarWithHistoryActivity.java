package com.princemartbd.team.activity.Marketer;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.princemartbd.team.R;
import com.princemartbd.team.adapter.ManWithAdapter;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.model.ManWithModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MarWithHistoryActivity extends AppCompatActivity {

    public Activity activity;
    public Session session;

    private RecyclerView recyclerView;

    private ManWithAdapter manWithAdapter;
    private ArrayList<ManWithModel> manWithModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_with_history);

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Coordinator Withdraw History");
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        session.IS_LOGGED_IN(activity);

        recyclerView = findViewById(R.id.recyclerViewId);

        if (ApiConfig.isConnected(activity)) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            manWithModelArrayList = new ArrayList<>();
            manWithAdapter = new ManWithAdapter(activity, manWithModelArrayList);

            findViewById(R.id.withdrawHistoryDashId).setOnClickListener(v -> startActivity(new Intent(activity, MarWithDashActivity.class)));

            getData();
        }
    }

    private void getData() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, "get_requests");
        params.put(Constant.USER_ID, session.getUserId());

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
//                        String total = jsonObject.getString("total");
//                        Toast.makeText(activity, total, Toast.LENGTH_SHORT).show();
                        JSONArray data = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject withdrawHistory = data.getJSONObject(i);

                            ManWithModel manWithModel = new ManWithModel(
                                    withdrawHistory.getString(Constant.ID),
                                    withdrawHistory.getString(Constant.TYPE),
                                    withdrawHistory.getString(Constant.TYPE_ID),
                                    withdrawHistory.getString(Constant.AMOUNT),
                                    withdrawHistory.getString(Constant.MESSAGE),
                                    withdrawHistory.getString(Constant.STATUS),
                                    withdrawHistory.getString(Constant.LAST_UPDATED),
                                    withdrawHistory.getString(Constant.DATE_CREATED)
                            );
                            manWithModelArrayList.add(manWithModel);
                        }
                        manWithAdapter = new ManWithAdapter(activity, manWithModelArrayList);
                        recyclerView.setAdapter(manWithAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exception", e.getMessage());
                }
            }
            Log.d("get_requests", response);
        }, activity, Constant.WITHDRAW_REQUEST, params, true);
    }
}