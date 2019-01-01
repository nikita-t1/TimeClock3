package com.studio.timeclock3;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_NOTIFICATION_VISIBLE = "isNotificationVisible";
    public static final String KEY_PREF_NOTIFICATION_PERSISTANT = "isNotificationPersistant";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_settings);

        Button button = (Button) findViewById(R.id.settings_button1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        View view = (View) findViewById(R.id.settings_fragment_main);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(null);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }




        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_fragment_main, new SettingsFragment())
                .commit();

//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                //((ViewGroup) view.getParent()).removeView(view);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.settings_fragment_main, new SettingsFragment())
//                        .commit();
//            }
//        });
    }

    public void test(View view) {


        Toasty.info(this, "YES").show();
    }
}

