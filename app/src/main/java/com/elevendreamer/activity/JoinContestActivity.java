package com.elevendreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.elevendreamer.APICallingPackage.Config;
import com.elevendreamer.R;
import com.elevendreamer.utils.SessionManager;
import com.elevendreamer.databinding.ActivityJoinContestBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Class.Validations;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.elevendreamer.Bean.BeanMyTeamList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.elevendreamer.APICallingPackage.Class.Validations.ShowToast;
import static com.elevendreamer.APICallingPackage.Config.MYTEAMLIST;
import static com.elevendreamer.APICallingPackage.Config.PLAYERLIST;
import static com.elevendreamer.APICallingPackage.Constants.MYTEAMLISTTYPE;
import static com.elevendreamer.APICallingPackage.Constants.PLAYERLISTTYPE;
import static com.elevendreamer.activity.ContestListActivity.IntentTeamOneImg;
import static com.elevendreamer.activity.ContestListActivity.IntentTeamTwoImg;

public class JoinContestActivity extends AppCompatActivity implements ResponseManager {

    JoinContestActivity activity;
    Context context;

    AdapterMyTeamList adapterMyTeamList;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    SessionManager sessionManager;
    BottomSheetDialog dialogGroundView;

    public static  String JoinMyTeamId;



    public static String EntryFee;
    int TeamCount = 0;
    public static String MyContestCode;


    ActivityJoinContestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_join_contest);
        context = activity = this;
        sessionManager = new SessionManager();
        initViews();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        EntryFee = getIntent().getStringExtra("EntryFee");
        MyContestCode = getIntent().getStringExtra("ContestCode");
        binding.inclVsBck.tvHeadTeamOneName.setText(ContestListActivity.IntentTeamOneName);
        binding.inclVsBck.tvHeadTeamTwoName.setText(ContestListActivity.IntentTeamTwoName);
        Glide.with(activity).load(Config.TEAMFLAGIMAGE + IntentTeamOneImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.inclVsBck.imTeam1);
        Glide.with(activity).load(Config.TEAMFLAGIMAGE + IntentTeamTwoImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.inclVsBck.imTeam2);

        binding.inclVsBck.tvContestTimer.setText(ContestListActivity.IntentTime);
        Validations.CountDownTimer(ContestListActivity.IntentTime,binding.inclVsBck.tvContestTimer);

        binding.tvJoinCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TeamCount<6) {
                    ContestListActivity.MyTeamEditorSave="Save";
                    Intent i = new Intent(activity, CreateTeamActivity.class);
                    i.putExtra("Activity", "ContestListActivity");
                    startActivity(i);
                }
                else {
                    ShowToast(context,"You can only create maximum 6 Teams");
                }

            }
        });


        binding.swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.swipeRefreshLayout.setRefreshing(true);
                                        callMyTeamList(false);
                                    }
                                }
        );

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callMyTeamList(false);
            }
        });

    }


    public void initViews(){
        binding.head.tvHeaderName.setText(getResources().getString(R.string.join_contest));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });



        binding.RvJoinMyTeamList.setHasFixedSize(true);
        binding.RvJoinMyTeamList.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.RvJoinMyTeamList.setLayoutManager(mLayoutManager);
        binding.RvJoinMyTeamList.setItemAnimator(new DefaultItemAnimator());

    }

    private void callMyTeamList(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(MYTEAMLIST,
                    createRequestJson(), context, activity, MYTEAMLISTTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", ContestListActivity.IntentMatchId);
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void callMyTeamPlayerList(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(PLAYERLIST,
                    createRequestJson1(), context, activity, PLAYERLISTTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson1() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("matchid", ContestListActivity.IntentMatchId);
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("designationid", "0");
            jsonObject.put("my_team", "1");
            jsonObject.put("my_team_id", JoinMyTeamId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (type.equals(PLAYERLISTTYPE)){

            dialogGroundView = new BottomSheetDialog(activity);
            dialogGroundView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogGroundView.setContentView(R.layout.dialog_ground_view);

            final LinearLayout LL_GroundWK = dialogGroundView.findViewById(R.id.LL_GroundWK);
            final LinearLayout LL_GroundBAT = dialogGroundView.findViewById(R.id.LL_GroundBAT);
            final LinearLayout LL_GroundAR = dialogGroundView.findViewById(R.id.LL_GroundAR);
            final LinearLayout LL_GroundBOWL = dialogGroundView.findViewById(R.id.LL_GroundBOWL);
            ImageView im_CloseIcon = dialogGroundView.findViewById(R.id.im_CloseIcon);
            dialogGroundView.show();
            im_CloseIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogGroundView.dismiss();
                }
            });

            try {
                JSONArray jsonArray = result.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject userData = jsonArray.getJSONObject(i);

                    String PlayerId = userData.getString("player_id");
                    String IsSelected = userData.getString("is_select");
                    String Role = userData.getString("short_term");
                    String player_shortname = userData.getString("player_shortname");
                    String PlayerImage = userData.getString("image");
                    String PlayerCredit = userData.getString("credit_points");
                    String is_captain = userData.getString("is_captain");
                    String is_vicecaptain = userData.getString("is_vicecaptain");
                    if (is_captain==null){
                        is_captain = "0";
                    }
                    if (is_vicecaptain==null){
                        is_vicecaptain = "0";
                    }

                    if (IsSelected.equals("1")) {
                        if (Role.equals("WK")) {
                            View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                    LL_GroundWK, false);
                            ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                            TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                            TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                            TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);
                            Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(im_GroundPlayerImage);
                            tv_GroundPlayerName.setText(player_shortname);
                            tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");

                            if (is_captain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText(" C ");
                            }
                            if (is_vicecaptain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText("VC");
                            }

                            LL_GroundWK.addView(to_add);
                        } else if (Role.equals("BAT")) {
                            View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                    LL_GroundBAT, false);
                            ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                            TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                            TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                            TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);
                            Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(im_GroundPlayerImage);
                            tv_GroundPlayerName.setText(player_shortname);
                            tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");
                            if (is_captain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText(" C ");
                            }
                            if (is_vicecaptain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText("VC");
                            }
                            LL_GroundBAT.addView(to_add);
                        } else if (Role.equals("AR")) {
                            View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                    LL_GroundAR, false);
                            ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                            TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                            TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                            TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);
                            Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(im_GroundPlayerImage);
                            tv_GroundPlayerName.setText(player_shortname);
                            tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");
                            if (is_captain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText(" C ");
                            }
                            if (is_vicecaptain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText("VC");
                            }
                            LL_GroundAR.addView(to_add);
                        } else if (Role.equals("BOWL")) {
                            View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                    LL_GroundBOWL, false);
                            ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                            TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                            TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                            TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);
                            Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(im_GroundPlayerImage);
                            tv_GroundPlayerName.setText(player_shortname);
                            tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");
                            if (is_captain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText(" C ");
                            }
                            if (is_vicecaptain.equals("1")){
                                tv_CorVC.setVisibility(View.VISIBLE);
                                tv_CorVC.setText("VC");
                            }
                            LL_GroundBOWL.addView(to_add);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        else {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.tvNoDataAvailable.setVisibility(View.GONE);
            binding.RvJoinMyTeamList.setVisibility(View.VISIBLE);

            try {
                JSONArray jsonArray = result.getJSONArray("data");
                TeamCount = jsonArray.length();
                List<BeanMyTeamList> beanContestLists = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanMyTeamList>>() {
                        }.getType());
                adapterMyTeamList = new AdapterMyTeamList(beanContestLists, activity);
                binding.RvJoinMyTeamList.setAdapter(adapterMyTeamList);

            } catch (Exception e) {
                e.printStackTrace();
            }
            adapterMyTeamList.notifyDataSetChanged();

        }

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        if (type.equals(MYTEAMLISTTYPE)) {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
            binding.RvJoinMyTeamList.setVisibility(View.GONE);
        }
    }


    public class AdapterMyTeamList extends RecyclerView.Adapter<AdapterMyTeamList.MyViewHolder> {
        private List<BeanMyTeamList> mListenerList;
        Context mContext;


        public AdapterMyTeamList(List<BeanMyTeamList> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_MyTeamName,tv_MyTeamCaptain,tv_MyTeamViceCaptain,tv_MyWKCount,tv_MyBATCount
                    ,tv_MyARCount,tv_MyBOWLCount,tv_JoinContestTeam;

            LinearLayout LL_MyTeamEdit,LL_MyTeamPreview,LL_MyTeamClone;


            public MyViewHolder(View view) {
                super(view);

                tv_MyTeamName = view.findViewById(R.id.tv_MyTeamName);
                tv_MyTeamCaptain = view.findViewById(R.id.tv_MyTeamCaptain);
                tv_MyTeamViceCaptain = view.findViewById(R.id.tv_MyTeamViceCaptain);
                tv_MyWKCount = view.findViewById(R.id.tv_MyWKCount);
                tv_MyBATCount = view.findViewById(R.id.tv_MyBATCount);
                tv_MyBOWLCount = view.findViewById(R.id.tv_MyBOWLCount);
                tv_MyARCount = view.findViewById(R.id.tv_MyARCount);
                tv_JoinContestTeam = view.findViewById(R.id.tv_JoinContestTeam);


                LL_MyTeamEdit = view.findViewById(R.id.LL_MyTeamEdit);
                LL_MyTeamPreview = view.findViewById(R.id.LL_MyTeamPreview);
                LL_MyTeamClone = view.findViewById(R.id.LL_MyTeamClone);



            }

        }
        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_team, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            holder.tv_JoinContestTeam.setVisibility(View.VISIBLE);
            final String Id = mListenerList.get(position).getId();
            final String TeamName = mListenerList.get(position).getTeam_number();
            final String Captain = mListenerList.get(position).getCaptain();
            final String ViceCaptain = mListenerList.get(position).getVicecaptain();
            final String BatCount = mListenerList.get(position).getBatsman();
            final String WkCount = mListenerList.get(position).getWkeeper();
            final String ArCount = mListenerList.get(position).getAllrounder();
            final String BowlCount = mListenerList.get(position).getBoller();


            holder.tv_MyWKCount.setText(WkCount);
            holder.tv_MyBATCount.setText(BatCount);
            holder.tv_MyARCount.setText(ArCount);
            holder.tv_MyBOWLCount.setText(BowlCount);
            holder.tv_MyTeamName.setText(TeamName);
            holder.tv_MyTeamCaptain.setText(Captain);
            holder.tv_MyTeamViceCaptain.setText(ViceCaptain);


            holder.LL_MyTeamPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JoinMyTeamId = mListenerList.get(position).getId();
                    callMyTeamPlayerList(true);

                }
            });

            holder.LL_MyTeamClone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TeamCount<6) {
                        JoinMyTeamId = mListenerList.get(position).getId();
                        ContestListActivity.JoinMyTeamId = mListenerList.get(position).getId();

                        ContestListActivity.MyTeamEditorSave = "Clone";
                        Intent i = new Intent(activity,CreateTeamActivity.class);
                        i.putExtra("Activity", "ContestListActivity");
                        startActivity(i);
                    }
                    else {
                        ShowToast(context,"You can only create maximum 6 Teams");
                    }

                }
            });

            holder.LL_MyTeamEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JoinMyTeamId = mListenerList.get(position).getId();
                    ContestListActivity.JoinMyTeamId= mListenerList.get(position).getId();

                    ContestListActivity.MyTeamEditorSave = "Edit";
                    Intent i = new Intent(activity,CreateTeamActivity.class);
                    i.putExtra("Activity", "ContestListActivity");
                    startActivity(i);

                }
            });

            holder.tv_JoinContestTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JoinMyTeamId = mListenerList.get(position).getId();
                    ContestListActivity.JoinMyTeamId= mListenerList.get(position).getId();
                    Intent i = new Intent(activity,PaymentConfirmationActivity.class);
                    i.putExtra("EntryFee",EntryFee);
                    i.putExtra("MyTeamId",JoinMyTeamId);
                    i.putExtra("MyContestCode", MyContestCode);
                    startActivity(i);

                }
            });


        }

    }
}