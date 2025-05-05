package com.elevendreamer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.elevendreamer.Adapter.Image_Adapter;
import com.elevendreamer.Bean.Dummy;
import com.elevendreamer.R;
import com.elevendreamer.databinding.FragmentDreamCoinsBinding;

import java.util.ArrayList;
import java.util.List;

public class dream_coins extends Fragment {

    private FragmentDreamCoinsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDreamCoinsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        List<Dummy> dummyList = new ArrayList<>();
        dummyList.add(new Dummy(R.drawable.recylerview_img));
        dummyList.add(new Dummy(R.drawable.recylerview_img));
        dummyList.add(new Dummy(R.drawable.recylerview_img));

        // ✅ Setup RecyclerView
        binding.imgRv.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        Image_Adapter adapter = new Image_Adapter(requireContext(), dummyList);
        binding.imgRv.setAdapter(adapter);


        binding.imgRv2.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        Image_Adapter adapterRv2 = new Image_Adapter(requireContext(), dummyList);
        binding.imgRv.setAdapter(adapterRv2);
        return view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // ✅ Prevent memory leaks
    }
}
