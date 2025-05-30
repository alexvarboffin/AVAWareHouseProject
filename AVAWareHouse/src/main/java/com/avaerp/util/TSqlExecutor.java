package com.avaerp.util;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.avaerp.form.adam.TDBForm;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleConnection;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class TSqlExecutor extends AsyncTask<Void, Void, Void> {

    public enum TSqlMode {QUERY, EXECUTE}

    public enum TExecuteStatus {
        SUCCESSFUL, NOT_CONNECTED, NO_INTERNET, PREPARE_ERROR, BIND_IN_ERROR,
        EXECUTING_ERROR, BIND_OUT_ERROR, OTHER_ERROR
    }

    private TDBForm mForm;
    private int mSqlId;
    private String mSql;
    private TParams mParams;
    private TExecuteStatus mStatus = TExecuteStatus.NOT_CONNECTED;
    private TSqlMode mMode;

    public TSqlExecutor(TDBForm aForm, int aSqlId, TSqlMode aMode, TParams aParams) {
        super();
        mForm = aForm;
        mSqlId = aSqlId;
        mSql = Util.getString(mForm, aSqlId);
        mParams = aParams;
        mMode = aMode;
    }

    private boolean checkInternet() {
        mForm.v("Checking internet");
        ConnectivityManager manager = (ConnectivityManager) mForm.getSystemService(CONNECTIVITY_SERVICE);
        mForm.v("Connectivity manager retrieved");
        /** Required permission ACCESS_NETWORK_STATE */
        if ((manager != null ? manager.getActiveNetworkInfo() : null) == null) {
            mForm.e("No internet connection");
            return false;
        } else {
            mForm.d("There's internet connection");
            return true;
        }
    }

    private OracleConnection getConnection() {
        if (checkInternet()) {
            mForm.getApp().initialize(mForm);
            TLoginInfo loginInfo = mForm.getApp().getLoginInfo();
            try {
                mForm.v("Connecting to database...");
                mForm.v("  Login: %s", loginInfo.getUserName());
                mForm.v("  Password: %s", loginInfo.getPassword());
                mForm.v("  Url: %s", loginInfo.getUrl());
                OracleConnection result = null;

                Log.d("@@@", "getConnection: " + loginInfo.getUrl());

                result = (OracleConnection) DriverManager.getConnection(
                        loginInfo.getUrl(), loginInfo.getUserName(), loginInfo.getPassword());
                mForm.v("Connection opened");
                return result;
            } catch (SQLException e) {
                mForm.e("Can't login to database", e);
                return null;
            } catch (NoClassDefFoundError error) {
                Log.d("@@@", "getConnection: " + loginInfo.getUrl() + error.getMessage());
                return null;
            }
        } else {
            mStatus = TExecuteStatus.NO_INTERNET;
            return null;
        }
    }

    @Override
    protected Void doInBackground(Void... aParams) {
        OracleConnection conn = getConnection();
        if (conn != null) {
            try {
                try {
                    mForm.v("Preparing statement %s", mSql);
                    mStatus = TExecuteStatus.PREPARE_ERROR;
                    CallableStatement stmt = conn.prepareCall(mSql);
                    mForm.v("Parsing sql text");
                    mStatus = TExecuteStatus.OTHER_ERROR;
                    TSqlParser parser = new TSqlParser();
                    parser.parse(mSql);
                    mForm.v("Setting params to statement");
                    mStatus = TExecuteStatus.BIND_IN_ERROR;
                    parser.setParams(mForm, stmt, mParams);
                    mForm.v("Executing statement");
                    mStatus = TExecuteStatus.EXECUTING_ERROR;
                    switch (mMode) {
                        case EXECUTE: {
                            stmt.execute();
                            break;
                        }
                        case QUERY: {
                            ResultSet resultSet = stmt.executeQuery();
                            mForm.afterSqlQuery(mSqlId, mParams, resultSet);
                            break;
                        }
                    }
                    mForm.v("Getting params from statement");
                    mStatus = TExecuteStatus.BIND_OUT_ERROR;
                    parser.getParams(mForm, stmt, mParams);
                    mForm.v("Closing statement");
                    mStatus = TExecuteStatus.OTHER_ERROR;
                    stmt.close();
                    mStatus = TExecuteStatus.SUCCESSFUL;
                } catch (SQLException e) {
                    mForm.e("Preparing statement failed", e);
                } catch (Exception e) {
                    mForm.e("Unknown error", e);
                }
            } finally {
                try {
                    mForm.v("Closing connection");
                    conn.close();
                    mForm.v("Connection closed");
                } catch (SQLException e) {
                    mForm.e("Closing connection failed", e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Void aResult) {
        try {
            mForm.onSqlExecuted(mSqlId, mParams, mStatus);
        } finally {
            mStatus = TExecuteStatus.NOT_CONNECTED;
        }
    }
}
