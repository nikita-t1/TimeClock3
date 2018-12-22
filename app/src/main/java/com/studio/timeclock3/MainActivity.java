package com.studio.timeclock3;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogSetup;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements
HomeFragment.OnFragmentInteractionListener ,
StatisticsFragment.OnFragmentInteractionListener ,
ListingFragment.OnFragmentInteractionListener {

    private static String DEFAULT_CHANNEL_ID = "default_channel";
    private static String DEFAULT_CHANNEL_NAME = "Default";
    private NotificationManager mNotificationManager;
    int nId = 1;
    public boolean isNotificationVisible = false;
    public boolean isNotificationPersistant = false;



    MainOptionsBottomSheetDialogFragment mainOptionsBottomSheetDialogFragment;
    CalculatorBottomSheetDialogFragment calculatorBottomSheetDialogFragment;



    public void onFragmentInteraction(Uri uri){
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = HomeFragment.newInstance("Param1", "Param2");
                    transaction1.replace(R.id.main_fragment_container, selectedFragment);
                    transaction1.commit();
                    return true;
                case R.id.navigation_dashboard:
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = StatisticsFragment.newInstance("Param1", "Param2");
                    transaction2.replace(R.id.main_fragment_container, selectedFragment);
                    transaction2.commit();
                    return true;
                case R.id.navigation_notifications:
                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = ListingFragment.newInstance("Param1", "Param2");
                    transaction3.replace(R.id.main_fragment_container, selectedFragment);
                    transaction3.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeLogger();
        initializeChangelog();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, HomeFragment.newInstance("What", "Ever"));
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.decor_content_parent);
        setSupportActionBar(toolbar);

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

        Log.i("INFORMATION", "isNotificationPersistant == !!!TRUE!!!");
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


        Toasty.info(this, "App started", Toast.LENGTH_LONG, true).show();
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

        ChangelogDialogFragment builder = new ChangelogBuilder()
                .withUseBulletList(true)
                .buildAndShowDialog(this, false); // second parameter defines, if the dialog has a dark or light theme
    }

    private void initializeFutureFeature() {


        String[] some_array = getResources().getStringArray(R.array.features);

        Logger.i("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");

        Logger.i("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");

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

        Intent intent = new Intent(this, SettingsActivity.class);
        mainOptionsBottomSheetDialogFragment.dismiss();
        startActivity(intent);
    }
    public void aboutButton(View view) {
        Intent intent = new Intent(this, ExampleMaterialAboutActivity.class);
        mainOptionsBottomSheetDialogFragment.dismiss();
        intent.putExtra(ExampleMaterialAboutActivity.THEME_EXTRA, 2);
        Toasty.info(this, "SHIT", Toast.LENGTH_LONG, true).show();
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
}
