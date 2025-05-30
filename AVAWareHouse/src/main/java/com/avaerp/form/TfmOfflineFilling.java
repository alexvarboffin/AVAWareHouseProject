package com.avaerp.form;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBForm;
import com.avaerp.util.TOfflineFillingAdapter;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Будда on 10.07.2015.
 */
public class TfmOfflineFilling extends TDBForm {
    private ArrayList<HashMap<String, Object>> mDataSet;
    private TOfflineFillingAdapter mAdapter;
    EditText mCtrlScan = null;
    TextView mCtrlArticul = null;
    TextView mCtrlSerials = null;
    EditText mCtrlQnt = null;
    int mKeyCtrlId;
    private void newScan() {
        mCtrlScan.setText(null);
        mCtrlArticul.setText(null);
        mCtrlSerials.setText(null);
        mCtrlQnt.setText("1");
    }
    private void addScan(String aArticul, String aSerials, String aQnt, String aResult) {
        HashMap<String, Object> mRow = new HashMap<String, Object>();
        mRow.put("vcArticul", aArticul);
        mRow.put("vcSerials", aSerials);
        mRow.put("vcQnt", aQnt);
        mRow.put("vcResult", aResult);
        mDataSet.add(mRow);
    }
    private void parse(String aText) {
        mDataSet.clear();
        String[] rows = aText.split("~;");
        for (String row: rows) {
            String[] cols = row.split("~,");
            if (cols.length != 5) {
                sayLong("Ошибка в данных, полученных с сервера");
            } else {
                addScan(cols[0], cols[1], cols[3], cols[4]);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    private String Encode() {
        String result = "";
        for (HashMap<String, Object> row: mDataSet) {
            result = result + row.get("vcArticul").toString() + "~,";
            result = result + row.get("vcSerials").toString() + "~,";
            result = result + "~,";
            result = result + row.get("vcQnt").toString() + "~;";
        }
        return result;
    }
    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_offline_filling);
        mCtrlScan = (EditText) findViewById(R.id.TfmOfflineFilling_edScan);
        mCtrlArticul = (TextView) findViewById(R.id.TfmOfflineFilling_edArticul);
        mCtrlSerials = (TextView) findViewById(R.id.TfmOfflineFilling_edSerials);
        mCtrlQnt = (EditText) findViewById(R.id.TfmOfflineFilling_edQnt);
        mDataSet = new ArrayList<HashMap<String, Object>>();
        mAdapter = new TOfflineFillingAdapter(this, 0, mDataSet, R.layout.tfm_offline_filling_row);
        qpGlobal("WorkMode").setValue(1);
        getListView(R.id.TfmOfflineFilling_gvData).setAdapter(mAdapter);
    }
    @Override
    public boolean beforeSqlExecute(int aSqlId, TParams aParams, TSqlExecutor.TSqlMode aSqlMode) {
        aParams.qp("iWorkMode").setValue(qpGlobal("WorkMode").getInteger());
        aParams.qp("iStaffId").setValue(getUserID());
        aParams.qp("iDocumentId").setValue(qpGlobal("iDocumentId").getInteger());
        aParams.qp("vcData").setValue(Encode());
        return true;
    }
    @Override
    protected void onModalForm(int aCode, boolean aResultOk) {
        if (aCode == TfmInvDoc.class.hashCode()) {
            if (!aResultOk) {
                finish();
            } else {
                qp("iDocumentId").setValue(qpGlobal("iDocumentId").getInteger());
                querySql(R.string.sql_offline_filling_start);
            }
        } else if (aCode == TfmKeyboard.class.hashCode()) {
            if (aResultOk) {
                if (mKeyCtrlId != 0) {
                    setTextViewText(mKeyCtrlId, qpGlobal("KeyboardValue").getString());
                    onKeyDown(getView(mKeyCtrlId), KeyEvent.KEYCODE_ENTER);
                }
            }
        }
    }
    @Override
    public void onClick(View aView) {
        super.onClick(aView);
        int id = aView.getId();
        if (id == R.id.TfmOfflineFilling_btQntKeyboard) {
            mKeyCtrlId = R.id.TfmOfflineFilling_edQnt;
            showKeyboard(true);
        } else if (id == R.id.TfmOfflineFilling_btScanKeyboard) {
            mKeyCtrlId = R.id.TfmOfflineFilling_edScan;
            showKeyboard(true);
        } else if (id == R.id.TfmOfflineFilling_Row_btDelRow) {
            int pos = Integer.parseInt(aView.getTag().toString());
            mDataSet.remove(pos);
            mAdapter.notifyDataSetChanged();
        } else if (id == R.id.TfmOfflineFilling_btClear) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        } else if (id == R.id.TfmOfflineFilling_btSave) {
            executeSql(R.string.sql_offline_filling_save);
        } else if (id == R.id.TfmOfflineFilling_btConfirm) {
            executeSql(R.string.sql_confirm);
        }
    }

