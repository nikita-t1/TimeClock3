package com.studio.timeclock3.Data;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }


        private static WorkDay addWorkDay(final AppDatabase db, WorkDay workDay) {
        db.workDayDao().insertAll(workDay);
        return workDay;
    }

    private static void populateWithTestData(AppDatabase db) {
        WorkDay workDay = new WorkDay();
        workDay.setYear(2019);
        workDay.setWeekOfYear(15);
        workDay.setDayOfWeek(5);
        workDay.setDayOfMonth(11);
        workDay.setMonth(4);
        workDay.setTimeClockIn("06:43");
        workDay.setTimeClockOut("15:36");
        workDay.setTimeBreak("00:45");
        workDay.setWorkTimeGross("08:53");
        workDay.setWorkTimeNet("08:08");
        workDay.setOvertime("00:32");
        workDay.setWasPresent(true);
        workDay.setUserNote("Alles Gute...");
        addWorkDay(db, workDay);

        List<WorkDay> userList = db.workDayDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + userList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
