package com.elevendreamer.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.load.engine.Resource;
import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityMyInfoSettingsBinding;

import java.util.Calendar;

public class MyInfoSettingsActivity extends AppCompatActivity {
    MyInfoSettingsActivity activity;
    Context context;

    ActivityMyInfoSettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_info_settings);
        context= activity= this;

        initView();


    }

    private void initView() {

        binding.head.tvHeaderName.setText("My Info & Settings");
        binding.head.tvHeaderName.setVisibility(View.VISIBLE);
        binding.head.tvHeaderName.setTextColor(ContextCompat.getColor(this,R.color.white));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.permissionSettings.setOnClickListener(view -> {
            Intent i =  new Intent(this,PermissionAndSettingsActivity.class);
            startActivity(i);
        });

        binding.appLanguage.setOnClickListener(view -> {
            Intent i = new Intent(this,AppLanguageActivity.class);
            startActivity(i);
        });

        binding.manageAccount.setOnClickListener(view -> {
            Intent i = new Intent(this,ManageAccount.class);
            startActivity(i);
        });
        binding.tvDateOFBirth.setOnClickListener(view ->{
            Calendar calendar= Calendar.getInstance();
            int year= calendar.get(Calendar.YEAR);
            int month= calendar.get(Calendar.MONTH);
            int day= calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                    String date= String.format("%02d/%02d/%02d",selectedDay, selectedMonth+1,selectedYear);
                    binding.tvDateOFBirth.setText(date);
                }
            },year,month,day);
            datePickerDialog.show();
        });
    }
}