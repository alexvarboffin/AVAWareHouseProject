package com.avaerp.util;

import android.util.Log;

public class TLoginInfo {
    private String mUserName;
    private String mPassword;
    private String mUrl;
    private int mStaffId;
    public void setValues(String aStaffId, String aHostName, String aPort, String aSid) {
        mStaffId = Integer.parseInt(aStaffId);
        setValues(mStaffId, aHostName, Integer.parseInt(aPort), aSid);
    }
    public void setValues(int aStaffId, String aHostName, int aPort, String aSid) {
        if (aStaffId == 0) {
            mUserName = "REGUSER";
        } else {
            mUserName = String.format("OS_%03d", aStaffId);
        }
        mPassword = new Hash().encode(mUserName);
        mUrl = String.format("jdbc:oracle:thin:@%s:%d:%s", aHostName, aPort, aSid);

        Log.d("@@@", "setValues: "+mUrl);
    }
    public String getUserName() {return mUserName;}
    public String getPassword() {return mPassword;}
    public String getUrl() {return mUrl;}
    public int getUserID() {return mStaffId;}
}
