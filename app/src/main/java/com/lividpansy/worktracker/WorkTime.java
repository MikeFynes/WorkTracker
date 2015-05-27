package com.lividpansy.worktracker;

/**
 * Created by mike on 14/02/2015.
 */
public class WorkTime {

    private int  employee, trackingType;

    // DATES STORED AS VALUES IN MILLISECONDS AND PARSED TO DATES WHEREVER NEEDED
    private long startTime, endTime;
    private long id;

    public WorkTime(int employee, int trackingType, long startTime ){
        setEmployee(employee);
        setTrackingType(trackingType);

        setStartTime(startTime);
        setEndTime(0);
    }

    public WorkTime(long id, int employee, int trackingType, long startTime, long endTime ){
        setId(id);
        setEmployee(employee);
        setTrackingType(trackingType);

        setStartTime(startTime);
        setEndTime(endTime);
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getTrackingType() {
        return trackingType;
    }

    public void setTrackingType(int trackingType) {
        this.trackingType = trackingType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
