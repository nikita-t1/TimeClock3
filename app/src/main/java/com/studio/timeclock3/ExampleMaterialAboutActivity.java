package com.studio.timeclock3;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher;
import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogSetup;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import static android.widget.Toast.*;
import static com.studio.timeclock3.R.mipmap.ic_launcher;

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

                        ChangelogDialogFragment builder = new ChangelogBuilder()
                                .withUseBulletList(true)
                                .buildAndShowDialog(ExampleMaterialAboutActivity.this, false); // second parameter defines, if the dialog has a dark or light theme
                                            }
                })
                .build());

        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("GitHub")
                .icon(R.drawable.github_circle)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://github.com/nikita-t1/TimeClock3"));
                        PendingIntent pi = PendingIntent.getActivity(ExampleMaterialAboutActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
                        try {
                            pi.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build());

        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Future")
                .icon(R.drawable.ic_future_black_24dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        String[] some_array = getResources().getStringArray(R.array.features);

                        com.orhanobut.logger.Logger.i("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");

                        com.orhanobut.logger.Logger.i("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");

                        String str = String.join(",", some_array);
                        str=str.replaceAll(",", "\n\n");

                        new MaterialStyledDialog.Builder(ExampleMaterialAboutActivity.this)
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
                })
                .build());


        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Support Us")
                .icon(R.drawable.ic_attach_money_black_24dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toasty.info(getApplicationContext(), "Feature will be added later", LENGTH_SHORT, true).show();
                    }
                })
                .build());

        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Report Issue")
                .icon(R.drawable.ic_bug_report_black_24dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        IssueReporterLauncher.forTarget("nikita-t1", "android-issue-reporter")
                                // [Recommended] Theme to use for the reporter.
                                // (See #theming for further information.)
                                .theme(R.style.Theme_App_Light)
                                // [Optional] Auth token to open issues if users don't have a GitHub account
                                // You can register a bot account on GitHub and copy ist OAuth2 token here.
                                // (See #how-to-create-a-bot-key for further information.)
                                .guestToken("c2c21657f91989818e3589c041b6cad7a171659f")
                                // [Optional] Force users to enter an email adress when the report is sent using
                                // the guest token.
                                .guestEmailRequired(true)
                                // [Optional] Set a minimum character limit for the description to filter out
                                // empty reports.
                                .minDescriptionLength(20)
                                // [Optional] Include other relevant info in the bug report (like custom variables)
                                .putExtraInfo("Test 1", "Example string")
                                .putExtraInfo("Test 2", true)
                                // [Optional] Disable back arrow in toolbar
                                .homeAsUpEnabled(true)
                                .launch(ExampleMaterialAboutActivity.this);
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


        aboutCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Open Source Libraries")
                .icon(R.drawable.open_source_initiative)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                                 new LibsBuilder()
                                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                .withAboutAppName(ExampleMaterialAboutActivity.this.getString(R.string.app_name) )
                                .withAboutIconShown(true)
                                .withAboutVersionShown(true)
                                .withAutoDetect(true)
                                .start(ExampleMaterialAboutActivity.this);
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
