package com.avaerp.apps.warehouse;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.avaerp.form.adam.TLogForm;
import com.avaerp.util.TLoginInfo;
import com.avaerp.util.TParams;

import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleDriver;

public class TApplication extends Application {
    private static final String TAG = "TApplication";
    private TParams mParams = new TParams();
    private TLoginInfo mLoginInfo = new TLoginInfo();
    public TLoginInfo getLoginInfo() {return mLoginInfo;}
    private boolean mIsDriverOk = false;
    private Thread.UncaughtExceptionHandler mDefExceptionHandler;
    private Thread.UncaughtExceptionHandler mExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread aThread, Throwable aException) {
            Log.e(TAG, "Unhandled exception: ", aException);
            //mDefExceptionHandler.uncaughtException(aThread, aException);
        }
    };
    public boolean initialize(TLogForm aLogForm) {
        mDefExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mExceptionHandler);
        if (!mIsDriverOk) {
            try {
                DriverManager.registerDriver(new OracleDriver());
                Class.forName("oracle.jdbc.OracleDriver");
                mIsDriverOk = true;
            } catch (SQLException e) {
                aLogForm.e("Can't register oracle driver", e);
            } catch (ClassNotFoundException e) {
                aLogForm.e("Oracle driver not found", e);
            }
        }
        return mIsDriverOk;
    }
    public String getVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Can't detect version number", e);
            return "0.0.0.0";
        }

    }
    public TParams.TParam qp(String aParamName) {
        return mParams.qp(aParamName);
    }
}
