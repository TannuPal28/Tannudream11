package com.elevendreamer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elevendreamer.APICallingPackage.Config;
import com.elevendreamer.activity.HomeActivity;
import com.elevendreamer.activity.InviteFriendsActivity;
import com.elevendreamer.activity.WebviewAcitivity;
import com.elevendreamer.databinding.FragmentMoreBinding;


public class MoreFragment extends Fragment {


    HomeActivity activity;
    Context context;

    FragmentMoreBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater, container, false);

        context = activity = (HomeActivity)getActivity();


        binding.RLMoreInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, InviteFriendsActivity.class);
                startActivity(i);
            }
        });

        binding.RLFantasyPointSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","FANTASY POINT SYSTEM");
                i.putExtra("URL", Config.FANTASYPOINTSYSTEMURL);
                startActivity(i);
            }
        });
        binding.RLMoreHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","HOW TO PLAY");
                i.putExtra("URL",Config.HOWTOPLAYURL);
                startActivity(i);
            }
        });
        binding.RLMoreAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","ABOUT US");
                i.putExtra("URL",Config.ABOUTUSURL);
                startActivity(i);
            }
        });
        binding.RLMoreHelpDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","HELP DESK");
                i.putExtra("URL",Config.HELPDESKURL);
                startActivity(i);
            }
        });
        binding.RLPRICING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","PRICING");
                i.putExtra("URL",Config.PRICING);
                startActivity(i);
            }
        });
        binding.RLMoreLegality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","LEGALITY");
                i.putExtra("URL",Config.LEGALITY);
                startActivity(i);
            }
        });
        binding.RLMoreRefundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","REFUND POLICY");
                i.putExtra("URL",Config.REFUNDPOLICY);
                startActivity(i);
            }
        });
        binding.RLMoreWithdrawPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","WITHDRAW POLICY");
                i.putExtra("URL",Config.WITHDRAWPOLICY);
                startActivity(i);
            }
        });
        return binding.getRoot();
    }





}
