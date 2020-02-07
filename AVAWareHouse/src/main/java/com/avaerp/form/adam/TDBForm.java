package com.avaerp.form.adam;

import android.util.Log;

import com.avaerp.apps.warehouse.R;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.sql.CallableStatement;
import java.sql.ResultSet;

public abstract class TDBForm extends TStoredForm {
    public int getUserID() {
        return getApp().getLoginInfo().getUserID();
    }

    protected void execSql(int aSqlId, TParams aParams, TSqlExecutor.TSqlMode aSqlMode) {
        if (beforeSqlExecute(aSqlId, aParams, aSqlMode)) {
            TSqlExecutor sqlExecutor = new TSqlExecutor(this, aSqlId, aSqlMode, aParams);
            sqlExecutor.execute(null, null, null);
        }
    }

    protected void executeSql(int aSqlId, TParams aParams) {
        execSql(aSqlId, aParams, TSqlExecutor.TSqlMode.EXECUTE);
    }

    protected void executeSql(int aSqlId) {
        executeSql(aSqlId, getParams());
    }

    protected void querySql(int aSqlId, TParams aParams) {
        execSql(aSqlId, aParams, TSqlExecutor.TSqlMode.QUERY);
    }

    protected void querySql(int aSqlId) {
        querySql(aSqlId, getParams());
    }

    public boolean beforeSqlExecute(int aSqlId, TParams aParams, TSqlExecutor.TSqlMode aSqlMode) {
        return true;
    }

    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        switch (aStatus) {
            case SUCCESSFUL: {
                Log.d("@@@@", "SUCCESS");
                break;
            }
            case NO_INTERNET: {
                sayLong(R.string.error_no_internet);
                break;
            }
            case NOT_CONNECTED: {
                sayLong(R.string.error_not_connected);
                break;
            }
            default: {
                sayLong(R.string.error_sql_syntax);
            }
        }
    }

    public void afterSqlQuery(int aSqlId, TParams aParams, ResultSet aResultSet) {
    }
}
