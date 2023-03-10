package com.princemartbd.team.activity.Base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.princemartbd.team.R;
import com.princemartbd.team.adapter.StatementAdapter;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.model.StatementModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatementActivity extends AppCompatActivity {

    private Activity activity;
    private Session session;
    private String startDate;
    private String endDate;
    private RecyclerView statementRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Statement");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String page = getIntent().getStringExtra(Constant.PAGE);
        String type = getIntent().getStringExtra(Constant.TYPE);
        String typeId = getIntent().getStringExtra(Constant.TYPE_ID);
        String userId = getIntent().getStringExtra(Constant.USER_ID);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH)+1; // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        endDate = mYear + "-" + mMonth + "-" + mDay;
        if (mMonth != 1)
            startDate = mYear + "-" + (mMonth - 1) + "-" + mDay;
        else
            startDate = mYear + "-1-" + mDay;

        statementRV = findViewById(R.id.statementRV);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        statementRV.setLayoutManager(layoutManager);

        getData(page, type, typeId, userId);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getData(String page, String type, String typeId, String userId) {

        ArrayList<StatementModel> statementModelArrayList = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, page);
        params.put(Constant.TYPE, type);
        params.put(Constant.TYPE_ID, typeId);
        params.put(Constant.USER_ID, userId);
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                Log.d("statement", "Res if: " + params + "\n" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        Log.d("statement", "Try if: " + jsonObject );
                        JSONArray data = jsonObject.getJSONArray(Constant.TYPE_TRANSACTION);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject transactions = data.getJSONObject(i);

                            StatementModel statementModel = new StatementModel(
                                    transactions.getString(Constant.ID),
                                    transactions.getString("paid_to"),
                                    transactions.getString("paid_from"),
                                    transactions.getString("trx_type"),
                                    transactions.getString("trx"),
                                    transactions.getString("amount"),
                                    transactions.getString("post_balance"),
                                    transactions.getString("status"),
                                    transactions.getString("date_created"),
                                    transactions.getString("last_updated")
                            );
                            statementModelArrayList.add(statementModel);
                        }
                        StatementAdapter adapter = new StatementAdapter(activity, statementModelArrayList);
                        statementRV.setAdapter(adapter);
                    }else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("statement", "Catch: " + e.getMessage());
                }
            }
        }, activity, Constant.MANAGE_TRANSACTIONS_URL, params, true);

    }

}