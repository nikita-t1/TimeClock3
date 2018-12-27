package com.studio.timeclock3;

import android.content.Context;

import com.michaelflisar.changelog.tags.IChangelogTag;

public class ChangelogTag {
    // Required empty public constructor
}

class ChangelogTagChange implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "change";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#ffc107\"><b>Change: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagExperiment implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "exp";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#9c27b0\"><b>Experimental: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagNew implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "new";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#4caf50\"><b>New: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagBug implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "bug";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#f44336\"><b>Bug: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagFix implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "fix";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#3f51b5\"><b>Fix: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagUpdate implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "update";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#4caf50\"><b>Update: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagInfo implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "info";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#000000\"><b>Info: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagRemove implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "remove";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#ffc107\"><b>Remove: </b></font>";
        return prefix + changeText;
    }
}

class ChangelogTagFuture implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "future";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#03a9f4\"><b>Future: </b></font>";
        return prefix + changeText;
    }
}
class ChangelogTagRefactor implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "refactor";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#9c27b0\"><b>Refactor: </b></font>";
        return prefix + changeText;
    }
}
