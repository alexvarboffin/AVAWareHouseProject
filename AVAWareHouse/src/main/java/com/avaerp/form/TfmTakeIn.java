package com.avaerp.form;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBListForm;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Будда on 24.09.2014.
 */
public class TfmTakeIn extends TDBListForm {
    EditText mScanControl = null;
    TextView mSerialsControl = null;
    TextView mWhZoneControl = null;
    public void clear() {
        mScanControl.setText(null);
        mWhZoneControl.setText(null);
        mSerialsControl.setText(null);
    }
    public void tryApply() {
        String mSerials = mSerialsControl.getText().toString();
        String mWhZone = mWhZoneControl.getText().toString();
        if (mWhZone.length() > 0 && mSerials.length() > 0) {
            qp("vcWhZone").setValue(mWhZone);
            qp("vcSerialsNum").setValue(mSerials);
            executeSql(R.string.sql_put_scan);
        }
    }
    @Override
    public void onCreate(Bundle aState) {
        mLayoutId_Form = R.layout.tfm_take_in;
        mLayoutId_Row = R.layout.tfm_take_in_row;
        mControlId_DataSet = R.id.TfmTakeIn_gvData;
        mDelBtnId = R.id.TfmTakeIn_Row_btDelRow;
        mSqlId_Form = R.string.sql_wh_queue;
        mFieldMap.qp("vcSerialsNum").setValue(R.id.TfmTakeIn_Row_SerialsNum);
        mFieldMap.qp("vcWhZone").setValue(R.id.TfmTakeIn_Row_WhZone);
        mFieldMap.qp("vcErrors").setValue(R.id.TfmTakeIn_Row_Result);
        super.onCreate(aState);
        mScanControl = getEditText(R.id.TfmTakeIn_edScan);
        mSerialsControl = getTextView(R.id.TfmTakeIn_edSerials);
        mWhZoneControl = getTextView(R.id.TfmTakeIn_edWhZone);
        clear();
    }

    @Override
    public boolean beforeSqlExecute(int aSqlId, TParams aParams, TSqlExecutor.TSqlMode aSqlMode) {
        aParams.qp("iWorkMode").setValue(qpGlobal("WorkMode").getInteger());
        aParams.qp("iStaffId").setValue(getUserID());
        aParams.qp("iDocumentId").setValue(0);
        return true;
    }

    @Override
    protected boolean onKeyDown(View aView, int aKeyCode) {
        boolean result = super.onKeyDown(aView, aKeyCode);
        if (aView == mScanControl && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            String value = mScanControl.getText().toString();
            if (!value.isEmpty()) {
                if (value.startsWith("20")) {
                    if (value.length() == 10) {
                        mSerialsControl.setText(value);
                        tryApply();
                    } else {
                        say("Неправильный код серийного номера");
                    }
                }
                if (value.startsWith("30")) {
                    if (value.length() == 8) {
                        mWhZoneControl.setText(value);
                        tryApply();
                    } else {
                        say("Неправильный код адресной ячейки");
                    }
                }
            }
            mScanControl.setText(null);
            result = true;
        }
        return result;
    }

    @Override
    protected boolean addRow(int aSqlId, HashMap<String, Object> aRow, ResultSet aResultSet) {
        boolean result = true;
        if (aSqlId == mSqlId_Form) {
            try {
                aRow.put("iMobWhScanId", aResultSet.getLong("iMobWhScanId"));
                aRow.put("vcSerialsNum", aResultSet.getString("vcSerialsNum"));
                aRow.put("vcWhZone", aResultSet.getString("vcWhZone"));
                aRow.put("vcErrors", aResultSet.getString("vcErrors"));
            } catch (SQLException e) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public void onClick(View aView) {
        super.onClick(aView);
        switch (aView.getId()) {
            case (R.id.TfmTakeIn_Row_btDelRow): {
                qp("iMobWhScanId").setValue(aView.getTag().toString());
                executeSql(R.string.sql_del_scan);
                break;
            }
            case (R.id.TfmTakeIn_btClear): {
                executeSql(R.string.sql_clear);
                break;
            }
            case (R.id.TfmTakeIn_btConfirm): {
                executeSql(R.string.sql_confirm);
                break;
            }
        }
    }

    @Override
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        switch (aSqlId) {
            case (R.string.sql_put_scan): {
                if (aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
                    HashMap<String, Object> row = new HashMap<String, Object>();
                    row.put("iMobWhScanId", qp("iMobWhScanId").getInteger());
                    row.put("vcSerialsNum", qp("vcSerialsNum").getString());
                    row.put("vcWhZone", qp("vcWhZone").getString());
                    row.put("vcErrors", qp("vcErrors").getString());
                    mDataSet.add(row);
                    mAdapter.notifyDataSetChanged();
                    clear();
                    break;
                }
            }
            case (R.string.sql_del_scan): {
                for (int i = 0; i < mDataSet.size(); i++) {
                    if (Integer.parseInt(mDataSet.get(i).get("iMobWhScanId").toString()) == (qp("iMobWhScanId").getInteger())) {
                        mDataSet.remove(i);
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
            }
            case (R.string.sql_confirm): {
                mDataSet.clear();
                mAdapter.notifyDataSetChanged();
                break;
            }
            case (R.string.sql_clear): {
                mDataSet.clear();
                mAdapter.notifyDataSetChanged();
                break;
            }
            default: {
                super.onSqlExecuted(aSqlId, aParams, aStatus);
            }
        }
    }
}
