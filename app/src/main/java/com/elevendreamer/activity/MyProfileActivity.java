package com.elevendreamer.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elevendreamer.Bean.UserDummy;
import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityMyProfileBinding;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    MyProfileActivity activity;
    Context context;
    ActivityMyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_profile);
        context= activity= this;

        // Initially hide the TextView
        binding.titleText.setVisibility(View.GONE);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Set scroll listener on NestedScrollView
        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 100) {
                    // If scrolled more than 100px, show the TextView
                    if (binding.titleText.getVisibility() != View.VISIBLE) {
                        binding.titleText.setVisibility(View.VISIBLE);
                    }
                } else {
                    // If scrolled back to the top, hide the TextView
                    if (binding.titleText.getVisibility() != View.GONE) {
                        binding.titleText.setVisibility(View.GONE);
                    }
                }
            }
        });

        List<UserDummy> userDummies= new ArrayList<>();
        userDummies.clear();
        userDummies.add(new UserDummy("KKR beat SRH by 80 runs",R.drawable.user_icon1));
        userDummies.add(new UserDummy("KKR beat SRH by 80 runs",R.drawable.user_icon1));
        userDummies.add(new UserDummy("KKR beat SRH by 80 runs",R.drawable.user_icon1));

        AdapterTeamList adapterTeamList= new AdapterTeamList(this,userDummies);
        RecyclerView.LayoutManager  mLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.rvPlayedList.setLayoutManager(mLayoutManager);
        binding.rvPlayedList.setAdapter(adapterTeamList);
    }

    public class AdapterTeamList extends RecyclerView.Adapter<AdapterTeamList.MyViewHolder>{

        private Context context;
        private List<UserDummy> userList;

        public  AdapterTeamList(Context context,List<UserDummy> userList){
            this.context= context;
            this.userList= userList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public MyViewHolder(@NonNull View itemView){
                super(itemView);
                textView= itemView.findViewById(R.id.tvTeamName);
            }
        }

        @NonNull
        @Override
        public AdapterTeamList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_recently_played_list_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterTeamList.MyViewHolder holder, int position) {
            UserDummy userDummy= userList.get(position);
            holder.textView.setText(userDummy.getName());
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }
}
