package com.studio.timeclock3;

import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class TimeCalculations {


    private final SharedPreferences sharedPreferences;
    private final SimpleDateFormat sdf_mm;
    private SimpleDateFormat sdf_HHmm;

    private long pauseTime;
    private long workingTime;
    private long timeClockIn;
    private long timeClockOut;
    private long workingPeriodMinutes;
    private long workingTimeEstimate;

    public TimeCalculations(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
        sdf_mm = new SimpleDateFormat("mm");

        workingTime = sharedPreferences.getLong("WORKING_TIME_MIN", 480);   //8:00
        pauseTime = sharedPreferences.getLong("PAUSE_TIME_MIN", 45);        //0:45
        timeClockIn = sharedPreferences.getLong("timeClockIn", 480);        //8:00
        timeClockOut = sharedPreferences.getLong("timeClockOut", 960);      //16:00
    }

    public String convertMinutesToDateString(long min)  {

        Date dt = null;
        try {
            dt = sdf_mm.parse(String.valueOf(min));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf_HHmm = new SimpleDateFormat("HH:mm");
        String dateString = sdf_HHmm.format(dt);

        return dateString;
    }

    public long convertDateStringToMinutes(String dateString){
        Date dt = null;
        try {
            dt = sdf_HHmm.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar aCalander = Calendar.getInstance();
        aCalander.setTime(dt);
        int hour = aCalander.get(Calendar.HOUR);
        int minute = aCalander.get(Calendar.MINUTE);
        long minutes = hour * 60 +minute;
        return minutes;
    }

    public void calculateWorkingPeriodAsMinutes(){
        workingPeriodMinutes = timeClockOut - timeClockIn;
    }

    public long getWorkingPeriodAsMinutes(boolean withPause){
        long local_workingPeriodMinutes = workingPeriodMinutes;
        if(withPause){
            local_workingPeriodMinutes += pauseTime;
        }
        return local_workingPeriodMinutes;
    }

    public String getWorkingPeriodAsDateString(boolean withPause){
        long local_workingPeriodMinutes = workingPeriodMinutes;
        if(withPause){
            local_workingPeriodMinutes += pauseTime;
        }
        return convertMinutesToDateString(local_workingPeriodMinutes);
    }

    public void calculateWorkingTimeEstimateAsMinutes(){
        workingTimeEstimate = getEndTimeEstimateAsMinutes(false) - timeClockIn;
    }

    public long getWorkingTimeEstimateAsMinutes(boolean withPause){
        long local_workingTimeEstimate = workingTimeEstimate;
        if(withPause){
            local_workingTimeEstimate += pauseTime;
        }
        return local_workingTimeEstimate;
    }

    public String getWorkingTimeEstimateAsDateString(boolean withPause){
        long local_workingTimeEstimate = workingTimeEstimate;
        if(withPause){
            local_workingTimeEstimate += pauseTime;
        }
        return convertMinutesToDateString(local_workingTimeEstimate);
    }

    public String getCurrentTimeDate(){
        return sdf_HHmm.format(new Date());
    }

    public int getCurrentTimeMinutes(){
        Calendar calendar = Calendar.getInstance();
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMin = calendar.get(Calendar.MINUTE);
        return startHour * 60 + startMin;
    }

    public void setStartTime() {

        timeClockIn = getCurrentTimeMinutes();
        sharedPreferences.edit().putLong("timeClockIn", timeClockIn).apply();
    }

    public void setEndTime() {
        timeClockOut = getCurrentTimeMinutes();
        sharedPreferences.edit().putLong("timeClockOut", timeClockOut).apply();
    }

    public long getEndTimeAsMinutes(){
        return timeClockOut;
    }

    public String getEndTimeAsDate(){
        return convertMinutesToDateString(timeClockOut);
    }

    public long getStartTimeAsMinutes() {
        return timeClockIn;
    }

    public String getStartTimeAsDate() {
        return convertMinutesToDateString(timeClockIn);
    }

    public long getEndTimeEstimateAsMinutes(boolean withPause) {
        long timeClockOut = timeClockIn + workingTime;

        if (withPause) {
            timeClockOut += pauseTime;
        }

        return timeClockOut;
    }

    public String getEndTimeEstimateAsDate(boolean withPause) {
        long timeClockOut = timeClockIn + workingTime;

        if (withPause) {
            timeClockOut += pauseTime;
        }

        return convertMinutesToDateString(timeClockOut);
    }

    public long getPauseTimeAsMinutes(){
        return pauseTime;
    }

    public String getPauseTimeAsDateString(){
        return convertMinutesToDateString(pauseTime);
    }
}
