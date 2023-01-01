package com.princemartbd.team.activity.Manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.princemartbd.team.R;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ManWithdrawActivity extends AppCompatActivity {

    private Activity activity;
    private EditText edtAmount;
    private Session session;
    private double main_balance;
    private double instant_balance;
    private Button btnRequest;
    private TextView tvTotalBalance;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_withdraw);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Manager Withdraw Request");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        activity = this;
        session = new Session(activity);

        session.IS_LOGGED_IN(activity);

        edtAmount = findViewById(R.id.edtAmount);
        btnRequest = findViewById(R.id.btnRequest);
        tvTotalBalance = findViewById(R.id.tvTotalBalance);

        String balance = session.getData("balance");
        if (!balance.equals("null")) {
            main_balance = Double.parseDouble(balance);
            tvTotalBalance.setText("Balance: " + main_balance);
        }

        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    try {
                        instant_balance = Double.parseDouble(s.toString());
                        double tempBalance = main_balance - instant_balance;
                        tvTotalBalance.setText("Total Balance: " + tempBalance);
                        if (instant_balance >= 20) {
                            btnRequest.setVisibility(View.VISIBLE);
                            btnRequest.setOnClickListener(v -> gettingAmount());
                        } else {
                            btnRequest.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "You can not use special Character \"" + s + "\" here.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    tvTotalBalance.setText("Total Balance: " + balance);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void gettingAmount() {

        if (main_balance > instant_balance) {

            String amount = String.valueOf(instant_balance);

            Map<String, String> params = new HashMap<>();
            params.put(Constant.PAGE, "send_request");
            params.put(Constant.USER_ID, session.getUserId());
            params.put(Constant.AMOUNT, amount);
            params.put(Constant.MESSAGE, "optional");
            ApiConfig.RequestToVolley((result, response) -> {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(Constant.ERROR)) {
                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(activity, ManagerActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("exception", e.getMessage());
                    }
                    Log.d("withdraw", response);
                }
            }, activity, Constant.WITHDRAW_REQUEST, params, true);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View customLayout = getLayoutInflater().inflate(R.layout.lyt_with_req_error, null);
            builder.setView(customLayout);
            Button ok = customLayout.findViewById(R.id.btnOk);

            AlertDialog dialog = builder.create();
            ok.setOnClickListener(v -> {
                dialog.dismiss();
                edtAmount.clearComposingText();
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}