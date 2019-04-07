package com.studio.timeclock3;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.ColorFilter;

import io.multimoon.colorful.ColorfulKt;
import io.multimoon.colorful.Defaults;
import io.multimoon.colorful.ThemeColor;

public class MyCustomApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
//        Defaults defaults = new Defaults(ThemeColor.LIME, ThemeColor.AMBER, true, false, 0);
//        ColorfulKt.initColorful(this, defaults);
        // Required initialization logic here!
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}