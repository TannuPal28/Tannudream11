package com.elevendreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    Context context;
    MainActivity activity;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = activity = this;


        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding.tvLetsPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity,LoginActivity.class);
                startActivity(i);
            }
        });

//        binding.LLStartSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(activity,RegistrationActivity.class);
//                i.putExtra("Reffered","Yes");
//                startActivity(i);
//            }
//        });
//
//        binding.tvLetsPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(activity,RegistrationActivity.class);
//                i.putExtra("Reffered","No");
//                startActivity(i);
//            }
//        });



    }



    @Override
    public void onBackPressed() {


        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }
}
