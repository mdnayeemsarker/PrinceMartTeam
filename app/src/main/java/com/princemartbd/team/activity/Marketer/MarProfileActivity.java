package com.princemartbd.team.activity.Marketer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.princemartbd.team.R;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MarProfileActivity extends AppCompatActivity {

    private Activity activity;
    private Session session;

    private TextView nameTv;
    private TextView emailTv;
    private TextView mobileTv;
    private TextView balanceTv;
    private TextView referralCodeTv;

    private Button referralCodeCopy;
    private Button referralCodeShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Team Coordinator Profile");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        activity = this;
        session = new Session(activity);

        nameTv = findViewById(R.id.nameId);
        emailTv = findViewById(R.id.emailId);
        mobileTv = findViewById(R.id.mobileId);
        balanceTv = findViewById(R.id.balanceId);
        referralCodeTv = findViewById(R.id.referralCodeId);
        referralCodeCopy = findViewById(R.id.referralCodeCopyId);
        referralCodeShare = findViewById(R.id.referralCodeShareId);

        if (ApiConfig.isConnected(activity)) {
            findViewById(R.id.withdrawId).setOnClickListener(v -> {
                startActivity(new Intent(activity, MarWithdrawActivity.class));
                finish();
            });
            getDate();
        }
    }

    private void getDate() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, Constant.PROFILE);
        params.put(Constant.USER_ID, session.getUserId());
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        JSONObject data = jsonObject.getJSONObject(Constant.PROFILE);

                        String added_by = data.getString("added_by");
                        String name = data.getString("name");
                        String email = data.getString("email");
                        String country_code = data.getString("country_code");
                        String mobile = data.getString("mobile");
                        String balance = data.getString("balance");
                        String referral_code = data.getString("referral_code");
                        String fcm_id = data.getString("fcm_id");
                        String status = data.getString("status");
                        String type = data.getString("type");
                        String date_created = data.getString("date_created");

                        if (!name.equals("null")){
                            nameTv.setText(name);
                        }
                        if (!email.equals("null")){
                            emailTv.setText(email);
                        }
                        if (!mobile.equals("null")){
                            mobileTv.setText(mobile);
                        }
                        if (!balance.equals("null")){
                            balanceTv.setText(balance);
                        }
                        if (!referral_code.equals("null")){
                            referralCodeTv.setText(referral_code);
                            referralCodeCopy.setOnClickListener(v -> {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", referral_code);
                                clipboard.setPrimaryClip(clip);
                            });
                            referralCodeShare.setOnClickListener(v -> {
                                String shareBody = "Hi, I'm \"" + name + "\" \nI'm using Prince Mart BD Big Online Shopping Platform. You also try this. Apps link: https://play.google.com/store/apps/details?id="
                                        + activity.getPackageName() + "\nHere is my referral code \"" + referral_code + "\"\n"+ "Thank You";
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                                sendIntent.setType("text/plain");
                                Intent.createChooser(sendIntent, "Share via");
                                startActivity(sendIntent);
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("profile", response);
        }, activity, Constant.DASHBOARD, params, true);
    }
}