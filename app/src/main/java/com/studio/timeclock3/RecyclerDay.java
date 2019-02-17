package com.studio.timeclock3;

public class RecyclerDay {

    private String dateStringRecycler;
    private  Integer dateNumberRecycler;
    private  String startTimeRecycler;
    private String endTimeRecycler;
    private Double balanceTimeRecycler;

    public RecyclerDay(String dateStringRecycler, Integer dateNumberRecycler, String startTimeRecycler, String endTimeRecycler, Double balanceTimeRecycler){
        this.dateStringRecycler = dateStringRecycler;
        this.dateNumberRecycler = dateNumberRecycler;
        this.startTimeRecycler = startTimeRecycler;
        this.endTimeRecycler = endTimeRecycler;
        this.balanceTimeRecycler = balanceTimeRecycler;
    }

    public String getDateStringRecycler(){
        return  dateStringRecycler;
    }

    public void setDateStringRecycler(String dateStringRecycler){
        this.dateStringRecycler = dateStringRecycler;
    }

    public Integer getDateNumberRecycler(){
        return  dateNumberRecycler;
    }

    public void setDateNumberRecycler(Integer dateNumberRecycler){
        this.dateNumberRecycler = dateNumberRecycler;
    }

    public String getStartTimeRecycler(){
        return  startTimeRecycler;
    }

    public void setStartTimeRecycler(String startTimeRecycler){
        this.startTimeRecycler = startTimeRecycler;
    }

    public String getEndTimeRecycler(){
        return  endTimeRecycler;
    }

    public void setEndTimeRecycler(String endTimeRecycler){
        this.endTimeRecycler = endTimeRecycler;
    }

    public Double getBalanceTimeRecycler(){
        return  balanceTimeRecycler;
    }

    public void setBalanceTimeRecycler(Double balanceTimeRecycler){
        this.balanceTimeRecycler = balanceTimeRecycler;
    }
}
