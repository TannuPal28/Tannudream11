package com.elevendreamer.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.elevendreamer.databinding.FragmentFixturesBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.elevendreamer.Bean.BeanHomeFixtures;
import com.elevendreamer.APICallingPackage.Config;
import com.elevendreamer.activity.ContestListActivity;
import com.elevendreamer.activity.HomeActivity;
import com.elevendreamer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.elevendreamer.APICallingPackage.Class.Validations.ShowToast;
import static com.elevendreamer.APICallingPackage.Config.HOMEFIXTURES;
import static com.elevendreamer.APICallingPackage.Constants.FIXTURESHOMETYPE;

import javax.sql.DataSource;


public class FragmentFixtures extends Fragment implements ResponseManager {


    HomeActivity activity;
    Context context;
    AdapterFixturesList adapterFixturesList;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;


    public static String TeamImage1, TeamImage2;

    FragmentFixturesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFixturesBinding.inflate(inflater, container, false);
        context = activity = (HomeActivity) getActivity();


        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        binding.RvHomeFixtures.setHasFixedSize(true);
        binding.RvHomeFixtures.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.RvHomeFixtures.setLayoutManager(mLayoutManager);
        binding.RvHomeFixtures.setItemAnimator(new DefaultItemAnimator());


        binding.swipeRefreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                binding.swipeRefreshLayout.setRefreshing(true);
                                                callHomeFixtures(false);
                                            }
                                        }
        );
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callHomeFixtures(false);
            }
        });


        return binding.getRoot();
    }


    private void callHomeFixtures(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(HOMEFIXTURES,
                    createRequestJson(), context, activity, FIXTURESHOMETYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Fixture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.RvHomeFixtures.setVisibility(View.VISIBLE);
        binding.tvNoDataAvailable.setVisibility(View.GONE);

        try {
            JSONArray jsonArray = result.getJSONArray("data");
            List<BeanHomeFixtures> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<BeanHomeFixtures>>() {
                    }.getType());
            adapterFixturesList = new AdapterFixturesList(beanHomeFixtures, activity);
            binding.RvHomeFixtures.setAdapter(adapterFixturesList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapterFixturesList.notifyDataSetChanged();

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.RvHomeFixtures.setVisibility(View.GONE);
        binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
    }

    public class AdapterFixturesList extends RecyclerView.Adapter<AdapterFixturesList.MyViewHolder> {
        private List<BeanHomeFixtures> mListenerList;
        Context mContext;


        public AdapterFixturesList(List<BeanHomeFixtures> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TeamOneName, tv_TeamsName, tv_TimeRemained, tv_TeamTwoName,tv_MatchTime, tv_TeamOneNamefull, tv_TeamTwoNamefull;
            ImageView im_Team1, im_Team2;
            CountDownTimer countDownTimer;
         ImageView im_Team1bg ;
         ImageView im_Team2bg;

            public MyViewHolder(View view) {
                super(view);

                im_Team1 = view.findViewById(R.id.im_Team1);
                tv_TeamOneName = view.findViewById(R.id.tv_TeamOneName);
                tv_TeamsName = view.findViewById(R.id.tv_TeamsName);
                tv_TimeRemained = view.findViewById(R.id.tv_TimeRemained);
                im_Team2 = view.findViewById(R.id.im_Team2);
                tv_TeamTwoName = view.findViewById(R.id.tv_TeamTwoName);
                tv_MatchTime = view.findViewById(R.id.tv_MatchTime);
                im_Team1bg = view.findViewById(R.id.im_Team1bg);
                im_Team2bg = view.findViewById(R.id.im_Team2bg);
                tv_TeamOneNamefull = view.findViewById(R.id.tv_TeamOneNamefull);
                tv_TeamTwoNamefull = view.findViewById(R.id.tv_TeamTwoNamefull);
            }

        }

        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_fixtures_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final String match_id = mListenerList.get(position).getMatch_id();
            String teamid1 = mListenerList.get(position).getTeamid1();
            String match_status = mListenerList.get(position).getMatch_status();
            String type = mListenerList.get(position).getType();
            final int time = mListenerList.get(position).getTime();
            String teamid2 = mListenerList.get(position).getTeamid2();
            final String team_name1 = mListenerList.get(position).getTeam_name1();
            final String team_image1 = mListenerList.get(position).getTeam_image1();
            final String team_short_name1 = mListenerList.get(position).getTeam_short_name1();
            Log.d("team_short_name1", team_short_name1);
            final String team_name2 = mListenerList.get(position).getTeam_name2();
            final String team_image2 = mListenerList.get(position).getTeam_image2();
            final String team_short_name2 = mListenerList.get(position).getTeam_short_name2();
            final String eleven_out = mListenerList.get(position).getEleven_out();

            if (eleven_out.equals("1")){
                holder.tv_MatchTime.setVisibility(View.VISIBLE);
                holder.tv_MatchTime.setText("Lineup Out");
            }else {
                holder.tv_MatchTime.setVisibility(View.GONE);
            }
            holder.tv_TeamTwoNamefull.setText(team_name2);
            holder.tv_TeamOneNamefull.setText(team_name1);
            holder.tv_TeamOneName.setText(team_short_name1);
            holder.tv_TeamTwoName.setText(team_short_name2);
            holder.tv_TeamsName.setText(type);
            Glide.with(getActivity())
                    .load(team_image1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team1);
            Glide.with(getActivity())
                    .load(team_image1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team1bg);
            Glide.with(getActivity())
                    .load(team_image2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team2bg);
            Log.d("team_image1", Config.TEAMFLAGIMAGE + team_image1);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE + team_image2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team2);

            if (match_status.equals("Fixture")) {
                holder.tv_TimeRemained.setCompoundDrawablesWithIntrinsicBounds(R.drawable.watch_icon_cont, 0, 0, 0);
                holder.tv_TimeRemained.setText(time + "");

                if (holder.countDownTimer != null) {
                    holder.countDownTimer.cancel();
                }

                try {
                    int FlashCount = time;
                    long millisUntilFinished = FlashCount * 1000L;

                    holder.countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                            long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;

                            StringBuilder timeBuilder = new StringBuilder();
                            if (days > 0) {
                                holder.tv_TimeRemained.setText("Tomorrow");
                            }else{
                            if (hours > 0 || days > 0) timeBuilder.append(hours).append("h ");
                            timeBuilder.append(minutes).append("m ");
                            holder.tv_TimeRemained.setText(timeBuilder.toString().trim());}
                        }

                        public void onFinish() {
                            callHomeFixtures(false);
                            holder.tv_TimeRemained.setText("Entry Over!");
                        }

                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.tv_TimeRemained.getText().toString().equals("Entry Over!")) {
                            ShowToast(context, "Entry Over");
                        } else {
                            TeamImage1 = team_image1;
                            TeamImage2 = team_image2;
                            Intent k = new Intent(activity, ContestListActivity.class);
                            k.putExtra("MatchId", match_id);
                            k.putExtra("Time", time + "");
                            k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                            k.putExtra("TeamsOneName", team_short_name1);
                            k.putExtra("TeamsTwoName", team_short_name2);
                            k.putExtra("TeamsOneName", team_name1);
                            k.putExtra("TeamsTwoName", team_name2);
                            k.putExtra("TeamsOneImg", team_image1);
                            k.putExtra("TeamsTwoImg", team_image2);
                            k.putExtra("teamoneshortname", team_short_name1);
                            k.putExtra("teamtwoshortname", team_short_name2);
                            startActivity(k);
                        }
                    }
                });


            }

        }

    }

}
