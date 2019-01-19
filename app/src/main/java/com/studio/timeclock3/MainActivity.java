package com.studio.timeclock3;


import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.irozon.sneaker.Sneaker;
import com.irozon.sneaker.interfaces.OnSneakerClickListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.michaelflisar.changelog.Changelog;
import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogSetup;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.viewpager.widget.ViewPager;
import es.dmoral.toasty.Toasty;
import jonathanfinerty.once.Once;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener , StatisticsFragment.OnFragmentInteractionListener , ListingFragment.OnFragmentInteractionListener, OnSneakerClickListener {

    private static String DEFAULT_CHANNEL_ID = "default_channel";
    private static String DEFAULT_CHANNEL_NAME = "Default";
    private NotificationManager mNotificationManager;
    int nId = 1;
    public boolean isNotificationVisible = false;
    public boolean isNotificationPersistant = false;


    private ViewPager viewPager;
    String intro = "intro";
    String changelog = "changelog";


    MainOptionsBottomSheetDialogFragment mainOptionsBottomSheetDialogFragment;
    CalculatorBottomSheetDialogFragment calculatorBottomSheetDialogFragment;


    public void onFragmentInteraction(Uri uri){
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment selectedFragment = null;
//
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//
//                    Logger.i("MainActivity: @onNavigationItemSelected -> Home");
//                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
//                    selectedFragment = HomeFragment.newInstance("Param1", "Param2");
//                    transaction1.replace(R.id.main_fragment_container, selectedFragment);
//                    transaction1.commit();
//                    return true;
//                case R.id.navigation_dashboard:
//                    //fm.beginTransaction().hide(active).show(fragment2).commit();
//                    //active = fragment2;
//                    Logger.i("MainActivity: @onNavigationItemSelected -> Statistics");
//                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
//                    selectedFragment = StatisticsFragment.newInstance("Param1", "Param2");
//                    transaction2.replace(R.id.main_fragment_container, selectedFragment);
//                    transaction2.commit();
//                    return true;
//                case R.id.navigation_listing:
//                    //fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).hide(active).show(fragment3).commit();
//                    //active = fragment3;
//                    Logger.i("MainActivity: @onNavigationItemSelected -> Listing");
//                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
//                    selectedFragment = ListingFragment.newInstance("Param1", "Param2");
//                    transaction3.replace(R.id.main_fragment_container, selectedFragment);
//                    transaction3.commit();
//                    return true;
//            }
//            return false;
//        }
//    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        setTheme(R.style.AppThemeMint);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeLogger();
        Once.initialise(this);

        Logger.e(String.valueOf(Integer.valueOf(70/60)));


        Logger.i("LogCat Info");
        Logger.w("LogCat Warning");
        Logger.wtf("LogCat WTF");
        Logger.e("LogCat Error");


        String recreateFragment = "recreateFragment";
        Log.i("TimeClock", "STARTED");

        if (!Once.beenDone(Once.THIS_APP_VERSION, changelog)) {
            initializeChangelog();
            Once.markDone(changelog);
        }

        setMainStartFragment();

        SharedPreferences mSharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);









        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_statistics_black_24dp, "Statistics"))
                .addItem(new BottomNavigationItem(R.drawable.calendar_clock, "Listing"))
                .addItem(new BottomNavigationItem(R.drawable.ic_menu_black_24dp, "Menu"))
                .setFirstSelectedPosition(0)
                .setBarBackgroundColor(R.color.colorPrimary)
                .setActiveColor(R.color.white)
                .setInActiveColor(R.color.blue_grey)
                .setMode(BottomNavigationBar.MODE_SHIFTING)
                .initialise();


        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            Fragment selectedFragment = null;

            @Override
            public void onTabSelected(int position) {
                Logger.i("onTabSelected:" + position);

                if (position == 0) {
                    Logger.i("MainActivity: @onNavigationItemSelected -> Home");
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = HomeFragment.newInstance("Param1", "Param2");
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
                }else if (position == 3) {
                    Toasty.info(MainActivity.this, "SOON...").show();
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("TimeClock");

//        mTitle.setText("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                bottomSheetView(view);
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //CoordinatorLayout coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        //coordinator.setScaleY(-1f);
        //TextView textView = (TextView) findViewById(R.id.toolbar_title);
        //textView.setScaleY(-1f);
        //floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        //floatingActionButton.hide();
        //FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_fragment_container);
        //frameLayout.setScaleY(-1f);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean isNotificationVisible = sharedPref.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION_VISIBLE, false);
        Boolean isNotificationPersistant = sharedPref.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION_PERSISTANT, false);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.pornhub.com"));

//          PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

        //1.Get reference to Notification Manager
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(mNotificationManager);

        //2.Build Notification with NotificationCompat.Builder
        if (isNotificationVisible) {

            if (isNotificationPersistant) {

                Notification notification = new NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
                        .setContentTitle("Test Notification")   //Set the title of Notification
                        .setContentText("Click on Notification to open \t -> pornhub.com")    //Set the text for notification
                        .setSmallIcon(R.drawable.ic_timelapse_black_24dp)   //Set the icon
                        .setContentIntent(pi)
                        .setAutoCancel(true) // Hides the notification after its been selected
                        .setOngoing(true)
                        .build();
                
                notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

                //Send the notification.
                mNotificationManager.notify(nId, notification);
            } else {
                Notification notification = new NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
                        .setContentTitle("Test Notification")   //Set the title of Notification
                        .setContentText("Click on Notification to open \t -> pornhub.com")    //Set the text for notification
                        .setSmallIcon(R.drawable.ic_timelapse_black_24dp)   //Set the icon
                        .setContentIntent(pi)
                        .setAutoCancel(true) // Hides the notification after its been selected
                        .setOngoing(false)
                        .build();

                //Send the notification.
                mNotificationManager.notify(nId, notification);
            }
        } else {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(nId);
        }

//        TimePicker picker= (TimePicker) findViewById(R.id.timePicker1);
//        picker.setIs24HourView(true);

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

    public void initializeFutureFeature() {


        String[] some_array = getResources().getStringArray(R.array.features);

        String str = String.join(",", some_array);
        str=str.replaceAll(",", "\n\n");

        new MaterialStyledDialog.Builder(this)
                .setHeaderScaleType(ImageView.ScaleType.CENTER_CROP)
                .setIcon(R.drawable.ic_future_black_24dp)
                .setTitle("Future:")
                .setHeaderColor(R.color.blue)
                .setDescription(str)
                .setStyle(Style.HEADER_WITH_ICON)
                .withDialogAnimation(false)
                .setPositiveText("OK")
                .withDivider(false)
                .withIconAnimation(false)
                .build()
                .show();

    }

    public void calculatorView(View view) {

        calculatorBottomSheetDialogFragment = CalculatorBottomSheetDialogFragment.newInstance();
        calculatorBottomSheetDialogFragment.show(getSupportFragmentManager(), "CalculatorBottomSheetDialogFragment");
        mainOptionsBottomSheetDialogFragment.dismiss();

        //startActivity(new Intent(this, ActivityDebugTools.class));

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

        if (!isNotificationPersistant) {

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.cancel(nId);
            }
        }

        super.onDestroy();
    }

    public void bottomSheetView(View view) {
        mainOptionsBottomSheetDialogFragment = MainOptionsBottomSheetDialogFragment.newInstance();
        mainOptionsBottomSheetDialogFragment.show(getSupportFragmentManager(), "MainOptionsBottomSheetDialogFragment");
    }
    public void settingsbutton(View view) {
        Logger.i("MainActivity: Intent -> Settings");

        Intent intent = new Intent(this, SettingsActivity.class);
        mainOptionsBottomSheetDialogFragment.dismiss();
        startActivity(intent);
    }
    public void aboutButton(View view) {
        Logger.i("MainActivity: Intent -> About");
        Intent intent = new Intent(this, ExampleMaterialAboutActivity.class);
        mainOptionsBottomSheetDialogFragment.dismiss();
        intent.putExtra(ExampleMaterialAboutActivity.THEME_EXTRA, 2);
        startActivity(intent);    }

    public static void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create channel only if it is not already created
            if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(
                        DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
                ));
            }
        }
    }

    @Override
    public void onSneakerClick(View view) {

    }

    public void setMainStartFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, HomeFragment.newInstance("What", "Ever"));
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.i("MainActivity: @onStart");

        //Unten stehender Code wird benötigt um statt der Default WORKING_TIME_HOURS die eingegebene Zeit nach dem Intro zu berechnen,
        //  da die ursprüngliche Methode in der @onCreateView schon ausgeführ wurde wenn das Intro started,
        //      wohingegen diese Methode ausgeführt wird wenn das Fragment in den Vordergrund rückt!!
        //          siehe -> HomwFragment @onResume
        if (!Once.beenDone(Once.THIS_APP_INSTALL, intro)) {
            Intent intent156 = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent156);
            Once.markDone(intro);
        } else {
            setMainStartFragment();
        }
    }
}
