package com.avaerp.form;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBForm;
import com.avaerp.util.TOfflineInAdapter;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Будда on 10.07.2015.
 */
public class TfmOfflineIn extends TDBForm {
    private ArrayList<HashMap<String, Object>> mDataSet;
    private TOfflineInAdapter mAdapter;
    EditText mCtrlScan = null;
    TextView mCtrlWhZone = null;
    TextView mCtrlSerials = null;
    int mKeyCtrlId;
    private void newScan() {
        mCtrlScan.setText(null);
        mCtrlWhZone.setText(null);
        mCtrlSerials.setText(null);
    }
    private void addScan(String aWhZone, String aSerials, String aResult) {
        HashMap<String, Object> mRow = new HashMap<String, Object>();
        mRow.put("vcWhZone", aWhZone);
        mRow.put("vcSerials", aSerials);
        mRow.put("vcResult", aResult);
        mDataSet.add(mRow);
        mAdapter.notifyDataSetChanged();
    }
    private void parse(String aText) {
        mDataSet.clear();
        String[] rows = aText.split("~;");
        for (String row: rows) {
            String[] cols = row.split("~,");
            if (cols.length != 5) {
                sayLong("Ошибка в сохраненных данных");
            } else {
                if (cols[1].contains("NULL")) {cols[1] = "";}
                if (cols[2].contains("NULL")) {cols[2] = "";}
                if (cols[4].contains("NULL")) {cols[4] = "";}
                addScan(cols[2], cols[1], cols[4]);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    private String Encode() {
        StringBuilder result = new StringBuilder();
        for (HashMap<String, Object> row: mDataSet) {
            result.append("NULL~,");
            result.append(row.get("vcSerials").toString()).append("~,");
            result.append(row.get("vcWhZone").toString()).append("~,");
            result.append("NULL~,");
            if (row.get("vcResult").toString() == "") {
                result.append("NULL~;");
            } else {
                result.append(row.get("vcResult").toString()).append("~;");
            }
        }
        return result.toString();
    }
    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_offline_in);
        mCtrlScan = findViewById(R.id.TfmOfflineIn_edScan);
        mCtrlSerials = findViewById(R.id.TfmOfflineIn_edSerials);
        mCtrlWhZone = findViewById(R.id.TfmOfflineIn_edWhZone);
        mDataSet = new ArrayList<HashMap<String, Object>>();
        mAdapter = new TOfflineInAdapter(this, 0, mDataSet, R.layout.tfm_offline_in_row);
        qpGlobal("WorkMode").setValue(2);
        getListView(R.id.TfmOfflineIn_gvData).setAdapter(mAdapter);
    }
    @Override
    public boolean beforeSqlExecute(int aSqlId, TParams aParams, TSqlExecutor.TSqlMode aSqlMode) {
        aParams.qp("iWorkMode").setValue(qpGlobal("WorkMode").getInteger());
        aParams.qp("iStaffId").setValue(getUserID());
        //aParams.qp("iDocumentId").setValue(qpGlobal("iDocumentId").getInteger());
        aParams.qp("iDocumentId").setValue(0);
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
        if (id == R.id.TfmOfflineIn_btScanKeyboard) {
            mKeyCtrlId = R.id.TfmOfflineIn_edScan;
            showKeyboard(true);
        } else if (id == R.id.TfmOfflineIn_Row_btDelRow) {
            int pos = Integer.parseInt(aView.getTag().toString());
            mDataSet.remove(pos);
            mAdapter.notifyDataSetChanged();
        } else if (id == R.id.TfmOfflineIn_btClear) {
            yesNo(0, "Очистить список?");
        } else if (id == R.id.TfmOfflineIn_btSave) {
            executeSql(R.string.sql_offline_filling_save);
        } else if (id == R.id.TfmOfflineIn_btConfirm) {
            executeSql(R.string.sql_confirm);
        }
    }
    @Override
    protected void onYesNo(int aDialogId, boolean aResult) {
        if (aResult) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected boolean onKeyDown(View aView, int aKeyCode) {
        boolean result = super.onKeyDown(aView, aKeyCode);
        if (aView == mCtrlScan && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            String value = mCtrlScan.getText().toString();
            if (!value.isEmpty()) {
                if (value.startsWith("30")) {
                    if (value.length() == 8) {
                        mCtrlWhZone.setText(value);
                    } else {
                        say("Неправильный код ячейки");
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
            if (mCtrlWhZone.getText().toString().length() > 0 &&
                    mCtrlSerials.getText().toString().length() > 0) {
                addScan(mCtrlWhZone.getText().toString(), mCtrlSerials.getText().toString(), "");
                newScan();
            }
            mCtrlScan.setText(null);
            result = true;
        }
        return result;
    }
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        if (!(aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL)) {return;}
        if (aSqlId == R.string.sql_offline_filling_start) {
            if (aParams.qp("vcResult").getString() != null) {
                parse(aParams.qp("vcResult").getString());
            }
        } else if (aSqlId == R.string.sql_offline_filling_save) {
            if (aParams.qp("vcResult").getString() != null) {
                parse(aParams.qp("vcResult").getString());
            }
            sayModal("Данные успешно сохранены");
        } else if (aSqlId == R.string.sql_confirm) {
            mDataSet.clear();
            mAdapter.notifyDataSetChanged();
            sayModal("Данные успешно подтверждены");
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle aState) {
        super.onSaveInstanceState(aState);
        aState.putLong("iDocumentId", qp("iDocumentId").getInteger());
        aState.putString("vcData", Encode());
        aState.putString("mScanControl.Text", mCtrlScan.getText().toString());
        aState.putString("mWhZoneControl.Text", mCtrlWhZone.getText().toString());
        aState.putString("mSerialsControl.Text", mCtrlSerials.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle aState) {
        super.onRestoreInstanceState(aState);
        if (aState != null) {
            qp("iDocumentId").setValue(aState.getLong("iDocumentId", 0));
        }
    }
    @Override
    protected void onPostCreate(Bundle aState) {
        super.onPostCreate(aState);
        if (aState != null) {
            parse(aState.get("vcData").toString());
            mCtrlScan.setText(aState.getString("mScanControl.Text", null));
            mCtrlWhZone.setText(aState.getString("mWhZoneControl.Text", null));
            mCtrlSerials.setText(aState.getString("mSerialsControl.Text", null));
        }
    }
}
