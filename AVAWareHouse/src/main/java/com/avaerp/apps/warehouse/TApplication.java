package com.avaerp.apps.warehouse;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.avaerp.form.adam.TLogForm;
import com.avaerp.util.TLoginInfo;
import com.avaerp.util.TParams;

import java.sql.DriverManager;
import java.sql.SQLException;


public class TApplication extends Application {
    private static final String TAG = "@@@";
    private final TParams mParams = new TParams();
    private final TLoginInfo mLoginInfo = new TLoginInfo();

    public TLoginInfo getLoginInfo() {
        return mLoginInfo;
    }

    private boolean mIsDriverOk = false;
    private Thread.UncaughtExceptionHandler mDefExceptionHandler;
    private final Thread.UncaughtExceptionHandler mExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread aThread, Throwable aException) {
            Log.e(TAG, "Unhandled exception: ", aException);
            //mDefExceptionHandler.uncaughtException(aThread, aException);
        }
    };

    public boolean initialize(TLogForm aLogForm) {
        DriverManager c;

        mDefExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mExceptionHandler);
        if (!mIsDriverOk) {
            try {


                try {
                    Class.forName("oracle.jdbc.OracleDriver");//Old version jdbc14
                } catch (java.lang.NoClassDefFoundError e) {
                    Log.d(TAG, "initialize: " + e.getLocalizedMessage());
                }
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                } catch (java.lang.NoClassDefFoundError e) {
                    Log.d(TAG, "initialize: " + e.getLocalizedMessage());
                }


                //Class.forName("oracle.jdbc.OracleDriver");//Old version jdbc14


                try {
                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                } catch (java.lang.NoClassDefFoundError e) {
                    Log.d(TAG, "initialize: " + e.getLocalizedMessage());
                }
                try {
                    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                } catch (java.lang.NoClassDefFoundError e) {
                    Log.d(TAG, "initialize: " + e.getLocalizedMessage());
                }

                mIsDriverOk = true;
            }
//            catch (SQLException e) {
//                aLogForm.e("Can't register oracle driver", e);
//            }
//            catch (ClassNotFoundException e) {
//                aLogForm.e("Oracle driver not found", e);
//            }
            catch (Exception e) {
                Log.d(TAG, "initialize: " + e.getLocalizedMessage());
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
