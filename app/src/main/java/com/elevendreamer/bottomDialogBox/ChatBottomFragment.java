package com.elevendreamer.bottomDialogBox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elevendreamer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChatBottomFragment extends BottomSheetDialogFragment {

    private onClickListener listener;

    public interface onClickListener{
        void onDirectChatClick();
        void onCreateGroupClick();
    }

    public void setListener(onClickListener listener){
        this.listener= listener;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat_bottom,container,false);
        View directChat= view.findViewById(R.id.tvDirectChat);
        View createAGroup= view.findViewById(R.id.tvCreateGroup);

        directChat.setOnClickListener(view1 -> {
                if (listener!= null){
                    listener.onDirectChatClick();
                    dismiss();
                }
        });

        createAGroup.setOnClickListener(view1 -> {
            if (listener!= null){
                listener.onCreateGroupClick();
                dismiss();
            }
        });

        return view;
    }
}