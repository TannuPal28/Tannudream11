package com.elevendreamer.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.elevendreamer.Bean.Message;

import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ChatActivity activity;
    Context context;
    ActivityChatBinding binding;

    private List<Message> messageList;
    private ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat);
        context= activity= this;

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();

    }

    private void initView() {
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(chatAdapter);

        binding.sendButton.setOnClickListener(v -> {
            String msg = binding.messageInput.getText().toString().trim();
            if (!msg.isEmpty()) {
                messageList.add(new Message(msg, true));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                binding.messageInput.setText("");
                binding.recyclerView.scrollToPosition(messageList.size() - 1);
            }
        });
    }

    public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Message> messages;
        private static final int VIEW_TYPE_SENT = 1;
        private static final int VIEW_TYPE_RECEIVED = 2;

        public ChatAdapter(List<Message> messages) {
            this.messages = messages;
        }

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == VIEW_TYPE_SENT) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sent, parent, false);
                return new SentMessageHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_received, parent, false);
                return new ReceivedMessageHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Message message = messages.get(position);
            if (holder instanceof SentMessageHolder) {
                ((SentMessageHolder) holder).bind(message);
            } else {
                ((ReceivedMessageHolder) holder).bind(message);
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class SentMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText;

            SentMessageHolder(View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.text_message_body);
            }

            void bind(Message message) {
                messageText.setText(message.getText());
            }
        }

        class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText;

            ReceivedMessageHolder(View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.text_message_body);
            }

            void bind(Message message) {
                messageText.setText(message.getText());
            }
        }
    }

}