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
import com.elevendreamer.databinding.ActivityAppLanguageBinding;

public class AppLanguageActivity extends AppCompatActivity {

    AppLanguageActivity activity;
    Context context;

    ActivityAppLanguageBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
       binding= DataBindingUtil.setContentView(this,R.layout.activity_app_language);
       context= activity= this;

       initView();

    }

    private void initView() {

        binding.head.tvHeaderName.setVisibility(View.VISIBLE);
        binding.head.tvHeaderName.setText("My Info & Settings");
        binding.head.tvHeaderName.setTextColor(ContextCompat.getColor(this,R.color.white));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        View.OnClickListener listener= v->{
            resetSelection();
            if (v == binding.rbEnglish){
                binding.llEnglish.setBackgroundResource(R.drawable.bg_language_selected);
                binding.tvEnglish.setTextColor(ContextCompat.getColor(this, R.color.addgreen));
                binding.rbEnglish.setChecked(true);
            }
            else  if (v== binding.rbHindi){
                binding.llHindi.setBackgroundResource(R.drawable.bg_language_selected);
                binding.tvHindi.setTextColor(ContextCompat.getColor(this,R.color.addgreen));
                binding.rbHindi.setChecked(true);
            } else if (v == binding.rbMarathi) {
                binding.llMarathi.setBackgroundResource(R.drawable.bg_language_selected);
                binding.tvMarathi.setTextColor(ContextCompat.getColor(this,R.color.addgreen));
                binding.rbMarathi.setChecked(true);
            }
        };

        binding.rbEnglish.setOnClickListener(listener);
        binding.rbHindi.setOnClickListener(listener);
        binding.rbMarathi.setOnClickListener(listener);

    }

    private void resetSelection() {
        binding.llEnglish.setBackgroundResource(R.drawable.bg_language_unselected);
        binding.llHindi.setBackgroundResource(R.drawable.bg_language_unselected);
        binding.llMarathi.setBackgroundResource(R.drawable.bg_language_unselected);

        binding.tvEnglish.setTextColor(ContextCompat.getColor(this,R.color.black));
        binding.tvHindi.setTextColor(ContextCompat.getColor(this,R.color.black));
        binding.tvMarathi.setTextColor(ContextCompat.getColor(this,R.color.black));

        binding.rbEnglish.setChecked(false);
        binding.rbHindi.setChecked(false);
        binding.rbMarathi.setChecked(false);
    }
}