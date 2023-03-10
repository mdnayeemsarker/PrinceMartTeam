package com.princemartbd.team.activity.Manager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ManSummeryActivity extends AppCompatActivity {

    private Activity activity;
    private Session session;
    private RecyclerView summeryRV;

    private String startDate;
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_summery);

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        summeryRV = findViewById(R.id.summeryRV);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.marketer) + " list");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        summeryRV.setLayoutManager(layoutManager);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH)+1; // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        endDate = mYear + "-" + mMonth + "-" + mDay;
        if (mMonth != 1)
            startDate = mYear + "-" + (mMonth - 1) + "-" + mDay;
        else
            startDate = mYear + "-1-" + mDay;


        Log.d( "date", "Start "+ startDate + "\nend " + endDate + mYear + mMonth + mDay);
        getData();

    }

    private void getData() {

        ArrayList<SummeryModel> summeryModelArrayList = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, "coordinators");
        params.put(Constant.USER_ID, session.getUserId());
        params.put("start_date", startDate);
        params.put("end_date", endDate);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
//                Log.d("statement", "Res if: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        Log.d("statement", "Try if: " + jsonObject);
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            summeryModelArrayList.add(new SummeryModel(data.getString(Constant.ID), data.getString(Constant.NAME), data.getString(Constant.PROFILE), data.getString(Constant.BALANCE), data.getString("income"), "coordinators", "0"));
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