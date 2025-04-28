package com.elevendreamer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.elevendreamer.R;
import com.elevendreamer.databinding.ActivityHomeBinding;
import com.elevendreamer.fragment.HomeFragment;
import com.elevendreamer.fragment.MoreFragment;
import com.elevendreamer.fragment.MyContestFragment;
import com.elevendreamer.fragment.ProfileFragment;
import com.elevendreamer.utils.SessionManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.elevendreamer.APICallingPackage.Class.Validations.ShowToast;
import static com.elevendreamer.APICallingPackage.Config.APKNAME;
import static com.elevendreamer.APICallingPackage.Config.APKURL;
import static com.elevendreamer.APICallingPackage.Config.UPDATEAPP;
import static com.elevendreamer.APICallingPackage.Constants.UPDATEAPPTYPE;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ResponseManager {

    private int[] tabIcons = {
            R.drawable.home_icon,
            R.drawable.contest_icon,
            R.drawable.profile_icon,
            R.drawable.more_icon
    };

    Context context;

    HomeActivity activity;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;

    public static GoogleApiClient mGoogleApiClient;
    Typeface LatoBold, LatoRegular, Ravenscroft;
    public static SessionManager sessionManager;

    int progressStatus = 0;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog pDialog;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    Dialog dialog;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = activity = this; // ✅ Moved this line before binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
//#4a0d0c
        getWindow().setStatusBarColor(Color.parseColor("#1a1a1a"));

        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Animation shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
        binding.imNotification.startAnimation(shake);

        //NavigationView navigationView= binding.navigationView;
        //  View headerView= navigationView.getHeaderView(0);

        //ImageView walletImage = headerView.findViewById(R.id.wallet6);
        // walletImage.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);

        binding.imNotification.setOnClickListener(view -> {
            Intent i = new Intent(activity, NotificationActivity.class);
            startActivity(i);
        });

        binding.imHomewallet.setOnClickListener(view -> {
            if (activity != null) {
                Intent i = new Intent(activity, MyAccountActivity.class);
                startActivity(i);
            } else {
                Log.e("HomeActivity", "Activity is null!");
            }
        });

        Ravenscroft = Typeface.createFromAsset(this.getAssets(), "Ravenscroft.ttf");
        String Name = sessionManager.getUser(context).getName();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        setupTabIcons1();
        replaceFragment(new HomeFragment());

        binding.tabs.setOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(activity, R.color.tabtextselected);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                ImageView logo = binding.logo;
                TextView title = binding.title;
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(new HomeFragment());
                        binding.head.setVisibility(View.VISIBLE);
                        logo.setVisibility(View.VISIBLE);
                        title.setVisibility(View.GONE);
                        break;
                    case 1:
                        replaceFragment(new MyContestFragment());
                        binding.head.setVisibility(View.VISIBLE);
                        logo.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        title.setText("My Matches");
                        break;
                    case 2:
                        replaceFragment(new ProfileFragment());
                        binding.head.setVisibility(View.GONE);
                        break;
                    default:
                        replaceFragment(new MoreFragment());
                        binding.head.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(activity, R.color.tabtextunselected);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {
            }
        });

        // Uncomment to enable in-app update check
        // callCheckUpdateVersion(false);

        /*if (drawerLayout == null) {
            Log.e("InitializationError", "DrawerLayout is null. Check XML file for proper ID.");
        }*/

        // setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void callCheckUpdateVersion(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(UPDATEAPP, createRequestJson(), context, activity, UPDATEAPPTYPE, isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (type.equals(UPDATEAPPTYPE)) {
            try {
                String Note = result.getString("note");
                String NewV = result.getString("new_version");
                String MobileVName = " BuildConfig.VERSION_NAME";

                if (!MobileVName.equals(NewV)) {
                    Dialog(Note, "Update", "What's new");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Context mContext, String type, String message) {
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DOWNLOAD_PROGRESS) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file..");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
            return pDialog;
        }
        return null;
    }

    public void Dialog(String UpdateNote, String UpdateorInstall, String WhatsnewHead) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);

        final TextView tv_DClose = dialog.findViewById(R.id.tv_DClose);
        final TextView tv_UpdateNote = dialog.findViewById(R.id.tv_UpdateNote);
        final TextView tv_UpdateApp = dialog.findViewById(R.id.tv_UpdateApp);
        final TextView tv_WhatsNewHead = dialog.findViewById(R.id.tv_WhatsNewHead);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        tv_UpdateNote.setText(UpdateNote);
        tv_UpdateApp.setText(UpdateorInstall);
        tv_WhatsNewHead.setText(WhatsnewHead);

        tv_DClose.setOnClickListener(view -> dialog.dismiss());

        tv_UpdateApp.setOnClickListener(view -> {
            if (tv_UpdateApp.getText().toString().equals("Update")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    List<String> listPermissionsNeeded = new ArrayList<>();
                    for (String p : permissions) {
                        if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                            listPermissionsNeeded.add(p);
                        }
                    }
                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions(activity,
                                listPermissionsNeeded.toArray(new String[0]), 100);
                    } else {
                        new DownloadFileFromURL().execute(APKURL);
                    }
                } else {
                    new DownloadFileFromURL().execute(APKURL);
                }
            } else {
                File apk = new File(Environment.getExternalStorageDirectory().toString() + "/" + APKNAME);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 23) {
                        Uri photoURI = FileProvider.getUriForFile(activity,
                                activity.getApplicationContext().getPackageName() + ".provider", apk);
                        intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupTabIcons1() {
        binding.tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.tabtextselected), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.tabtextunselected), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.tabtextunselected), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.tabtextunselected), PorterDuff.Mode.SRC_IN);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setPositiveButton("Exit", (dialog, id) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        ab.setNeutralButton("Logout", (dialog, id) -> Logout());
        ab.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        ab.create().show();
    }

    public void Logout() {
        LoginManager.getInstance().logOut();
        loginPrefsEditor.clear().apply();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(status -> {});
        startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.dismiss();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + APKNAME);
                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            Dialog("Your Update is ready to install", "Install", "Install App");
        }
    }
}
