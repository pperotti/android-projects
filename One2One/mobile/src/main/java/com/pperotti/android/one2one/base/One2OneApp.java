package com.pperotti.android.one2one.base;

import android.support.multidex.MultiDexApplication;

import com.orm.SugarContext;

/**
 * Created by pperotti on 13/6/16.
 */
public class One2OneApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
