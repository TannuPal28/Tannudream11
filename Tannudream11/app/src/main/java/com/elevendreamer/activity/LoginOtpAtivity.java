package com.elevendreamer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.elevendreamer.R;

import java.util.Objects;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class LoginOtpAtivity extends AppCompatActivity {
    private OtpTextView otp_View;
    private SharedPreferences.Editor loginPrefsEditor;
    private SharedPreferences loginPreferences;
    private TextView otp;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp_ativity);
        otp_View = (OtpTextView) findViewById(R.id.otp_view);
        otp_View = findViewById(R.id.otp_view);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        loginPrefsEditor = loginPreferences.edit();
otp = findViewById(R.id.otp);
otp.setText(getIntent().getStringExtra("OTP"));
        otp_View.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
if (Objects.equals(otp, getIntent().getStringExtra("OTP"))){
    loginPrefsEditor.putBoolean("saveLogin", true);
    loginPrefsEditor.commit();
    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                   startActivity(i);
} else{
    Log.e("otp", otp);
    Log.e("otp", getIntent().getStringExtra("OTP"));
    Toast.makeText(LoginOtpAtivity.this, "Please enter correct otp",  Toast.LENGTH_SHORT).show();}
            }
        });
    }
}
