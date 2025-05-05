package com.elevendreamer.activity;

import android.content.Context;
import android.content.Intent;
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
import com.elevendreamer.databinding.ActivityPermissionAndSettingsBinding;

public class PermissionAndSettingsActivity extends AppCompatActivity {

    PermissionAndSettingsActivity activity;
    Context context;

    ActivityPermissionAndSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding= DataBindingUtil.setContentView(this,R.layout.activity_permission_and_settings);
        context= activity= this;

        initView();

    }

    private void initView() {
        binding.head.tvHeaderName.setVisibility(View.VISIBLE);
        binding.head.tvHeaderName.setText("Permissions and Settings");
        binding.head.tvHeaderName.setTextColor(ContextCompat.getColor(this,R.color.white));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.rlEight.setOnClickListener(view -> {
            Intent i = new Intent(this,BlockedListActivity.class);
            startActivity(i);
        });
    }
}