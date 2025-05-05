package com.elevendreamer.activity;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityDirectChatContactsBinding;
import com.elevendreamer.fragment.ContactsFragment;
import com.elevendreamer.fragment.FriendsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DirectChatContacts extends AppCompatActivity {

    private DirectChatContacts activity;
    private Context context;
    private ActivityDirectChatContactsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_chat_contacts);
        context = activity = this;

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


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);

        EditText searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(this,R.color.black));
        searchEditText.setHintTextColor(ContextCompat.getColor(this,R.color.grey600));
        searchEditText.setBackgroundColor(Color.TRANSPARENT); // optional
        binding.searchView.setQueryHint("Search for others on Dream11");
        binding.searchView.setIconified(false); // <-- expands it
        binding.searchView.setIconifiedByDefault(false); // <-- disables collapse

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendSearchQueryToFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sendSearchQueryToFragment(newText);
                return true;
            }
        });


        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Contacts");
            } else {
                tab.setText("Friends");
            }
        }).attach();

        for (int i= 0;i<binding.tabLayout.getTabCount();i++){
            TabLayout.Tab tab= binding.tabLayout.getTabAt(i);

            if (tab!= null){
                TextView tabTextView = new TextView(this);
                tabTextView.setText(tab.getText());
                tabTextView.setGravity(Gravity.CENTER);
                tabTextView.setPadding(20, 10, 20, 10);
                // Set background for the first tab, else leave it null
                if (i == 0) {
                    tabTextView.setBackgroundResource(R.drawable.bg_black_btn);
                    tabTextView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tabTextView.setBackground(null);
                    tabTextView.setTextColor(getResources().getColor(R.color.black));
                }
//                tabTextView.setBackground(null); // No background by default?
                tab.setCustomView(tabTextView);
            }
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                    view.setBackgroundResource(R.drawable.bg_black_btn);
                    ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view != null) {
                    view.setBackgroundColor(ContextCompat.getColor(context,R.color.grey100));
                    ((TextView) view).setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void sendSearchQueryToFragment(String query) {
        int currentItem = binding.viewPager.getCurrentItem();
        Fragment currentFragment= getSupportFragmentManager().findFragmentByTag("f"+ currentItem);

        if (currentFragment instanceof ContactsFragment){
            ((ContactsFragment) currentFragment).filterContacts(query);
        }else {
            Log.d("tag","Freinds fragment remain");
        }
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new ContactsFragment();
            } else {
                return new FriendsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
