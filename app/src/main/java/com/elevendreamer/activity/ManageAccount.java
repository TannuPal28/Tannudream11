package com.elevendreamer.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityManageAccountBinding;

public class ManageAccount extends AppCompatActivity {

    ActivityManageAccountBinding binding;
    Context context;

    ManageAccount activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));

        binding= DataBindingUtil.setContentView(this,R.layout.activity_manage_account);
        context= activity= this;

        binding.head.tvHeaderName.setText("Manage Account");
        binding.head.tvHeaderName.setVisibility(View.VISIBLE);
        binding.head.tvHeaderName.setTextColor(ContextCompat.getColor(this,R.color.white));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}