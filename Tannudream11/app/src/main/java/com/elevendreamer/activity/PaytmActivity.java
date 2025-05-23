package com.elevendreamer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.elevendreamer.utils.CheckSumServiceHelper;
import com.elevendreamer.utils.PaytmConstants;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.elevendreamer.R;
import com.elevendreamer.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import static com.elevendreamer.APICallingPackage.Class.Validations.ShowToast;
import static com.elevendreamer.APICallingPackage.Config.ADDAMOUNT;
import static com.elevendreamer.APICallingPackage.Constants.ADDAMOUNTTYPE;

public class PaytmActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback,ResponseManager {

    Button buttonBuy;

    private String orderID = "";
    private String customerID = "";
    private String PayAmount = "0.0";

    PaytmActivity activity;
    Context context;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;
    TextView tv_TxDetails,tv_Proceed;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    JSONObject PaytmResponse;

    String AfterPaymentOrderId, AfterPaymentTxId,AfterPaymentAmount,FinalMessage,AfterPaymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        context = activity = this;
        sessionManager = new SessionManager();

        tv_TxDetails = findViewById(R.id.tv_TxDetails);
        tv_Proceed = findViewById(R.id.tv_Proceed);

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_HeaderName.setText("PAYMENT OPTION");

        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });


        PayAmount = getIntent().getStringExtra("FinalAmount");
        customerID = sessionManager.getUser(context).getUser_id();
        orderID = "OrderID" + System.currentTimeMillis()+"-"+customerID+"-"+PayAmount;
        generateCheckSum();

        tv_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, MyAccountActivity.class);
                startActivity(i);
            }
        });


    }

    private void generateCheckSum() {
        String checkSum = "";
        try {
            checkSum = checksumGeneration(orderID,
                    customerID, PayAmount, PaytmConstants.CALLBACK_URL);
            initializePaytmPayment(checkSum);

        } catch (Exception e) { }
    }

    public String checksumGeneration(String orderId, String cust_id,
                                     String amount,
                                     String callbackUrl) {
        TreeMap<String, String> paramMap = new TreeMap<String, String>();

        paramMap.put("MID", PaytmConstants.M_ID);
        paramMap.put("ORDER_ID", orderID);
        paramMap.put("CHANNEL_ID", PaytmConstants.CHANNEL_ID);
        paramMap.put("CUST_ID", customerID);
        paramMap.put("TXN_AMOUNT", PayAmount);
        paramMap.put("WEBSITE", PaytmConstants.WEBSITE);
        paramMap.put("INDUSTRY_TYPE_ID", PaytmConstants.INDUSTRY_TYPE_ID);
        paramMap.put("CALLBACK_URL", PaytmConstants.CALLBACK_URL+orderID);
        String checkSum = "";
        try {
            checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(PaytmConstants.MerchantKEY, paramMap);
            paramMap.put("CHECKSUMHASH", checkSum);
            boolean is = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(PaytmConstants.MerchantKEY, paramMap, checkSum);

            Log.e("Paytm Checksum: ", is + "");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return checkSum;
    }


    private void initializePaytmPayment(String checksumHash) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService("https://ggg.com/");

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", PaytmConstants.M_ID);
        paramMap.put("ORDER_ID", orderID);
        paramMap.put("CHANNEL_ID", PaytmConstants.CHANNEL_ID);
        paramMap.put("CUST_ID", customerID);
        paramMap.put("TXN_AMOUNT", PayAmount);
        paramMap.put("WEBSITE", PaytmConstants.WEBSITE);
        paramMap.put("INDUSTRY_TYPE_ID", PaytmConstants.INDUSTRY_TYPE_ID);
        paramMap.put("CALLBACK_URL", PaytmConstants.CALLBACK_URL+orderID);
        paramMap.put("CHECKSUMHASH", checksumHash);
        PaytmOrder order = new PaytmOrder(paramMap);
        // intializing the paytm service/verifyCh
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true,
                true, this);
    }

    public JSONObject bundleToJson(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                json.put(key, bundle.get(key));
            } catch (JSONException e) {
            }
        }
        return json;
    }


    @Override
    public void onTransactionResponse(Bundle inResponse) {

        PaytmResponse= bundleToJson(inResponse);
        Log.e("Paytm Response", "onTransactionResponse: "+PaytmResponse);
        String Status = inResponse.getString("STATUS");
        AfterPaymentStatus = inResponse.getString("STATUS");
        AfterPaymentTxId = inResponse.getString("TXNID");
        AfterPaymentOrderId = inResponse.getString("ORDERID");
        AfterPaymentAmount = inResponse.getString("TXNAMOUNT");

        FinalMessage = "Status - "+Status+"\nTransactionId - "+AfterPaymentTxId+"\nOrder Id - "+AfterPaymentOrderId
                +"\nAmount - "+AfterPaymentAmount;

        if (Status.equals("TXN_FAILURE")){
            ShowToast(context,""+Status);
            callAddAmount(true);

        }
        else if (Status.equals("TXN_SUCCESS")){
            ShowToast(context,""+Status);
            callAddAmount(true);
        }
        else if (Status.equals("PENDING")){
            ShowToast(context,""+Status);
            callAddAmount(true);
        }
        else {
            ShowToast(context,""+Status);
        }
    }

    @Override
    public void networkNotAvailable() {
        ShowToast(context,"Network not available");
    }

    @Override
    public void onErrorProceed(String s) {

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

        ShowToast(context,""+inErrorMessage);
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {

        ShowToast(context,""+inErrorMessage);
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

        ShowToast(context,""+inErrorMessage);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        ShowToast(context,"Transaction Cancel by User");
        Intent i = new Intent(activity, HomeActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

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
            jsonObject.put("mode", "Paytm");
            jsonObject.put("transection_detail", PaytmResponse);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        ShowToast(context,message);
        Dialog(AfterPaymentStatus,AfterPaymentTxId,AfterPaymentOrderId,AfterPaymentAmount);
        try {
            String Status = result.getString("transaction_status");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onError(Context mContext, String type, String message) {
        ShowToast(context,message);
        PaymentFailedDialog("Some Error Occured. Please visit Transaction History Page for More Details");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity, HomeActivity.class);
        startActivity(i);
        finish();
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
