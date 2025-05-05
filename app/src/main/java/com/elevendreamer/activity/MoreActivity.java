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
import com.elevendreamer.databinding.ActivityMoreBinding;

public class MoreActivity extends AppCompatActivity {

    MoreActivity activity;
    Context context;
    ActivityMoreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#B71C1C"));
       binding= DataBindingUtil.setContentView(this,R.layout.activity_more);
       context= activity= this;

       initView();
    }

    private void initView() {
        binding.head.tvHeaderName.setText("More");
        binding.head.tvHeaderName.setVisibility(View.VISIBLE);
        binding.head.tvHeaderName.setTextColor(ContextCompat.getColor(this,R.color.white));
        binding.head.toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.red_a700));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}