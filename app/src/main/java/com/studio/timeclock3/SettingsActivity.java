package com.studio.timeclock3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_NOTIFICATION_VISIBLE = "isNotificationVisible";
    public static final String KEY_PREF_NOTIFICATION_PERSISTANT = "isNotificationPersistant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
