package com.avaerp.form.adam;

import android.util.Log;

import com.avaerp.apps.warehouse.R;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.sql.ResultSet;
import java.util.Objects;

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
        executeSql(aSqlId, getParams0());
    }

    protected void querySql(int aSqlId, TParams aParams) {
        execSql(aSqlId, aParams, TSqlExecutor.TSqlMode.QUERY);
    }

    protected void querySql(int aSqlId) {
        querySql(aSqlId, getParams0());
    }

    public boolean beforeSqlExecute(int aSqlId, TParams aParams, TSqlExecutor.TSqlMode aSqlMode) {
        return true;
    }

    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        if (Objects.requireNonNull(aStatus) == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
            Log.d("@@@@", "SUCCESS");
        } else if (aStatus == TSqlExecutor.TExecuteStatus.NO_INTERNET) {
            sayLong(R.string.error_no_internet);
        } else if (aStatus == TSqlExecutor.TExecuteStatus.NOT_CONNECTED) {
            sayLong(R.string.error_not_connected);
        } else {
            sayLong(R.string.error_sql_syntax);
        }
    }

    public void afterSqlQuery(int aSqlId, TParams aParams, ResultSet aResultSet) {
    }
}
