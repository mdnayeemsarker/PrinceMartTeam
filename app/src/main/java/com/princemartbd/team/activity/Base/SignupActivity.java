package com.princemartbd.team.activity.Base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.princemartbd.team.R;
import com.princemartbd.team.helper.ApiConfig;
import com.princemartbd.team.helper.Constant;
import com.princemartbd.team.helper.Session;
import com.princemartbd.team.ui.PinView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail;
    private EditText imgLoginPassword;
    private EditText imgLoginConPassword;
    private EditText referralCodeEdit;

    public Activity activity;
    public Session session;

    private String type = "";
    private String referralCode = "";
    private String mobile = "";

    private ScrollView lytVerify;
    private ScrollView lytSignup;
    private LinearLayout lytOTP;

    private EditText edtMobileVerify;

    boolean resendOTP = false;
    boolean timerOn;

    private PinView pinViewOTP;

    private Button btnVerify;

//    CountryCodePicker edtCountryCodePicker;
    TextView tvTimer, tvResend;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        activity = this;
        session = new Session(activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(activity, LoginActivity.class));
            finish();
        });

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        imgLoginPassword = findViewById(R.id.imgLoginPassword);
        imgLoginConPassword = findViewById(R.id.imgLoginConPassword);
        referralCodeEdit = findViewById(R.id.referralCodeEditId);
        LinearLayout referralCodeLayout = findViewById(R.id.referralCodeLayoutId);

        lytVerify = findViewById(R.id.lytVerify);
        lytSignup = findViewById(R.id.lytSignup);
        lytOTP = findViewById(R.id.lytOTP);

        pinViewOTP = findViewById(R.id.pinViewOTP);

        tvResend = findViewById(R.id.tvResend);
        tvTimer = findViewById(R.id.tvTimer);
