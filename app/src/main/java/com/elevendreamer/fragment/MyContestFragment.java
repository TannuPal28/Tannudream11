package com.elevendreamer.fragment;


import android.os.Bundle;

import com.elevendreamer.databinding.FragmentMyContestBinding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elevendreamer.R;

import java.util.ArrayList;
import java.util.List;

public class MyContestFragment extends Fragment {



    FragmentMyContestBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyContestBinding.inflate(inflater, container, false);



        setupViewPager(binding.myviewpager);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myviewpager, new FragmentMyFixtures());
        transaction.commit();


        return binding.getRoot();
    }

    private void setupViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new FragmentMyFixtures(), "Upcoming");
        adapter.addFragment(new FragmentMyLive(), "Live");
        adapter.addFragment(new FragmentMyResults(), "Completed");
        viewPager.setAdapter(adapter);

        binding.myviewpager.setAdapter(adapter);
        binding.FragmentMyTab.setupWithViewPager(binding.myviewpager);

        for (int i = 0; i < binding.FragmentMyTab.getTabCount(); i++) {
            TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            binding.FragmentMyTab.getTabAt(i).setCustomView(tv);
        }
        viewPager.setOffscreenPageLimit(2);

    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
