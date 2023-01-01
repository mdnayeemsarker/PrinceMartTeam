package com.princemartbd.team.activity.Manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.princemartbd.team.R;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddMarketerActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtLoginMobile;
    private EditText imgLoginPassword;

    public Activity activity;
    public Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marketer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Marketer");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        activity = this;
        session = new Session(activity);

        session.IS_LOGGED_IN(activity);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtLoginMobile = findViewById(R.id.edtLoginMobile);
        imgLoginPassword = findViewById(R.id.imgLoginPassword);


        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            String mobile = edtLoginMobile.getText().toString();
            String password = imgLoginPassword.getText().toString();


            if (TextUtils.isEmpty(name)) {
                edtName.requestFocus();
                edtName.setError("Name field is mandatory.!");
            } else if (TextUtils.isEmpty(email)) {
                edtEmail.requestFocus();
                edtEmail.setError("Email field is mandatory.!");
            } else if (TextUtils.isEmpty(mobile)) {
                edtLoginMobile.requestFocus();
                edtLoginMobile.setError("Mobile field is mandatory.!");
            } else if (mobile.length() != 11) {
                edtLoginMobile.requestFocus();
                edtLoginMobile.setError("Must Be Mobile Number is 11 Digit");
            } else if (TextUtils.isEmpty(password)) {
                imgLoginPassword.requestFocus();
                imgLoginPassword.setError("Password field is mandatory.!");
            } else {
                addMarketer(name, email, mobile, password);
            }

        });

    }

    public void addMarketer(String name, String email, String mobile, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, Constant.ADD_AFFILIATERS);
        params.put(Constant.NAME, name);
        params.put(Constant.EMAIL, email);
        params.put(Constant.MOBILE, mobile);
        params.put(Constant.USER_ID, session.getUserId());
        params.put(Constant.PASSWORD, password);
        params.put(Constant.COUNTRY_CODE, "88");
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        startActivity(new Intent(activity, ManagerActivity.class));
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("add_affiliate", response);
            }
        }, activity, Constant.AFFILIATER_ADD, params, true);
    }

}