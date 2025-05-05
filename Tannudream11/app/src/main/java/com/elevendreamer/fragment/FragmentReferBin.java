package com.elevendreamer.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elevendreamer.R;
import com.elevendreamer.databinding.FragmentReferBinBinding;

public class FragmentReferBin extends Fragment {

    FragmentReferBinBinding binding;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReferBinBinding.inflate(inflater, container, false);
        context = getActivity();


        binding.tvQuestion1.setOnClickListener(v -> {
            if (binding.tvAnswer1.getVisibility() == View.GONE) {
                binding.tvAnswer1.setVisibility(View.VISIBLE);
                binding.tvQuestion1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_24px, 0);

            } else {
                binding.tvAnswer1.setVisibility(View.GONE);
                binding.tvQuestion1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_24px, 0);
            }
        });

        return  binding.getRoot();

    }
}