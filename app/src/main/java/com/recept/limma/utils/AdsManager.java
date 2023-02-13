package com.recept.limma.utils;

import android.app.Activity;

import com.recept.limma.databases.prefs.AdsPref;
import com.recept.limma.databases.prefs.SharedPref;

public class AdsManager {

    Activity activity;
    SharedPref sharedPref;
    AdsPref adsPref;

    public AdsManager(Activity activity) {
        this.activity = activity;
        this.sharedPref = new SharedPref(activity);
        this.adsPref = new AdsPref(activity);

    }



}
