package com.studio.timeclock3.Data;

import java.util.ArrayList;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workDay")
public class WorkDay {

    @PrimaryKey(autoGenerate = true)
    private int WorkDayId;

    @ColumnInfo(name = "year")
    private Integer year;

    @ColumnInfo(name = "weekOfYear")
    private Integer weekOfYear;

    @ColumnInfo(name = "dayOfWeek")
    private Integer dayOfWeek;

    @ColumnInfo(name = "dayOfMonth")
    private Integer dayOfMonth;

    @ColumnInfo(name = "month")
    private Integer month;

    @ColumnInfo(name = "timeClockIn")
    private String timeClockIn;

    @ColumnInfo(name = "timeClockOut")
    private String timeClockOut;

    @ColumnInfo(name = "timeBreak")
    private String timeBreak;

    @ColumnInfo(name = "workTimeGross")
    private String workTimeGross;

    @ColumnInfo(name = "workTimeNet")
    private String workTimeNet;

    @ColumnInfo(name = "overtime")
    private String overtime;

    @ColumnInfo(name = "wasPresent")
    private Boolean wasPresent;

    @ColumnInfo(name = "userNote")
    private String userNote;

    @ColumnInfo(name = "furtherAddition")
    private String furtherAddition;


    public int getWorkDayId() {
        return WorkDayId;
    }

    public void setWorkDayId(int WorkDayId) {
        this.WorkDayId = WorkDayId;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getTimeClockIn() {
        return timeClockIn;
    }

    public void setTimeClockIn(String timeClockIn) {
        this.timeClockIn = timeClockIn;
    }

    public String getTimeClockOut() {
        return timeClockOut;
    }

    public void setTimeClockOut(String timeClockOut) {
        this.timeClockOut = timeClockOut;
    }

    public String getTimeBreak() {
        return timeBreak;
    }

    public void setTimeBreak(String timeBreak) {
        this.timeBreak = timeBreak;
    }

    public String getWorkTimeGross() {
        return workTimeGross;
    }

    public void setWorkTimeGross(String workTimeGross) {
        this.workTimeGross = workTimeGross;
    }

    public String getWorkTimeNet() {
        return workTimeNet;
    }

    public void setWorkTimeNet(String workTimeNet) {
        this.workTimeNet = workTimeNet;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public Boolean getWasPresent() {
        return wasPresent;
    }

    public void setWasPresent(Boolean wasPresent) {
        this.wasPresent = wasPresent;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public String getFurtherAddition() {
        return furtherAddition;
    }

    public void setFurtherAddition(String furtherAddition) {
        this.furtherAddition = furtherAddition;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
