package com.elevendreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityCreateContestBinding;

import org.json.JSONObject;

import static com.elevendreamer.APICallingPackage.Class.Validations.ShowToast;

public class CreateContestActivity extends AppCompatActivity implements ResponseManager {

    CreateContestActivity activity;
    Context context;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    String ContestName;
    int WinningAmount, ContestSize;
    double EntryFees;
    String StringWinningAmount, StringContestSize;
    String MatchId;

    ActivityCreateContestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = activity = this; // ✅ Ensure context/activity before binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_contest); // ✅ Correct layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initViews();

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        MatchId = getIntent().getStringExtra("MatchId");

        binding.RL2.setOnClickListener(view -> calculateEntryFee());

        binding.RLBottomCreateMyContest.setOnClickListener(view -> {
            if (calculateEntryFee()) {
                Intent i = new Intent(activity, SelectPrizeCreateActivity.class);
                i.putExtra("ContestName", ContestName);
                i.putExtra("ContestSize", ContestSize);
                i.putExtra("ContestWinningAmount", WinningAmount);
                i.putExtra("EntryFees", EntryFees);
                i.putExtra("MatchId", MatchId);
                startActivity(i);
            }
        });
    }

    public void initViews() {
        binding.head.tvHeaderName.setText("Make Your Own Contest");
        binding.head.imBack.setOnClickListener(view -> onBackPressed());
    }

    private boolean calculateEntryFee() {
        ContestName = binding.etContestName.getText().toString();
        StringWinningAmount = binding.etWinningAmount.getText().toString();
        StringContestSize = binding.etContestSize.getText().toString();

        if (StringWinningAmount.isEmpty()) {
            ShowToast(context, "Enter Winning Amount");
            return false;
        } else if (StringContestSize.isEmpty()) {
            ShowToast(context, "Enter Contest Size");
            return false;
        }

        try {
            WinningAmount = Integer.parseInt(StringWinningAmount);
            ContestSize = Integer.parseInt(StringContestSize);
        } catch (NumberFormatException e) {
            ShowToast(context, "Please enter valid numbers");
            return false;
        }

        if (WinningAmount <= 0 || WinningAmount > 10000) {
            ShowToast(context, "Winning Amount Should be between 1 to 10,000");
            return false;
        }

        if (ContestSize < 2 || ContestSize > 100) {
            ShowToast(context, "Contest Size Must be between 2 to 100");
            return false;
        }

        double twentyPercent = (WinningAmount * 0.2);
        double finalAmount = WinningAmount + twentyPercent;
        EntryFees = finalAmount / ContestSize;

        binding.tvEntryFees.setText("Entry Fees - " + String.format("%.2f", EntryFees));

        if (EntryFees < 5) {
            ShowToast(context, "Entry Fees cannot be less than 5 Rs.");
            return false;
        }

        return true;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        // Handle API success response if used
    }

    @Override
    public void onError(Context mContext, String type, String message) {
        // Handle API error response if used
    }
}
