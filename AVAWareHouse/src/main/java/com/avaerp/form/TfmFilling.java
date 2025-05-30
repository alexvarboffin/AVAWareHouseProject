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
public class TfmFilling extends TDBListForm {
    EditText mScanControl = null;
    TextView mArticulControl = null;
    TextView mSerialsControl = null;
    EditText mQntControl = null;
    int mControlForKeyboard;

    public void clear() {
        mScanControl.setText(null);
        mArticulControl.setText(null);
        mQntControl.setText("1");
        mSerialsControl.setText(null);
    }

    public void tryApply() {
        String mArticul = mArticulControl.getText().toString();
        String mSerials = mSerialsControl.getText().toString();
        String mQnt = mQntControl.getText().toString();
        if (mQnt.isEmpty()) {
            mQnt = "1";
        }
        if (!mArticul.isEmpty() && !mSerials.isEmpty()) {
            qp("vcArticul").setValue(mArticul);
            qp("vcSerialsNum").setValue(mSerials);
            qp("vcQnt").setValue(mQnt);
            executeSql(R.string.sql_put_scan);
        }
    }

    @Override
    public void onCreate(Bundle aState) {
        mLayoutId_Form = R.layout.tfm_filling;
        mLayoutId_Row = R.layout.tfm_filling_row;
        mControlId_DataSet = R.id.TfmFilling_gvData;
        mDelBtnId = R.id.TfmFilling_Row_btDelRow;
        mFieldMap.qp("vcSerialsNum").setValue(R.id.TfmFilling_Row_SerialsNum);
        mFieldMap.qp("vcArticul").setValue(R.id.TfmFilling_Row_Articul);
        mFieldMap.qp("vcQnt").setValue(R.id.TfmFilling_Row_Qnt);
        mFieldMap.qp("vcErrors").setValue(R.id.TfmFilling_Row_Result);
        super.onCreate(aState);
        mSqlId_Form = R.string.sql_wh_queue;
        mScanControl = getEditText(R.id.TfmFilling_edScan);
        mArticulControl = getTextView(R.id.TfmFilling_edArticul);
        mSerialsControl = getTextView(R.id.TfmFilling_edSerials);
        mQntControl = getEditText(R.id.TfmFilling_edQnt);
        mQntControl.setSelectAllOnFocus(true);
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
        if (aView == mScanControl && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            String value = mScanControl.getText().toString();
            if (!value.isEmpty()) {
                if (value.startsWith("10")) {
                    if (value.length() == 16) {
                        mArticulControl.setText(value);
                        tryApply();
                    } else {
                        say("Неправильный код артикула");
                    }
                }
                if (value.startsWith("20")) {
                    if (value.length() == 10) {
                        mSerialsControl.setText(value);
                        tryApply();
                    } else {
                        say("Неправильный код серийного номера");
                    }
                }
            }
            mScanControl.setText(null);
            result = true;
        }
        if (aView == mQntControl && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            mScanControl.requestFocus();
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
                aRow.put("vcArticul", aResultSet.getString("vcArticul"));
                aRow.put("vcQnt", aResultSet.getString("vcQnt"));
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
        int id = aView.getId();
        if (id == R.id.TfmFilling_Row_btDelRow) {
            qp("iMobWhScanId").setValue(aView.getTag().toString());
            executeSql(R.string.sql_del_scan);
        } else if (id == R.id.TfmFilling_btClear) {
            executeSql(R.string.sql_clear);
        } else if (id == R.id.TfmFilling_btConfirm) {
            executeSql(R.string.sql_confirm);
        } else if (id == R.id.TfmFilling_btQntKeyboard) {
            mControlForKeyboard = R.id.TfmFilling_edQnt;
            showKeyboard(true);
        } else if (id == R.id.TfmFilling_btScanKeyboard) {
            mControlForKeyboard = R.id.TfmFilling_edScan;
            showKeyboard(true);
        }
    }

    @Override
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        if (aSqlId == R.string.sql_put_scan) {
            if (aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
                HashMap<String, Object> row = new HashMap<String, Object>();
                row.put("iMobWhScanId", qp("iMobWhScanId").getInteger());
                row.put("vcSerialsNum", qp("vcSerialsNum").getString());
                row.put("vcArticul", qp("vcArticul").getString());
                row.put("vcQnt", qp("vcQnt").getString());
                row.put("vcErrors", qp("vcErrors").getString());
                mDataSet.add(row);
                mAdapter.notifyDataSetChanged();
                clear();
                return;
            }
            for (int i = 0; i < mDataSet.size(); i++) {
                if (Integer.parseInt(mDataSet.get(i).get("iMobWhScanId").toString()) == (qp("iMobWhScanId").getInteger())) {
                    mDataSet.remove(i);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else if (aSqlId == R.string.sql_del_scan) {
            for (int i = 0; i < mDataSet.size(); i++) {
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
    protected void onModalForm(int aCode, boolean aResultOk) {
        if (aCode == TfmInvDoc.class.hashCode()) {
            if (!aResultOk) {
                finish();
            } else {
                qp("iDocumentId").setValue(qpGlobal("iDocumentId").getInteger());
                querySql(mSqlId_Form);
            }
        } else if (aCode == TfmKeyboard.class.hashCode()) {
            if (aResultOk) {
                if (mControlForKeyboard != 0) {
                    setTextViewText(mControlForKeyboard, qpGlobal("KeyboardValue").getString());
                    onKeyDown(getView(mControlForKeyboard), KeyEvent.KEYCODE_ENTER);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putLong("iDocumentId", qp("iDocumentId").getInteger());
        bundle.putString("mScanControl.Text", mScanControl.getText().toString());
        bundle.putString("mArticulControl.Text", mArticulControl.getText().toString());
        bundle.putString("mSerialsControl.Text", mSerialsControl.getText().toString());
        bundle.putString("mQntControl.Text", mQntControl.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle aState) {
        if (aState != null) {
            qp("iDocumentId").setValue(aState.getLong("iDocumentId", 0));
        }
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        if (qp("iDocumentId").getInteger() == 0) {
            showForm(TfmInvDoc.class, true, getParams0());
        } else {
            querySql(mSqlId_Form);
            mScanControl.setText(bundle.getString("mScanControl.Text", null));
            mArticulControl.setText(bundle.getString("mArticulControl.Text", null));
            mSerialsControl.setText(bundle.getString("mSerialsControl.Text", null));
            mQntControl.setText(bundle.getString("mQntControl.Text", "1"));
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
