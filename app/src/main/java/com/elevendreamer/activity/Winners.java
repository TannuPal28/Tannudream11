package com.elevendreamer.activity;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.elevendreamer.Bean.UserDummy;
import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityWinnersBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Winners extends AppCompatActivity{
    Winners activity;
    Context context;
    ActivityWinnersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding= DataBindingUtil.setContentView(this,R.layout.activity_winners);
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

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.blink_move);
        binding.tvPeople.startAnimation(animation);

        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng delhi = new LatLng(28.6139, 77.2090); // Example location
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi,10));
                googleMap.addMarker(new MarkerOptions().position(delhi).title("marker in delhi"));
            }
        });

        List<UserDummy> userDummies= new ArrayList<>();
        userDummies.clear();
        userDummies.add(new UserDummy("MAJHARUL KINGS",R.drawable.user_icon1));
        userDummies.add(new UserDummy("MAJHARUL KINGS",R.drawable.user_icon1));
        userDummies.add(new UserDummy("MAJHARUL KINGS",R.drawable.user_icon1));
        userDummies.add(new UserDummy("MAJHARUL KINGS",R.drawable.user_icon1));

        AdapterWinnersList winnerAdapter= new AdapterWinnersList(this,userDummies);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvWinnerList.setLayoutManager(mLayoutManager);
        binding.rvWinnerList.setAdapter(winnerAdapter);

        PagerSnapHelper snapHelper= new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvWinnerList);
        binding.indicator.attachToRecyclerView(binding.rvWinnerList,snapHelper);
        winnerAdapter.registerAdapterDataObserver(binding.indicator.getAdapterDataObserver());
//        binding.indicator.setIndicatorDrawable(R.drawable.black_dot_unselected, R.drawable.black_dot_selected);

        AdapterJoinedTourList adapterJoinedTourList= new AdapterJoinedTourList(this,userDummies);
        RecyclerView.LayoutManager manager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.rvTourJoinedList.setLayoutManager(manager);
        binding.rvTourJoinedList.setAdapter(adapterJoinedTourList);

        AdapterMillionTrustedUser adapterMillionTrustedUser= new AdapterMillionTrustedUser(this,userDummies);
        RecyclerView.LayoutManager manager1= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.rvMillionUsers.setLayoutManager(manager1);
        binding.rvMillionUsers.setAdapter(adapterMillionTrustedUser);


    }

    public class AdapterWinnersList extends RecyclerView.Adapter<AdapterWinnersList.MyViewHolder>{
        private Context context;
        private List<UserDummy> userList;

        public AdapterWinnersList(Context context,List<UserDummy> userList){
            this.context= context;
            this.userList= userList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                textView= itemView.findViewById(R.id.tvUserNAme);
            }
        }


        @NonNull
        @Override
        public AdapterWinnersList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_winner_list,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterWinnersList.MyViewHolder holder, int position) {

            UserDummy userDummy= userList.get(position);
            holder.textView.setText(userDummy.getName());

        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    public class AdapterJoinedTourList extends RecyclerView.Adapter<AdapterJoinedTourList.MyViewHolder>{
        private Context context;
        private List<UserDummy> userList;

        public AdapterJoinedTourList(Context context,List<UserDummy> userList){
            this.context= context;
            this.userList= userList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                textView= itemView.findViewById(R.id.tvUserNAme);
            }
        }


        @NonNull
        @Override
        public AdapterJoinedTourList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_joined_tours_list,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterJoinedTourList.MyViewHolder holder, int position) {

            UserDummy userDummy= userList.get(position);
            holder.textView.setText(userDummy.getName());

        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    public class AdapterMillionTrustedUser extends RecyclerView.Adapter<AdapterMillionTrustedUser.MyViewHolder>{
        private Context context;
        private List<UserDummy> userList;

        public AdapterMillionTrustedUser(Context context,List<UserDummy> userList){
            this.context= context;
            this.userList= userList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                textView= itemView.findViewById(R.id.userExp);
            }
        }


        @NonNull
        @Override
        public AdapterMillionTrustedUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_millions_trusted_user_list,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterMillionTrustedUser.MyViewHolder holder, int position) {

            UserDummy userDummy= userList.get(position);
            holder.textView.setText(userDummy.getName());

        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

}



























