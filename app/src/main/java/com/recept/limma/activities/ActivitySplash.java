package com.recept.limma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.recept.limma.BuildConfig;
import com.recept.limma.R;
import com.recept.limma.callbacks.CallbackAds;
import com.recept.limma.config.AppConfig;
import com.recept.limma.databases.prefs.SharedPref;
import com.recept.limma.rests.RestAdapter;
import com.recept.limma.utils.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySplash extends AppCompatActivity {

    public static final String TAG = "ActivitySplash";
    ProgressBar progressBar;
    ImageView img_splash;
    SharedPref sharedPref;
    Call<CallbackAds> callbackCall = null;
    private boolean isAdShown = false;
    private boolean isAdDismissed = false;
    private boolean isLoadCompleted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.getTheme(this);
        setContentView(R.layout.activity_splash);
        Tools.setNavigation(this);
        sharedPref = new SharedPref(this);

        img_splash = findViewById(R.id.img_splash);
        if (sharedPref.getIsDarkTheme()) {
            img_splash.setImageResource(R.drawable.bg_splash_dark);
        } else {
            img_splash.setImageResource(R.drawable.bg_splash_default);
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        loadConfig();

    }

    private void requestAds(String apiUrl) {
        this.callbackCall = RestAdapter.createAPI(apiUrl).getAds(AppConfig.REST_API_KEY);
        this.callbackCall.enqueue(new Callback<CallbackAds>() {
            public void onResponse(Call<CallbackAds> call, Response<CallbackAds> response) {
                CallbackAds resp = response.body();
                if (resp != null && resp.status.equals("ok")) {

                    Log.d("Response", "Ads Data is saved");
                    onSplashFinished();
                } else {
                    onSplashFinished();
                }
            }

            public void onFailure(Call<CallbackAds> call, Throwable th) {
                Log.e("onFailure", "" + th.getMessage());
                onSplashFinished();
            }
        });
    }

    private void loadConfig() {
        String decode = Tools.decodeBase64(AppConfig.SERVER_KEY);
        String data = Tools.decrypt(decode);
        String[] results = data.split("_applicationId_");
        String apiUrl = results[0].replace("http://localhost", "http://10.0.2.2");
        String applicationId = results[1];
        sharedPref.saveConfig(apiUrl, applicationId);

        if (applicationId.equals(BuildConfig.APPLICATION_ID)) {
            if (Tools.isConnect(this)) {
                requestAds(apiUrl);
            } else {
                launchMainScreen();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Whoops! invalid server key or applicationId, please check your configuration")
                    .setPositiveButton(getString(R.string.dialog_ok), (dialog, which) -> finish())
                    .setCancelable(false)
                    .show();
        }
    }

    private void onSplashFinished() {
        launchMainScreen();
    }

    private void launchMainScreen() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        new Handler(Looper.getMainLooper()).postDelayed(this::finish, 2000);
    }



}
