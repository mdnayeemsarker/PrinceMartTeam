package com.princemartbd.team.activity.Base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.princemartbd.team.R;
import com.princemartbd.team.adapter.HistoryAdapter;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.model.HistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    private Activity activity;
    private Session session;
    private String userType;

    private TextView historyMerTV, historyCusTV;

    private RecyclerView historyRV;
    private HistoryModel historyModel;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        define();
        work();
    }

    private void define() {

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("History");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        historyMerTV = findViewById(R.id.historyMerTV);
        historyCusTV = findViewById(R.id.historyCusTV);
        historyRV = findViewById(R.id.historyRV);

        userType = getIntent().getStringExtra("userType");

    }

    private void work() {
        viewChange(historyMerTV, historyCusTV, "sellers");
        historyMerTV.setOnClickListener(v -> viewChange(historyMerTV, historyCusTV, "sellers"));
        historyCusTV.setOnClickListener(v -> viewChange(historyCusTV, historyMerTV, "users"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        historyRV.setLayoutManager(layoutManager);
    }

    private void viewChange(TextView textView1, TextView textView2, String pageType) {

        getData(pageType);
        textView1.setClickable(false);
        textView2.setClickable(true);
        textView1.setTextColor(getColor(R.color.white));
        textView1.setBackgroundColor(getColor(R.color.gray));
        textView2.setTextColor(getColor(R.color.gray));
        textView2.setBackgroundColor(getColor(R.color.white));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getData(String type) {

        ArrayList<HistoryModel> historyModelArrayList = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, type);
        if (userType.equals("self"))
            params.put(Constant.USER_ID, session.getUserId());
        else
            params.put(Constant.USER_ID, userType);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        JSONArray data;
                        if (type.equals("users")) {
                            data = jsonObject.getJSONArray("users");
                        }else {
                            data = jsonObject.getJSONArray("sellers");
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject users = data.getJSONObject(i);

                            if (type.equals("users")){
                                historyModel = new HistoryModel(
                                        users.getString(Constant.ID),
                                        users.getString(Constant.NAME),
                                        "store_name",
                                        users.getString(Constant.PROFILE),
                                        users.getString(Constant.EMAIL),
                                        users.getString(Constant.MOBILE),
                                        users.getString(Constant.STATUS)
                                );
                            }else {
                                historyModel = new HistoryModel(
                                        users.getString(Constant.ID),
                                        users.getString(Constant.NAME),
                                        users.getString("store_name"),
                                        users.getString("logo"),
                                        users.getString(Constant.EMAIL),
                                        users.getString(Constant.MOBILE),
                                        users.getString(Constant.STATUS)
                                );
                            }
                            historyModelArrayList.add(historyModel);
                        }
                        historyAdapter = new HistoryAdapter(activity, historyModelArrayList);
                        historyRV.setAdapter(historyAdapter);
                    }
                    Log.d("history", "Try: " + jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("history", "Catch: " + e.getMessage());
                }
            }
        }, activity, Constant.HISTORY_URL, params, true);
    }
}