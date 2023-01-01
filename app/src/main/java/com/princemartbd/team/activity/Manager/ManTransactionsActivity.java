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
import com.princemartbd.team.adapter.ManTranAdapter;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.model.ManTranModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ManTransactionsActivity extends AppCompatActivity {

    public Activity activity;
    public Session session;

    private RecyclerView manTransactionsRecyclerView;

    private ManTranAdapter manTranAdapter;
    private ArrayList<ManTranModel> manTranModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_transactions);

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Manager Transactions");
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        session.IS_LOGGED_IN(activity);

        manTransactionsRecyclerView = findViewById(R.id.manTransactionsRecyclerViewId);

        if (ApiConfig.isConnected(activity)){

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            manTransactionsRecyclerView.setLayoutManager(layoutManager);

            manTranModelArrayList = new ArrayList<>();
            manTranAdapter = new ManTranAdapter(activity, manTranModelArrayList);

            getData();
        }
    }

    private void getData() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, "all_transactions");
        params.put(Constant.USER_ID, session.getUserId());

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        JSONArray data = jsonObject.getJSONArray(Constant.TYPE_TRANSACTION);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject transactions = data.getJSONObject(i);

                            ManTranModel manTranModel = new ManTranModel(
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
                            manTranModelArrayList.add(manTranModel);
                        }
                        manTranAdapter = new ManTranAdapter(activity, manTranModelArrayList);
                        manTransactionsRecyclerView.setAdapter(manTranAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            Log.d("all_transactions", response);
        }, activity, Constant.ALL_TRANSACTIONS, params, true);
    }
}