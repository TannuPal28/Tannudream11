package com.elevendreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elevendreamer.Bean.UserDummy;
import com.elevendreamer.R;
import com.elevendreamer.bottomDialogBox.ChatBottomFragment;
import com.elevendreamer.databinding.ActivityChatListctivityBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatListctivity extends AppCompatActivity {

    ChatListctivity activity;
    Context context;
    ActivityChatListctivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding= DataBindingUtil.setContentView(this, R.layout.activity_chat_listctivity);
        context= activity= this;

        initView();

    }

    private void initView() {
        binding.head.tvHeaderName.setText("Chat");
        binding.head.tvHeaderName.setVisibility(View.VISIBLE);
        binding.head.tvHeaderName.setTextColor(ContextCompat.getColor(this,R.color.white));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        List<UserDummy> userDummyList= new ArrayList<>();
        userDummyList.add(new UserDummy("Future fantasy",R.drawable.user_icon1));
        userDummyList.add(new UserDummy("Future fantasy2",R.drawable.user_icon1));

        AdapterChatList adapterChatList= new AdapterChatList(this,userDummyList);
        RecyclerView.LayoutManager manager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.rvChat.setLayoutManager(manager);
        binding.rvChat.setAdapter(adapterChatList);

        binding.floatingBtn.setOnClickListener(view -> {
            callBottomDialogBox();
        });
    }

    private void callBottomDialogBox() {
        ChatBottomFragment chatBottomFragment= new ChatBottomFragment();
        chatBottomFragment.setListener(new ChatBottomFragment.onClickListener() {
            @Override
            public void onDirectChatClick() {
                Intent i= new Intent(getApplicationContext(),DirectChatContacts.class);
                startActivity(i);
            }

            @Override
            public void onCreateGroupClick() {

            }
        });
        chatBottomFragment.show(getSupportFragmentManager(),"ChatBottomFragment");
    }

    public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyViewHolder>{
        private Context context;
        private List<UserDummy> userDummyList;

        public AdapterChatList(Context context, List<UserDummy> userDummyList){
            this.context= context;
            this.userDummyList= userDummyList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.adapter_chat_user_list,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            UserDummy userDummy= userDummyList.get(position);
            holder.textView.setText(userDummy.getName());

        }

        @Override
        public int getItemCount() {
            return userDummyList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
                TextView textView;

            public MyViewHolder(@NonNull View itemView){
                super((itemView));
                textView= itemView.findViewById(R.id.userName);
            }
        }
    }
}