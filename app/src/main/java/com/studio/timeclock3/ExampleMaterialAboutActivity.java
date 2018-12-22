package com.studio.timeclock3;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.michaelflisar.changelog.ChangelogBuilder;

import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import static android.widget.Toast.*;

public class ExampleMaterialAboutActivity extends MaterialAboutActivity {

    public static final String THEME_EXTRA = "";
    public static final int THEME_LIGHT_LIGHTBAR = 0;
    public static final int THEME_LIGHT_DARKBAR = 1;
    public static final int THEME_DARK_LIGHTBAR = 2;
    public static final int THEME_DARK_DARKBAR = 3;
    public static final int THEME_CUSTOM_CARDVIEW = 4;


    MaterialAboutCard.Builder cardBuilder = new MaterialAboutCard.Builder();

    @Override
    @NonNull
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {


        MaterialAboutCard.Builder appCardBuilder = initializeAppCardBuilder();
        MaterialAboutCard.Builder aboutCardBuilder = initializeAboutCardBuilder();
        MaterialAboutCard.Builder developerCardBuilder = initializeDeveloperCardBuilder();

        return new MaterialAboutList(appCardBuilder.build(), aboutCardBuilder.build(), developerCardBuilder.build());

    }

    private MaterialAboutCard.Builder initializeDeveloperCardBuilder() {
        MaterialAboutCard.Builder developerCardBuilder = new MaterialAboutCard.Builder();
        developerCardBuilder.title("Developer");

        developerCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Nikita Tarasov")
                .subText("Main Developer")
                .icon(R.drawable.account_outline)
                .build());

        developerCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Noah Röschard")
                .subText("Honorary Mention")
                .icon(R.drawable.account_outline)
                .build());


        developerCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Contact")
                .icon(R.drawable.ic_mail_outline_black_24dp)
                .subText("idiotenpost@gmail.com")
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toasty.info(getApplicationContext(), "Feature will be added later", LENGTH_SHORT, true).show();
                    }
                })
                .build());

        return developerCardBuilder;
    }

    private MaterialAboutCard.Builder initializeAboutCardBuilder() {
        MaterialAboutCard.Builder aboutCardBuilder = new MaterialAboutCard.Builder();
        aboutCardBuilder.title("About");

        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(R.drawable.ic_history_black_24dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toasty.warning(getApplicationContext(), "Working on it right now!", Toast.LENGTH_LONG, true).show();

                    }
                })
                .build());

        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("GitHub")
                .icon(R.drawable.github_circle)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toasty.info(getApplicationContext(), "Feature will be added later", LENGTH_SHORT, true).show();
                    }
                })
                .build());

        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Support")
                .icon(R.drawable.github_circle)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toasty.info(getApplicationContext(), "Feature will be added later", LENGTH_SHORT, true).show();
                    }
                })
                .build());

        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Feedback")
                .icon(R.drawable.message_alert)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toasty.info(getApplicationContext(), "Feature will be added later", LENGTH_SHORT, true).show();
                    }
                })
                .build());


        return aboutCardBuilder;
    }

    private MaterialAboutCard.Builder initializeAppCardBuilder() {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.title("App");


        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Version")
                .icon(R.drawable.android_head)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toasty.info(getApplicationContext(), "Time Clock", LENGTH_LONG, true).show();
                    }
                })
                .subText(BuildConfig.VERSION_NAME)
                .build());


        try {
            appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                    .text("MinSDKVersion")
                    .subText(String.valueOf(getPackageManager().getApplicationInfo(BuildConfig.APPLICATION_ID, 0).minSdkVersion))
                    .icon(R.drawable.code_less_than)
                    .build());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                    .text("TargetSDKVersion")
                    .subText(String.valueOf(getPackageManager().getApplicationInfo(BuildConfig.APPLICATION_ID, 0).targetSdkVersion))
                    .icon(R.drawable.code_greater_than_or_equal)
                    .build());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Application ID")
                .icon(R.drawable.code_braces)
                .subText(BuildConfig.APPLICATION_ID)
                .build());
        return appCardBuilder;
    }


    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.title_about);
    }
}
