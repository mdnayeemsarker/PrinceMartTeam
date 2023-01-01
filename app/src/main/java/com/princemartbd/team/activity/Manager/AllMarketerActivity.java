package com.princemartbd.team.activity.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.princemartbd.team.R;
import com.princemartbd.team.adapter.AllMarketerAdapter;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.model.AllMarketerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllMarketerActivity extends AppCompatActivity {

    private Activity activity;
    private Session session;
    private RecyclerView allMarketerRecyclerView;

    private AllMarketerAdapter allMarketerAdapter;
    private ArrayList<AllMarketerModel> allMarketerModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_marketer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All Coordinator");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        activity = this;
        session = new Session(activity);

        allMarketerRecyclerView = findViewById(R.id.allMarketerRecyclerViewId);

        if (ApiConfig.isConnected(activity)){

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            allMarketerRecyclerView.setLayoutManager(layoutManager);

            allMarketerModelArrayList = new ArrayList<>();
            allMarketerAdapter = new AllMarketerAdapter(activity, allMarketerModelArrayList);

            getData();
        }

    }

    private void getData() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, Constant.ALL_AFFILIATERS);
        params.put(Constant.USER_ID, session.getUserId());

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        JSONArray data = jsonObject.getJSONArray(Constant.AFFILIATERS);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject affiliaters = data.getJSONObject(i);

                            AllMarketerModel allMarketerModel = new AllMarketerModel(
                                    affiliaters.getString(Constant.ID),
                                    affiliaters.getString(Constant.ADDED_BY),
                                    affiliaters.getString(Constant.NAME),
                                    affiliaters.getString(Constant.EMAIL),
                                    affiliaters.getString(Constant.COUNTRY_CODE),
                                    affiliaters.getString(Constant.MOBILE),
                                    affiliaters.getString(Constant.BALANCE),
                                    affiliaters.getString(Constant.REFERRAL_CODE),
                                    affiliaters.getString(Constant.FCM_ID),
                                    affiliaters.getString(Constant.STATUS),
                                    affiliaters.getString(Constant.TYPE),
                                    affiliaters.getString(Constant.DATE_CREATED)
                                );
                            allMarketerModelArrayList.add(allMarketerModel);
                        }
                        allMarketerAdapter = new AllMarketerAdapter(activity, allMarketerModelArrayList);
                        allMarketerRecyclerView.setAdapter(allMarketerAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d(Constant.ALL_AFFILIATERS, response);
        }, activity, Constant.ALL_MARKETERS, params, true);
    }
}