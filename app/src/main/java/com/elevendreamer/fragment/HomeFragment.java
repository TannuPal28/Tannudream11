package com.elevendreamer.fragment;

import android.content.Context;
import android.os.Bundle;

import com.elevendreamer.databinding.FragmentHomeBinding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.elevendreamer.Bean.BeanBanner;
import com.elevendreamer.APICallingPackage.Config;
import com.elevendreamer.R;
import com.elevendreamer.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.elevendreamer.APICallingPackage.Config.HOMEBANNER;
import static com.elevendreamer.APICallingPackage.Constants.HOMEBANNERTYPE;


public class HomeFragment extends Fragment implements ResponseManager {



    private int dotscount;
    private ImageView[] dots;
    BannerAdapter bannerAdapter;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;
    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());
        sessionManager = new SessionManager();




        setupViewPager(binding.viewpager);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.viewpager, new FragmentFixtures());
        transaction.commit();

        callHomeBanner(false);

        return binding.getRoot();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new FragmentFixtures(), getResources().getString(R.string.fixture));
        adapter.addFragment(new FragmentLive(), getResources().getString(R.string.live));
        adapter.addFragment(new FragmentResults(), getResources().getString(R.string.result));

        binding.viewpager.setAdapter(adapter);
        binding.FragmentTab.setupWithViewPager(viewPager);

        for (int i = 0; i < binding.FragmentTab.getTabCount(); i++) {
//            TextView tv=(TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab,null);
//            binding.FragmentTab.getTabAt(i).setCustomView(tv);
        }

        viewPager.setOffscreenPageLimit(1);

    }

    private void callHomeBanner(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(HOMEBANNER,
                    createRequestJson(), getActivity(), getActivity(), HOMEBANNERTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(getActivity()).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        binding.collapseToolbar.setVisibility(View.GONE);

        try {
            JSONArray jsonArray = result.getJSONArray("data");
            List<BeanBanner> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<BeanBanner>>() {
                    }.getType());
            bannerAdapter = new BannerAdapter(beanHomeFixtures, getActivity());
            binding.VPSlider.setAdapter(bannerAdapter);

            dotscount = bannerAdapter.getCount();
            dots = new ImageView[dotscount];

            for(int i = 0; i < dotscount; i++){

                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                binding.SliderDots.addView(dots[i], params);

            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.active_dot));

            binding.VPSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for(int i = 0; i< dotscount; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
        bannerAdapter.notifyDataSetChanged();

    }


    @Override
    public void onError(Context mContext, String type, String message) {
        binding.collapseToolbar.setVisibility(View.GONE);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

    public class BannerAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<BeanBanner> mListenerList;

        public BannerAdapter(List<BeanBanner> mListenerList, Context context) {
            this.context = context;
            this.mListenerList = mListenerList;
        }

        @Override
        public int getCount() {
            return mListenerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slider_banner, null);

            ImageView imageView = view.findViewById(R.id.im_banner);

            String Imagename = mListenerList.get(position).getBanner_image();
            final String Type = mListenerList.get(position).getType();

            Glide.with(getActivity()).load(Config.BANNERIMAGE+Imagename)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });



            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }
}