    @Override
    protected boolean onKeyDown(View aView, int aKeyCode) {
        boolean result = super.onKeyDown(aView, aKeyCode);
        if (aView == mCtrlScan && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            String value = mCtrlScan.getText().toString();
            if (!value.isEmpty()) {
                if (value.startsWith("10")) {
                    if (value.length() == 16) {
                        mCtrlArticul.setText(value);
                    } else {
                        say("Неправильный код артикула");
                    }
                }
                if (value.startsWith("20")) {
                    if (value.length() == 10) {
                        mCtrlSerials.setText(value);
                    } else {
                        say("Неправильный код серийного номера");
                    }
                }
            }
            if (!mCtrlArticul.getText().toString().isEmpty() &&
                    !mCtrlSerials.getText().toString().isEmpty() &&
                    !mCtrlQnt.getText().toString().isEmpty()) {
                addScan(mCtrlArticul.getText().toString(), mCtrlSerials.getText().toString(),
                        mCtrlQnt.getText().toString(), null);
                newScan();
            }
            mCtrlScan.setText(null);
            result = true;
        }
        if (aView == mCtrlQnt && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            mCtrlScan.requestFocus();
            mCtrlScan.setText(null);
            result = true;
        }
        return result;
    }
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        if (aSqlId == R.string.sql_offline_filling_start) {
            if (aParams.qp("vcResult").getString() != null) {
                parse(aParams.qp("vcResult").getString());
            }
        } else if (aSqlId == R.string.sql_offline_filling_save) {
            if (aParams.qp("vcResult").getString() != null) {
                parse(aParams.qp("vcResult").getString());
            }
        } else if (aSqlId == R.string.sql_confirm) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
    protected void onSaveInstanceState(Bundle aState) {
        aState.putLong("iDocumentId", qp("iDocumentId").getInteger());
        aState.putString("vcData", Encode());
        aState.putString("mScanControl.Text", mCtrlScan.getText().toString());
        aState.putString("mArticulControl.Text", mCtrlArticul.getText().toString());
        aState.putString("mSerialsControl.Text", mCtrlSerials.getText().toString());
        aState.putString("mQntControl.Text", mCtrlQnt.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle aState) {
        if (aState != null) {
            qp("iDocumentId").setValue(aState.getLong("iDocumentId", 0));
        }
    }
    @Override
    protected void onPostCreate(Bundle aState) {
        super.onPostCreate(aState);
        if (qp("iDocumentId").getInteger() == 0) {
            showForm(TfmInvDoc.class, true, getParams0());
        } else {
            parse(aState.get("vcData").toString());
            mCtrlScan.setText(aState.getString("mScanControl.Text", ""));
            mCtrlArticul.setText(aState.getString("mArticulControl.Text", null));
            mCtrlSerials.setText(aState.getString("mSerialsControl.Text", null));
            mCtrlQnt.setText(aState.getString("mQntControl.Text", "1"));
        }
    }
}
