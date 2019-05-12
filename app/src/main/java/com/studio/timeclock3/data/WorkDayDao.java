package com.studio.timeclock3.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WorkDayDao {

    @Query("SELECT * FROM workDay")
    List<WorkDay> getAll();

    @Query("SELECT * FROM workDay where year = :year AND month = :month AND dayOfMonth = :dayOfMonth")
    WorkDay findByDate(int year, int month, int dayOfMonth);

    @Query("SELECT * FROM workDay where year = :year AND month = :month")
    WorkDay findbyMonth(int year, int month);

    @Insert
    void insertAll(WorkDay... workDays);

    @Insert
    Long insertWorkDay(WorkDay workDay);

    @Update
    void updateWorkDay(WorkDay workDay);

    @Query("DELETE FROM workDay")
    void deleteAll();

    @Delete
    void deleteWorkDay(WorkDay workDay);
}
