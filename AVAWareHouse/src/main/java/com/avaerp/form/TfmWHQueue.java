package com.avaerp.form;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBListForm;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TfmWHQueue extends TDBListForm {
    protected TextView scanControl;
    String mSerialsNum;
    String mArticul;
    String mWhZone;
    String mQnt;
    private int mode() {
        return qpGlobal("WorkMode").getInteger();
    }
    private void clear() {
        mSerialsNum = null;
        mArticul = null;
        mWhZone = null;
        mQnt = null;
        scanControl.setHint("Введите серийный номер");
    }
    @Override
    public void onCreate(Bundle aState) {
        mLayoutId_Form = R.layout.tfm_wh_queue;
        mLayoutId_Row = R.layout.tfm_wh_queue_row;
        mControlId_DataSet = R.id.TfmWHQueue_gvData;
        mSqlId_Form = R.string.sql_wh_queue;
        mFieldMap.qp("vcSerialsNum").setValue(R.id.TfmWHQueue_Row_vcSerialsNum);
        mFieldMap.qp("vcArticul").setValue(R.id.TfmWHQueue_Row_vcArticul);
        mFieldMap.qp("vcWhZone").setValue(R.id.TfmWHQueue_Row_vcWhZone);
        mFieldMap.qp("vcQnt").setValue(R.id.TfmWHQueue_Row_vcQnt);
        mFieldMap.qp("vcErrors").setValue(R.id.TfmWHQueue_Row_vcErrors);
        super.onCreate(aState);
        scanControl = getTextView(R.id.TfmWHQueue_edValue);
        clear();
    }

    @Override
    public boolean beforeSqlExecute(int aSqlId, TParams aParams, TSqlExecutor.TSqlMode aSqlMode) {
        aParams.qp("iWorkMode").setValue(qpGlobal("WorkMode").getInteger());
        aParams.qp("iStaffId").setValue(getUserID());
        return true;
    }

    @Override
    protected boolean onKeyDown(View aView, int aKeyCode) {
        boolean result = super.onKeyDown(aView, aKeyCode);
        if (aView == scanControl && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            String value = scanControl.getText().toString();
            if (!value.isEmpty()) {
                onScan(value);
                scanControl.setText(null);
            }
        }
        return result;
    }

    private void apply() {
        qp("vcSerialsNum").setValue(mSerialsNum);
        if (mode() == TfmWorkMode.WM_TAKE_ON | mode() == TfmWorkMode.WM_FILLING) {
            qp("vcWhZone").setValue(mWhZone);
            qp("vcQnt").setValue(mQnt);
        }
        if (mode() == TfmWorkMode.WM_FILLING) {
            qp("vcArticul").setValue(mArticul);
        }
        executeSql(R.string.sql_put_scan);
    }

    protected void onScan(String aValue) {
        if (mSerialsNum == null) {
            mSerialsNum = aValue;
            if (mode() == TfmWorkMode.WM_TAKE_OFF) {
                apply();
            } else {
                scanControl.setHint(mode() == TfmWorkMode.WM_TAKE_ON ? "Введите количество" : "Введите артикул");
                scanControl.setText(mode() == TfmWorkMode.WM_TAKE_ON ? "1" : null);
            }
            return;
        };
        if (mQnt == null && mode() == TfmWorkMode.WM_TAKE_ON) {
            mQnt = aValue;
            scanControl.setHint("Введите адрес хранения");
            return;
        }
        if (mArticul == null && mode() == TfmWorkMode.WM_FILLING) {
            mArticul = aValue;
            scanControl.setHint("Введите адрес хранения");
            return;
        }
        mWhZone = aValue;
        apply();
    }

    @Override
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        if (aSqlId == R.string.sql_put_scan) {
            if (aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
                HashMap<String, Object> row = new HashMap<String, Object>();
                row.put("iMobWhScanId", qp("iMobWhScanId").getInteger());
                row.put("vcSerialsNum", qp("vcSerialsNum").getString());
                row.put("vcArticul", qp("vcArticul").getString());
                row.put("vcWhZone", qp("vcWhZone").getString());
                row.put("vcQnt", qp("vcQnt").getString());
                row.put("vcErrors", qp("vcErrors").getString());
                mDataSet.add(row);
                mAdapter.notifyDataSetChanged();
                clear();
                return;
            }
            for (int i = 0; i < mDataSet.size(); i++) {
                //int i1 = (Integer) mDataSet.get(i).get("iMobWhScanId");
                //int i2 = qp("iMobWhScanId").getInteger();
                //if (i1 == i2) {
                if (Integer.parseInt(mDataSet.get(i).get("iMobWhScanId").toString()) == (qp("iMobWhScanId").getInteger())) {
                    mDataSet.remove(i);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else if (aSqlId == R.string.sql_del_scan) {
            for (int i = 0; i < mDataSet.size(); i++) {
                //int i1 = (Integer) mDataSet.get(i).get("iMobWhScanId");
                //int i2 = qp("iMobWhScanId").getInteger();
                //if (i1 == i2) {
                if (Integer.parseInt(mDataSet.get(i).get("iMobWhScanId").toString()) == (qp("iMobWhScanId").getInteger())) {
                    mDataSet.remove(i);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else if (aSqlId == R.string.sql_confirm) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        } else if (aSqlId == R.string.sql_clear) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        } else {
            super.onSqlExecuted(aSqlId, aParams, aStatus);
        }
    }

    @Override
    protected boolean addRow(int aSqlId, HashMap<String, Object> aRow, ResultSet aResultSet) {
        boolean result = true;
        if (aSqlId == mSqlId_Form) {
            try {
                aRow.put("iMobWhScanId", aResultSet.getLong("iMobWhScanId"));
                aRow.put("vcSerialsNum", aResultSet.getString("vcSerialsNum"));
                aRow.put("vcArticul", aResultSet.getString("vcArticul"));
                aRow.put("vcWhZone", aResultSet.getString("vcWhZone"));
                aRow.put("vcQnt", aResultSet.getString("vcQnt"));
                aRow.put("vcErrors", aResultSet.getString("vcErrors"));
            } catch (SQLException e) {
                result = false;
            }
        }
        return result;
    }

    public void onClick(View aView) {
        super.onClick(aView);
        int id = aView.getId();
        if (id == R.id.btDelRow) {
            qp("iMobWhScanId").setValue(aView.getTag().toString());
            executeSql(R.string.sql_del_scan);
        } else if (id == R.id.btClear) {
            executeSql(R.string.sql_clear);
        } else if (id == R.id.btConfirm) {
            executeSql(R.string.sql_confirm);
        }
    }
/*    @Override
    protected boolean addRow(int aSqlId, HashMap<String, Object> aRow, ResultSet aResultSet) {
        boolean result = true;
        try {
            aRow.put("vcValue", aResultSet.getString("vcValue"));
            qp("iMobWhScanId").setValue(aResultSet.getInt("iMobWhScanId"));
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }
    @Override
    protected boolean onKeyDown(View aView, int aKeyCode) {
        boolean result = super.onKeyDown(aView, aKeyCode);
        if (aView.getId() == mControlId_Value && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            String value = getTextViewText(mControlId_Value);
            if (!value.isEmpty()) {
                qp("vcValue").setValue(value.substring(2));
                execSql(R.string.sql_wh_queue_put, getParams(), TSqlExecutor.TSqlMode.EXECUTE);
                getTextView(mControlId_Value).setText(null);
            }
        }
        return result;
    }
*/
}
