package com.elevendreamer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.elevendreamer.utils.AppEnvironment;
import com.elevendreamer.utils.AppPreference;
import com.elevendreamer.application.BaseApplication;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.elevendreamer.APICallingPackage.Config;
import com.elevendreamer.R;
import com.elevendreamer.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.elevendreamer.APICallingPackage.Config.ADDAMOUNT;
import static com.elevendreamer.APICallingPackage.Constants.ADDAMOUNTTYPE;

public class PayUMoneyPaymentActivity extends AppCompatActivity implements View.OnClickListener,ResponseManager {

    String TAG = "PayUMoneyPaymentActivity";
    PayUMoneyPaymentActivity activity;
    Context context;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;

    EditText et_CheckName,et_CheckEmail,et_CheckNumber;
    TextView tv_totalamount,tv_Proceed,tv_Logout;
    private Dialog loadingDialog;
    private String userMobile, userEmail,userName;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private SharedPreferences userDetailsPreference;
    private AppPreference mAppPreference;

    String FPaymentId,FTransactionId,FPaymentMode,FTransactionStatus,FAmount,FUserName,
            FEmail,FPhone,FMessage,FBankReference,FBankCode,FNameOnCard,FCardNumber,FNetAmountDebit,
            FPayumoneyId,FStudentId,FProductId,FProductPrice,FProductDiscount;


    private String orderID = "";
    private String customerID = "";
    private String PayAmount = "0.0";

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    JSONObject PayUMoneyResponse;


