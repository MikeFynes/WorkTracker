package com.lividpansy.worktracker;

import java.util.ArrayList;

/**
 * Created by mike on 14/02/2015.
 */
public interface AsyncListener {
    public void workTimeId(long result);

    public void checkOutSuccess();

    public void loginSuccess(int result, String wifiName);

    public void loginFail();

    public void sendList(ArrayList<WorkTime> result);

    public void listIsEmpty();
}
