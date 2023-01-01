package com.princemartbd.team.activity.Base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.princemartbd.team.R;
import com.princemartbd.team.activity.Manager.ManagerActivity;
import com.princemartbd.team.activity.Marketer.MarketerActivity;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.helper.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText imgLoginPassword, edtLoginMobile;

    TextView tvMobile, tvForgotPass;
    Session session;

    Activity activity;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = LoginActivity.this;
        session = new Session(activity);

        Button btnLogin = findViewById(R.id.btnLogin);

        imgLoginPassword = findViewById(R.id.imgLoginPassword);
        edtLoginMobile = findViewById(R.id.edtLoginMobile);
        tvMobile = findViewById(R.id.tvMobile);
        tvForgotPass = findViewById(R.id.tvForgotPass);

        edtLoginMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);

        imgLoginPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pass, 0, R.drawable.ic_show, 0);

        if (!session.getData(Constant.MOBILE).equals("")){
            edtLoginMobile.setText(session.getData(Constant.MOBILE));
        }

        Utils.setHideShowPassword(imgLoginPassword);

        onStart();

        btnLogin.setOnClickListener(this::getInput);

        findViewById(R.id.btnSignup).setOnClickListener(v -> {
            Intent intent = new Intent(activity, SignupActivity.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.tvForgotPass).setOnClickListener(v -> {
            Intent intent = new Intent(activity, ForgotPassActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getInput(View view) {
        ApiConfig.hideKeyboard(activity, view);
        String mobile = edtLoginMobile.getText().toString();
        String password = imgLoginPassword.getText().toString();

        if (TextUtils.isEmpty(mobile)){
            edtLoginMobile.requestFocus();
            edtLoginMobile.setError("Mobile number is mandatory");
        }else if (TextUtils.isEmpty(password)){
            imgLoginPassword.requestFocus();
            imgLoginPassword.setError("Mobile number is mandatory");
        }else {
            UserLogin(mobile, password);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ApiConfig.isConnected(activity)) {
            if (session.getIsLog()) {
                Intent intent = new Intent();
                if (session.getTypeUser().equals(Constant.MANAGER)) {
                    intent = new Intent(activity, ManagerActivity.class);
                } else  if (session.getTypeUser().equals(Constant.MARKETER)) {
                    intent = new Intent(activity, MarketerActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    public void UserLogin(String mobile, String password) {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, Constant.LOGIN);
        params.put(Constant.MOBILE, mobile);
        params.put(Constant.PASSWORD, password);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        if (jsonObject.getString("affiliater_type").equals(Constant.MANAGER)) {
                            JSONObject manager = jsonObject.getJSONObject(Constant.MANAGER);
                            new Session(activity).setLogInData(jsonObject.getString("affiliater_type"), manager.getString(Constant.USER_ID), mobile, password);
                            startActivity(new Intent(activity, ManagerActivity.class));
                        }else {
                            JSONObject manager = jsonObject.getJSONObject(Constant.MARKETER);
                            new Session(activity).setLogInData(jsonObject.getString("affiliater_type"), manager.getString(Constant.USER_ID), mobile, password);
                            startActivity(new Intent(activity, MarketerActivity.class));
                        }
                    }
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exception", e.getMessage());
                }
            }
            Log.d("response", response);
        }, activity, Constant.AUTH_LOGIN_URL, params, true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}