    String FinalMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_umoney_payment);

        context = activity = this;
        sessionManager = new SessionManager();
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_HeaderName.setText("PAYMENT OPTION");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        tv_totalamount = (TextView) findViewById(R.id.tv_totalamount);
        tv_Proceed = (TextView) findViewById(R.id.tv_Checkout);
        tv_Logout = (TextView) findViewById(R.id.tv_Logout);

        et_CheckName = (EditText) findViewById(R.id.tv_CheckName);
        et_CheckEmail = (EditText) findViewById(R.id.tv_CheckEmail);
        et_CheckNumber = (EditText) findViewById(R.id.tv_CheckNumber);
        tv_totalamount.setText("₹ "+getIntent().getStringExtra("FinalAmount"));

        mAppPreference = new AppPreference();

        PayAmount = getIntent().getStringExtra("FinalAmount");
        customerID = sessionManager.getUser(context).getUser_id();
        orderID = "OrderID" + System.currentTimeMillis()+"-"+customerID+"-"+PayAmount;


        settings = getSharedPreferences("settings", MODE_PRIVATE);


        et_CheckEmail.setText(sessionManager.getUser(context).getEmail()+"");
        et_CheckName.setText(sessionManager.getUser(context).getName()+"");
        et_CheckNumber.setText(sessionManager.getUser(context).getMobile()+"");


        tv_Logout.setOnClickListener(this);

        tv_Proceed.setOnClickListener(this);
        setUpUserDetails();
        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);


    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }


    public static void setErrorInputLayout(EditText editText, String msg) {
        editText.setError(msg);
        editText.requestFocus();
    }

    public static boolean isValidEmail(String strEmail) {
        return strEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    public static boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile(AppPreference.PHONE_PATTERN);

        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    private void setUpUserDetails() {
        userDetailsPreference = getSharedPreferences(AppPreference.USER_DETAILS, MODE_PRIVATE);
        userEmail = userDetailsPreference.getString(AppPreference.USER_EMAIL, mAppPreference.getDummyEmail());
        userMobile = userDetailsPreference.getString(AppPreference.USER_MOBILE, mAppPreference.getDummyMobile());
        userName = userDetailsPreference.getString(AppPreference.USER_NAME, mAppPreference.getDummyName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_Proceed.setEnabled(true);


    }

    /**
     * This function sets the mode to PRODUCTION in Shared Preference
     */
    private void selectProdEnv() {

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
                editor = settings.edit();
                editor.putBoolean("is_prod_env", true);
                editor.apply();



                setupCitrusConfigs();
            }
        }, AppPreference.MENU_DELAY);
    }

    /**
     * This function sets the mode to SANDBOX in Shared Preference
     */
    private void selectSandBoxEnv() {
        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
        editor = settings.edit();
        editor.putBoolean("is_prod_env", false);
        editor.apply();

        setupCitrusConfigs();
    }

    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        if (appEnvironment == AppEnvironment.PRODUCTION) {
        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);

    }

    @Override
    public void onClick(View v) {
        userEmail = et_CheckEmail.getText().toString().trim();
        userMobile = et_CheckNumber.getText().toString().trim();
        userName = et_CheckName.getText().toString().trim();
        if (v.getId() == R.id.tv_Logout || validateDetails(userEmail, userMobile,userName)) {
            switch (v.getId()) {
                case R.id.tv_Checkout:
                    //tv_Proceed.setEnabled(false);
                    launchPayUMoneyFlow();
                    break;
                case R.id.tv_Logout:
                    tv_Logout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**
     * This fucntion checks if email and mobile number are valid or not.
     *
     * @param email  email id entered in edit text
     * @param mobile mobile number entered in edit text
     * @return boolean value
     */
    public boolean validateDetails(String email, String mobile, String name) {
        name = name.trim();
        email = email.trim();
        mobile = mobile.trim();

        if (TextUtils.isEmpty(name)) {
            setErrorInputLayout(et_CheckName, "Enter Name");
            return false;
        } else
        if (TextUtils.isEmpty(mobile)) {
            return false;
        } else if (!isValidPhone(mobile)) {
            return false;
        } else if (TextUtils.isEmpty(email)) {
            return false;
        } else if (!isValidEmail(email)) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {
 double amount = 0;
        try {
            amount = Double.parseDouble(PayAmount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = orderID;
        String phone = et_CheckNumber.getText().toString().trim();
        String productName = mAppPreference.getProductInfo()+ System.currentTimeMillis();
        String firstName = et_CheckName.getText().toString().trim();
        String email = et_CheckEmail.getText().toString().trim();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();

        try {

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            tv_Proceed.setEnabled(true);
        }
    }






    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs
                // to replace this with their server side hash generation url
                URL url = new URL(Config.HASHKEYREQUEST);

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                  String Result = result.toString();
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();

            tv_Proceed.setEnabled(true);
            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(activity, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }


    private class AsyncHash extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(Config.HASHKEYREQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(20000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("hashkey", params[0]);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {

                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                progressDialog.dismiss();
                if (null != result) {
                    JSONObject obj = new JSONObject(result);
                    String merchantHash = obj.getString("payment_hash");
                    tv_Proceed.setEnabled(true);
                    if (merchantHash.isEmpty() || merchantHash.equals("")) {
                        Toast.makeText(activity, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    } else {

                    }

                    } else {
                        Toast.makeText(getBaseContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public static class EditTextInputWatcher implements TextWatcher {

        private TextInputLayout textInputLayout;

        EditTextInputWatcher(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                textInputLayout.setError(null);
                textInputLayout.setErrorEnabled(false);
            }
        }
    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }


    private void callAddAmount(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(ADDAMOUNT,
                    createRequestJson(), context, activity, ADDAMOUNTTYPE,
                    isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("amount", PayAmount);
            jsonObject.put("mode", "PayUMoney");
            jsonObject.put("transection_detail", PayUMoneyResponse);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        try {
            String Status = result.getString("transaction_status");

            FinalMessage = "Status - "+Status+"\nTransactionId - "+FTransactionId+
                    "\nPayment Id - "+FPaymentId
                    +"\nAmount - "+FAmount;

            Dialog(FTransactionStatus,FTransactionId,FPaymentId,FAmount);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onError(Context mContext, String type, String message) {
        PaymentFailedDialog("Some Error Occured. Please visit Transaction History Page for More Details");
    }

    public void Dialog(String Status, String TxId, String OrderId, String TxAmount){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dialog);

        final TextView tv_DStatus = dialog.findViewById(R.id.tv_DStatus);
        final TextView tv_DTransactionId = dialog.findViewById(R.id.tv_DTransactionId);
        final TextView tv_DOrderId = dialog.findViewById(R.id.tv_DOrderId);
        final TextView tv_DTxAmount = dialog.findViewById(R.id.tv_DTxAmount);
        final TextView tv_TxDone = dialog.findViewById(R.id.tv_TxDone);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        tv_DStatus.setText(Status);
        tv_DTransactionId.setText(TxId);
        tv_DOrderId.setText(OrderId);
        tv_DTxAmount.setText("₹ "+TxAmount);


        tv_TxDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(activity, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void PaymentFailedDialog(String message){

        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setCancelable(false);
        ab.setMessage(message);

        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent i = new Intent(activity, HomeActivity.class);
                startActivity(i);
                finish();

            }
        });
        AlertDialog alert = ab.create();
        alert.show();

    }



}
