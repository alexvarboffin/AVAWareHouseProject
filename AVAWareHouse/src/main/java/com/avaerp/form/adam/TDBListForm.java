package com.avaerp.form.adam;

import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.avaerp.apps.warehouse.R;
import com.avaerp.util.TDataAdapter;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TDBListForm extends TDBForm {
    protected ArrayList<HashMap<String, Object>> mDataSet = new ArrayList<HashMap<String, Object>>();
    protected TDataAdapter mAdapter;
    protected int mLayoutId_Form;
    protected int mLayoutId_Row;
    protected int mSqlId_Form = 0;
    protected int mControlId_DataSet;
    protected int mDelBtnId;
    protected TParams mFieldMap = new TParams();
    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(mLayoutId_Form);
        mAdapter = new TDataAdapter(this, 0, mDataSet, mLayoutId_Row, mFieldMap);
        mAdapter.mDelBtnId = mDelBtnId;
        getListView(mControlId_DataSet).setAdapter(mAdapter);
        if (mSqlId_Form != 0) querySql(mSqlId_Form);
    }
    protected boolean addRow(int aSqlId, HashMap<String, Object> aRow, ResultSet aResultSet) {
        return false;
    }
    @Override
    public void afterSqlQuery(int aSqlId, TParams aParams, ResultSet aResultSet) {
        if (aSqlId == mSqlId_Form) {
            mDataSet.clear();
            super.afterSqlQuery(aSqlId, aParams, aResultSet);
            try {
                while (aResultSet.next()) {
                    HashMap<String, Object> row = new HashMap<String, Object>();
                    if (addRow(aSqlId, row, aResultSet)) {
                        mDataSet.add(row);
                    }
                }
            } catch (SQLException e) {
                sayLong(R.string.error_fetching_row);
            }
        }
    }
    @Override
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        super.onSqlExecuted(aSqlId, aParams, aStatus);
        if (aSqlId == mSqlId_Form) {
            if (aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    private Object fb(String aFieldName, int aRowId) {
        return mDataSet.get(aRowId).get(aFieldName);
    }
    private Object fb(String aFieldName, long aRowId) {
        return fb(aFieldName, (int) aRowId);
    }
    public Long fbLong(String aFieldName, long aRowId) {
        return (Long) fb(aFieldName, aRowId);
    }

}
