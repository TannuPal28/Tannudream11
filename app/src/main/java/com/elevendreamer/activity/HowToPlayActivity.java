package com.elevendreamer.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elevendreamer.Bean.UserDummy;
import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityHowToPlayBinding;
import com.elevendreamer.databinding.AdapterHowToPlayListBinding;

import java.util.ArrayList;
import java.util.List;

public class HowToPlayActivity extends AppCompatActivity {

    HowToPlayActivity activity;
    Context context;
    ActivityHowToPlayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding= DataBindingUtil.setContentView(this,R.layout.activity_how_to_play);
        context= activity= this;

        initView();

    }

    private void initView() {

        binding.head.tvHeaderName.setText("How to Play");
        binding.head.tvHeaderName.setVisibility(View.VISIBLE);
        binding.head.tvHeaderName.setTextColor(ContextCompat.getColor(this,R.color.white));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        List<UserDummy> userDummies= new ArrayList<>();
        userDummies.add(new UserDummy("Cricket",R.drawable.bats_hvr));
        userDummies.add(new UserDummy("Football",R.drawable.bats_hvr));
        userDummies.add(new UserDummy("Kabaddi",R.drawable.user_icon1));
        userDummies.add(new UserDummy("Hockey",R.drawable.user_icon1));

        AdapterHowToPlayListBinding adapterHowToPlayListBinding = new AdapterHowToPlayListBinding(this,userDummies);
        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(this,2);
        binding.rvPlay.setLayoutManager(layoutManager);
        binding.rvPlay.setAdapter(adapterHowToPlayListBinding);
    }

    public class AdapterHowToPlayListBinding extends RecyclerView.Adapter<AdapterHowToPlayListBinding.MyViewHolder>{
        private Context context;
        private List<UserDummy> userDummyList;

        public AdapterHowToPlayListBinding(Context context,List<UserDummy> userDummyList){
            this.context= context;
            this.userDummyList= userDummyList;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView textView;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                textView= itemView.findViewById(R.id.tvUserNAme);
            }
        }

        @NonNull
        @Override
        public AdapterHowToPlayListBinding.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_how_to_play_list,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterHowToPlayListBinding.MyViewHolder holder, int position) {
            UserDummy userDummy= userDummyList.get(position);
            holder.textView.setText(userDummy.getName());
        }

        @Override
        public int getItemCount() {
            return userDummyList.size();
        }
    }
}