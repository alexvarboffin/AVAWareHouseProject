package com.avaerp.form;

import android.os.Bundle;

import com.avaerp.apps.warehouse.R;
import com.avaerp.util.TDataAdapter;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;
import com.avaerp.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Будда on 03.07.2014.
 */
public class TfmWhTakeOff extends TfmWHQueue {
    private TDataAdapter mAdapter;
    @Override
    public void onCreate(Bundle aState) {
        mLayoutId_Row = R.layout.tfm_wh_queue_row_take_off;
        super.onCreate(aState);
        //getTextView(mControlId_Value).setHint(R.string.TfmWhQueue_Hint_SerialsNum);
        mFieldMap.qp("vcSerialsNum").setValue(R.id.TfmWHQueue_TakeOff_SerialsNum);
        mFieldMap.qp("vcArticul").setValue(R.id.TfmWHQueue_TakeOff_Articul);
        mFieldMap.qp("vcFullName").setValue(R.id.TfmWHQueue_TakeOff_FullName);
        mAdapter = new TDataAdapter(this, 0/*R.id.TfmWHQueue_TakeOff_Articul*/, mDataSet,
                R.layout.tfm_wh_queue_row_take_off, mFieldMap);
        getListView(R.id.TfmWHQueue_gvData).setAdapter(mAdapter);
        ///
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("vcArticul", "ARTICUL");
        mDataSet.add(temp);
        //mapItems();
    }
    @Override
    protected void onScan(String aValue) {
        super.onScan(aValue);
        executeSql(R.string.sql_take_off_on_scan_serials);
    }
    @Override
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        switch (aSqlId) {
            case R.string.sql_take_off_on_scan_serials: {
                if (aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
                    HashMap<String, Object> row = new HashMap<String, Object>();
//                    row.put("iStatus", notFound()? -1: 1);
//                    row.put("vcSerialsNum", notFound()? MSG_NotFound(): qp("vcSerialsNum").getString());
//                    row.put("vcArticul", notFound()? MSG_NotFound(): qp("vcArticul").getString());
//                    row.put("vcFullName", notFound()? MSG_NotFound(): qp("vcFullName").getString());
                    mDataSet.add(row);
                }
            }
        }
    }
    @Override
    public void afterSqlQuery(int aSqlId, TParams aParams, ResultSet aResultSet) {
        switch (aSqlId) {
            case R.string.sql_serials_info: {
                HashMap<String, Object> row = new HashMap<String, Object>();
                row.put("vcSerialsNum", qp("vcScanValue"));
                try {
                    row.put("vcArticul", aResultSet.getString("vcArticul"));
                    row.put("vcFullName", aResultSet.getString("vcFullName"));
                } catch (SQLException e) {
                    row.put("vcArticul", R.string.msg_not_found);
                    row.put("vcFullName", R.string.msg_not_found);
                }
                mDataSet.add(row);
            }
        }
    }

}
