package com.elevendreamer.APICallingPackage.Class;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import com.google.android.material.snackbar.Snackbar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elevendreamer.R;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validations {
    public static ProgressDialog progress=null;


    public static boolean isNetworkAvailable(Context context) {

        try {
            NetworkInfo localNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            return (localNetworkInfo != null) && (localNetworkInfo.isConnected());

        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

    }


    public static void showProgress(Context mContext) {
        try {
            if (mContext!=null) {

                if (progress!=null && progress.isShowing())
                {
                    return;
                }

                progress = ProgressDialog.show(mContext, null, null);
                progress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progress.setContentView(R.layout.progress_loading);



            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

    }


    public  static String MobilePattern = "[0-9]{10}";

    public static  boolean isValidEmail(String email) {
        String EMAIL_PATTERN ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void ShowToast(Context context, String Message){
      Toast toast=  Toast.makeText(context,Message, Toast.LENGTH_SHORT);
        toast.show();
    }


    public static void ShowSnack(RelativeLayout relativeLayout, String Message){
        Snackbar.make(relativeLayout, Message, Snackbar.LENGTH_LONG).show();
    }



    public static void CountDownTimer(String Time, final TextView textView){
        try {
            int FlashCount = Integer.parseInt(Time);
            long FlashMiliSec = FlashCount * 1000L;

            new CountDownTimer(FlashMiliSec, 1000) {

                public void onTick(long millisUntilFinished) {

                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;

                    StringBuilder timeBuilder = new StringBuilder();
                    if (days > 0) timeBuilder.append(days).append("d ");
                    if (hours > 0 || days > 0) timeBuilder.append(hours).append("h ");
                    timeBuilder.append(minutes).append("m");

                    textView.setText(timeBuilder.toString().trim());
                }

                public void onFinish() {
                    textView.setText("Entry Over!");
                }

            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static void CountDownTimer1(String Time, final TextView textView){
        try {
            int FlashCount = Integer.parseInt(Time);
            long FlashMiliSec = FlashCount * 1000L;

            new CountDownTimer(FlashMiliSec, 1000) {

                public void onTick(long millisUntilFinished) {

                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;

                    StringBuilder timeBuilder = new StringBuilder();
                    if (days > 0) timeBuilder.append(days).append("d ");
                    if (hours > 0 || days > 0) timeBuilder.append(hours).append("h ");
                    timeBuilder.append(minutes).append("m");

                    textView.setText(timeBuilder.toString().trim() + " left");
                }

                public void onFinish() {
                    textView.setText("Entry Over!");
                }

            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }



}