//        edtCountryCodePicker = findViewById(R.id.edtCountryCodePicker);
//        edtCountryCodePicker.setCountryForNameCode("BD");

        edtMobileVerify = findViewById(R.id.edtMobileVerify);

        btnVerify = findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(v -> {
            ApiConfig.hideKeyboard(activity, v);
            checkNumber();
        });

        Button btnSignup = findViewById(R.id.btnSignup);

        RadioButton managerRadio = findViewById(R.id.managerRadioId);
        RadioButton marketerRadio = findViewById(R.id.marketerRadioId);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        managerRadio.setOnClickListener(v ->  {
            type = "1";
            referralCodeLayout.setVisibility(View.GONE);
            btnSignup.setText("Manager Sign Up");
        });
        marketerRadio.setOnClickListener(v -> {
            type = "2";
            referralCodeLayout.setVisibility(View.VISIBLE);
            btnSignup.setText("Coordinator Sign Up");
        });

        btnSignup.setOnClickListener(v -> {
            ApiConfig.hideKeyboard(activity, v);
            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            String password = imgLoginPassword.getText().toString();
            String re_password = imgLoginConPassword.getText().toString();

            if (TextUtils.isEmpty(name)) {
                edtName.requestFocus();
                edtName.setError("Name field is mandatory.!");
            } else if (TextUtils.isEmpty(email)) {
                edtEmail.requestFocus();
                edtEmail.setError("Email field is mandatory.!");
            } else if (TextUtils.isEmpty(password)) {
                imgLoginPassword.requestFocus();
                imgLoginPassword.setError("Password field is mandatory.!");
            } else if (TextUtils.isEmpty(re_password)) {
                imgLoginConPassword.requestFocus();
                imgLoginConPassword.setError("Confirm Password field is mandatory.!");
            } else {

                if (password.equals(re_password)){
                    switch (type) {
                        case "":
                            setSnackBar("Please select any one", "Ok", "none");
                            break;
                        case "1":
                            signup(name, email, mobile, password, referralCode);
                            break;
                        case "2":
                            referralCode = referralCodeEdit.getText().toString();
                            if (TextUtils.isEmpty(referralCode)){
                                referralCodeEdit.requestFocus();
                                referralCodeEdit.setError("Referral field is mandatory.!");
                            }else {
                                signup(name, email, mobile, password, referralCode);
                            }
                            break;
                    }
                }else {
                    setSnackBar("Password are not same, please check your password", "Ok", "none");
                }
            }

        });
    }

    private void checkNumber() {
        session.setData(Constant.COUNTRY_CODE, "88");

        mobile = edtMobileVerify.getText().toString();

//        Map<String, String> params = new HashMap<>();
//        params.put(Constant.PAGE, Constant.VERIFY_USER);
//        params.put(Constant.MOBILE, "0"+mobile);
//
//        ApiConfig.RequestToVolley((result, response) -> {
//            if (result) {
//                try {
//                    JSONObject object = new JSONObject(response);
//                    String phoneNumber = ("+" + session.getData(Constant.COUNTRY_CODE) + "0" + mobile);
//                    if (!object.getBoolean(Constant.ERROR)) {
//                        generateOTP(phoneNumber);
//                    } else {
////                        setSnackBar(getString(R.string.alert_register_num1) + " " + getString(R.string.app_name) + getString(R.string.alert_register_num2), getString(R.string.btn_ok), "reset");
//                        setSnackBar(getString(R.string.alert_not_register_num1) + " " + getString(R.string.app_name) + getString(R.string.alert_not_register_num2), getString(R.string.btn_ok), "none");
//                    }
//                } catch (JSONException e) {
//                    Log.d("checkNumber", e.getMessage());
////                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, activity, Constant.AUTH_LOGIN_URL, params, true);
        String phoneNumber = ("+" + session.getData(Constant.COUNTRY_CODE) + "0" + mobile);
        generateOTP(phoneNumber);
    }

    public void generateOTP(String oPhoneNumber) {
        String generateOTP = ApiConfig.RanNumGen.randomString(6);
        session.setData(Constant.GENERATED_OTP, generateOTP);

        Map<String, String> params = new HashMap<>();
        params.put(Constant.TEXT, "আপনার একাউন্ট যাচাইকরণ OTP: " + session.getData(Constant.GENERATED_OTP));
        params.put(Constant.MOBILE, oPhoneNumber);
        params.put(Constant.SEND_SMS, Constant.GetVal);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(Constant.ERROR)) {

                        JSONObject data = object.getJSONObject(Constant.DATA);

                        if (data.getString("status_code").equals("200")){
                            setSnackBar("প্রিয় গ্রাহক, আপনার মোবাইল নম্বর ভেরিফিকেশনের জন্য আপনার মোবাইল নাম্বারে আমরা একটি OTP পাঠিয়েছি। অনুগ্রহ পূর্বক আপনার মোবাইলের মেসেজ চেক করুন।", getString(R.string.btn_ok), "done");
                            edtMobileVerify.setEnabled(false);
//                            edtCountryCodePicker.setCcpClickable(false);
                            btnVerify.setText(getString(R.string.verify_otp));
                            lytOTP.setVisibility(View.VISIBLE);
                        }else {
                            setSnackBar("Error ", getString(R.string.btn_ok), "done");
                        }
                        if (resendOTP) {
                            setSnackBar(getString(R.string.otp_resend_alert), "Ok", "none");
                        }
                        else {
                            edtMobileVerify.setEnabled(false);
//                            edtCountryCodePicker.setCcpClickable(false);
                            btnVerify.setText(getString(R.string.verify_otp));
                            lytOTP.setVisibility(View.VISIBLE);

                            btnVerify.setOnClickListener(v -> setSignUpLayout());

                            new CountDownTimer(60000, 1000) {
                                @SuppressLint("SetTextI18n")
                                public void onTick(long millisUntilFinished) {
                                    timerOn = true;
                                    // Used for formatting digit to be in 2 digits only
                                    NumberFormat f = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        f = new DecimalFormat("00");
                                    }
                                    long min = (millisUntilFinished / 60000) % 60;
                                    long sec = (millisUntilFinished / 1000) % 60;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        tvTimer.setText(f.format(min) + ":" + f.format(sec));
                                    }
                                }

                                public void onFinish() {
                                    resendOTP = false;
                                    timerOn = false;
                                    tvTimer.setVisibility(View.GONE);
                                    tvResend.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

                                    tvResend.setOnClickListener(v -> {

                                        Map<String, String> params = new HashMap<>();
                                        params.put(Constant.TEXT, "আপনার একাউন্ট যাচাইকরণ OTP: " + session.getData(Constant.GENERATED_OTP));
                                        params.put(Constant.MOBILE, oPhoneNumber);
                                        params.put(Constant.SEND_SMS, Constant.GetVal);
                                        ApiConfig.RequestToVolley((result, response) -> {
                                            if (result) {
                                                try {
                                                    JSONObject object = new JSONObject(response);
                                                    if (!object.getBoolean(Constant.ERROR)) {
                                                        setSnackBar("প্রিয় গ্রাহক, আপনার মোবাইল নম্বর ভেরিফিকেশনের জন্য আপনার মোবাইল নাম্বারে আমরা একটি OTP পাঠিয়েছি। অনুগ্রহ পূর্বক আপনার মোবাইলের মেসেজ চেক করুন।", getString(R.string.btn_ok), "done");
                                                    } else {
                                                        setSnackBar("Something went wrong, Please try again latter", "Retry", "none");
                                                    }
                                                } catch (JSONException e) {
                                                    Log.d("ignored", e.getMessage());
                                                }

                                                resendOTP = true;
                                                resendOTP = false;
                                                timerOn = false;
                                                tvTimer.setVisibility(View.GONE);
                                                tvResend.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                                                tvResend.setClickable(false);
                                                Log.d("response", response);
                                            }
                                        }, activity, Constant.SMS_SEND_URL, params, true);
                                    });
                                }
                            }.start();
                        }

                    } else {
                        setSnackBar("Something went wrong, Please try again latter", "Retry", "reset");
                    }
                } catch (JSONException e) {
                    Log.d("ignored", e.getMessage());
                }
                Log.d("response", response);
            }
        }, activity, Constant.SMS_SEND_URL, params, true);
    }

    private void setSignUpLayout() {

        String otptext = Objects.requireNonNull(pinViewOTP.getText()).toString().trim();
        if (ApiConfig.CheckValidation(otptext, false, false)) {
            pinViewOTP.requestFocus();
            pinViewOTP.setError(getString(R.string.enter_otp));
        } else if (otptext.isEmpty()){
            pinViewOTP.requestFocus();
            pinViewOTP.setError(getString(R.string.enter_otp));
        }else {
            if (otptext.equals(session.getData(Constant.GENERATED_OTP))){
                lytOTP.setVisibility(View.GONE);
                lytVerify.setVisibility(View.GONE);
                lytSignup.setVisibility(View.VISIBLE);
            }else {
                setSnackBar("Verification Field, Please try again with right OTP", "OK", "reset");
            }
        }
    }

    public void setSnackBar(String message, String action, String type) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, view -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            if (type.equals("done")){
                intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                snackbar.dismiss();
                startActivity(intent);
            }
        });

        snackbar.setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();

        new CountDownTimer(15000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                snackbar.dismiss();
            }
        }.start();
    }

    private void signup(String name, String email, String mobile, String password, String referralCode) {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.PAGE, Constant.REGISTER);
        params.put(Constant.NAME, name);
        params.put(Constant.EMAIL, email);
        params.put(Constant.MOBILE, "0"+mobile);
        params.put(Constant.PASSWORD, password);
        params.put(Constant.COUNTRY_CODE, "88");
        params.put(Constant.TYPE, type);
        params.put(Constant.AFFILIATER_CODE, referralCode);
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean(Constant.ERROR)) {
                        setSnackBar(object.getString(Constant.MESSAGE), "Ok", "none");
                        startActivity(new Intent(activity, LoginActivity.class));
                        session.createUserLoginSession(name, email, "0"+mobile, password, "88", referralCode);
                    }else {
                        setSnackBar("Something went wrong, Please try again latter.", "OK", "none");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    setSnackBar("Something went wrong, Please try again latter", "Ok", "none");
                    Log.d("exception", e.getMessage());
                }
                Log.d("response", response);
            }
        }, activity, Constant.AUTH_LOGIN_URL, params, true);
    }
}