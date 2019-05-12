package com.studio.timeclock3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.irozon.sneaker.Sneaker;
import com.irozon.sneaker.interfaces.OnSneakerClickListener;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;
import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogSetup;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.studio.timeclock3.data.AppDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends CyaneaAppCompatActivity implements
        StatisticsFragment.OnFragmentInteractionListener ,
        ListingFragment.OnFragmentInteractionListener,
        OnSneakerClickListener,
        MainOptionsFragment.OnFragmentChangeListener,
        SettingsFragment.OnFragmentChangeListener,
        CustomizationFragment.OnFragmentChangeListener,
        ThemeFragment.OnFragmentChangeListener,
        ExperimentalFragment.OnFragmentChangeListener,
        DataFragment.OnFragmentChangeListener{


    private Map<String, Fragment> map = new HashMap<>();
    private MainActivity activity;

    public void onFragmentInteraction(Uri uri){
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeLogger();
        initializeFragmentMap();
        setMainStartFragment();

        SharedPreferences mSharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbar.setOnClickListener(v -> getCyanea().edit().accent(getRandomColor()).primary(getRandomColor()).apply().recreate(activity));
        mTitle.setText("");


        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_variant_outline, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.chart_pie, "Statistics"))
                .addItem(new BottomNavigationItem(R.drawable.calendar_clock, "Listing"))
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_black_24dp, "Menu"))
                .setFirstSelectedPosition(0)
                .setBarBackgroundColor(R.color.white)
                .setActiveColor(String.format("#%06X", (0xFFFFFF & (getCyanea().getPrimary()))))
                .setInActiveColor(R.color.blue_grey)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise();


        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            Fragment selectedFragment = null;

            @Override
            public void onTabSelected(int position) {
                Logger.i("onTabSelected:" + position);
                //Resume at the choosen position --> Save the screen somehow, to resume later
                mEditor.putInt("NavBarPosition", position).apply();
                mTitle.setText("");
                toolbar.setBackgroundColor(getColor(R.color.white));

                if (position == 0) {
                    Logger.i("MainActivity: @onNavigationItemSelected -> Home");
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = HomeFragment.newInstance();
                    transaction1.replace(R.id.main_fragment_container, selectedFragment);
                    transaction1.commit();
                } else if (position == 1) {
                    Logger.i("MainActivity: @onNavigationItemSelected -> Statistics");
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = StatisticsFragment.newInstance("Param1", "Param2");
                    transaction2.replace(R.id.main_fragment_container, selectedFragment);
                    transaction2.commit();
                } else if (position == 2) {
                    Logger.i("MainActivity: @onNavigationItemSelected -> Listing");
                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = ListingFragment.newInstance("Param1", "Param2");
                    transaction3.replace(R.id.main_fragment_container, selectedFragment);
                    transaction3.commit();
                    toolbar.setBackgroundColor(getCyanea().getPrimary());
                }else if (position == 3) {
                    Logger.i("MainActivity: @onNavigationItemSelected -> MainOptions");
                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = MainOptionsFragment.newInstance("Param1", "Param2");
                    transaction3.replace(R.id.main_fragment_container, selectedFragment);
                    transaction3.commit();
                }

            }
            @Override
            public void onTabUnselected(int position) {
                Logger.i("onTabUnselected:" + position);
            }
            @Override
            public void onTabReselected(int position) {
                Logger.i("onTabReselected:" + position);
            }
        });
    }

    private void initializeFragmentMap() {

        map.put("MainOptions", MainOptionsFragment.newInstance("PORN", "O"));

//        map.put("Calculator", CalculatorFragment.newInstance("What", "Ever"));
//        map.put("Calendar", CalendarFragment.newInstance("What", "Ever"));
        map.put("Settings",SettingsFragment.newInstance("What", "Ever"));
//        map.put("About", AboutFragment.newInstance("What", "Ever"));
//        map.put("Feedback", FeedbackFragment.newInstance("What", "Ever"));
//
        map.put("Theme", ThemeFragment.newInstance("What", "Ever"));
        map.put("Customization", CustomizationFragment.newInstance("What", "Ever"));
//        map.put("Behaviour", BehaviourFragment.newInstance("What", "Ever"));
        map.put("Data", DataFragment.newInstance("What", "Ever"));
//        map.put("Notification", NotificationFragment.newInstance("What", "Ever"));
//        map.put("Language", LanguageFragment.newInstance("What", "Ever"));
        map.put("Experimental", ExperimentalFragment.newInstance("What", "Ever"));

    }

    private void SneakerAlert(int i, String title, String message) {
        //this.getWindow().getDecorView().setSystemUiVisibility(1);
        Sneaker.with(this)
                .setTitle(title, R.color.white) // Title and title color
                .setMessage(message, R.color.white) // Message and message color
                .setDuration(i) // Time duration to show
                .autoHide(true) // Auto hide Sneaker view
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT) // Height of the Sneaker layout
                //.setIcon(R.drawable.timer, R.color.white, false) // Icon, icon tint color and circular icon view
                .setOnSneakerClickListener(this) // Click listener for Sneaker
                //.setCornerRadius(radius, margin) // Radius and margin for round corner Sneaker. - Version 1.0.2
                .sneak(R.color.colorPrimaryDark); // Sneak with background color

    }

    public void initializeChangelog() {
        ChangelogSetup.get().clearTags();

        ChangelogSetup.get().registerTag(new ChangelogTagChange());
        ChangelogSetup.get().registerTag(new ChangelogTagExperiment());
        ChangelogSetup.get().registerTag(new ChangelogTagNew());
        ChangelogSetup.get().registerTag(new ChangelogTagBug());
        ChangelogSetup.get().registerTag(new ChangelogTagInfo());
        ChangelogSetup.get().registerTag(new ChangelogTagFix());
        ChangelogSetup.get().registerTag(new ChangelogTagRemove());
        ChangelogSetup.get().registerTag(new ChangelogTagUpdate());
        ChangelogSetup.get().registerTag(new ChangelogTagRefactor());
        ChangelogSetup.get().registerTag(new ChangelogTagFuture());


        ChangelogDialogFragment builder = new ChangelogBuilder()
                .withUseBulletList(true)
                .buildAndShowDialog(this, false); // second parameter defines, if the dialog has a dark or light theme
            }


    private void initializeLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Logger")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }


    @Override
    protected void onDestroy() {
        Logger.i("MainActivity: @onDestroy");
        AppDatabase.destroyInstance();

        super.onDestroy();
    }

    @Override
    public void onSneakerClick(View view) {

    }

    public void setMainStartFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, HomeFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.i("MainActivity: @onStart");

    }

    @Override
    public void onRestart() {
        super.onRestart();
        Logger.i("MainActivity: @onRestart");
    }

    @Override
    public void OnFragmentChange(String fragment) {
        Logger.i(String.valueOf(fragment));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, Objects.requireNonNull(map.get(fragment))).addToBackStack("");
        transaction.commit();

        //addToBackStack lässt dich durch den BackButton ()HardwareTAste) zurückkommen

//        Toasty.info(this, fragment, Toast.LENGTH_LONG , true).show();

    }
}
