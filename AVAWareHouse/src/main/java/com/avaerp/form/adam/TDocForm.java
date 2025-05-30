package com.avaerp.form.adam;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.TfmGdsPos;
import com.avaerp.form.TfmKeyboard;
import com.avaerp.form.TfmWHQueue;
import com.avaerp.form.TfmWorkMode;
import com.avaerp.util.TParams;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TDocForm extends TDBListForm {
    private static final String TAG = "@@@";

    private int mControlId_DocumentCode;

    private boolean mIsKeyboard = false;
    protected int mControlForKeyboard;


    @Override
    public void onCreate(Bundle aState) {
        mLayoutId_Form = R.layout.tfm_doc_form;
        mLayoutId_Row = R.layout.tfm_doc_form_row;
        //super.onCreate(aState);

        mControlId_DataSet = R.id.TfmDocForm_gvData;
        mFieldMap.qp("vcName").setValue(R.id.TfmDocForm_lbName);
        super.onCreate(aState);
        if (mSqlId_Form == 0) mSqlId_Form = R.string.sql_document;
    }

    @Override
    public void onStart() {
        super.onStart();
        //execSql(R.string.sql_doc_form_caption, getParams(), TSqlExecutor.TSqlMode.QUERY);
    }

//    @Override
//    protected boolean onKeyDown(View aView, int aKeyCode) {
//        Log.d(TAG, "[code] --> " + aKeyCode);
//
////        try {
////            if (aView.getId() == mControlId_DocumentCode && aKeyCode == KeyEvent.KEYCODE_ENTER) {
////                qp("vcScanValue").setValue(getTextViewText(mControlId_DocumentCode));
////                querySql(mSqlId_Form);
////            }
////        } catch (Exception e) {
////            Log.d(TAG, "onKeyDown: " + e.getLocalizedMessage());
////            return result;
////        }
//        return super.onKeyDown(aView, aKeyCode);
//    }

    @Override
    protected boolean addRow(int aSqlId, HashMap<String, Object> aRow, ResultSet aResultSet) {
        boolean result = true;
        try {
            aRow.put("iDocumentId", aResultSet.getLong("iDocumentId"));
            aRow.put("vcName", aResultSet.getString("vcName"));
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }

    protected void selectWorkMode(int aPosition) {
        showForm(TfmWorkMode.class, true, getParams0());
    }

    @Override
    protected void onListItemClick(ListView aListView, View aView, int aPosition, long aId) {
        qpGlobal("iDocumentId").setValue(fbLong("iDocumentId", aId));
        setResult(RESULT_OK);
        finish();
        //qp("iDocumentId").setValue(fbLong("iDocumentId", aId));
        //selectWorkMode(aPosition);
    }

    @Override
    protected void onModalForm(int aCode, boolean aResultOK) {
        if (aCode == TfmWorkMode.class.hashCode()) {
            if (aResultOK) {
                switch (qpGlobal("WorkMode").getInteger()) {
                    case TfmWorkMode.WM_VIEW: {
                        showForm(TfmGdsPos.class, false, getParams0());
                        break;
                    }
                    default: {
                        showForm(TfmWHQueue.class, false, getParams0());
                    }
                }
            }
        } else if (aCode == TfmKeyboard.class.hashCode()) {
            if (aResultOK) {
                //@@@       setTextViewText(mControlId_DocumentCode, qpGlobal("KeyboardValue").getString());

                //@@@  onKeyDown(getView(mControlId_DocumentCode), KeyEvent.KEYCODE_ENTER);
            }
        }
    }

    @Override
    public void afterSqlQuery(int aSqlId, TParams aParams, ResultSet aResultSet) {
        if (aSqlId == R.string.sql_doc_form_caption) {
        } else {
            super.afterSqlQuery(aSqlId, aParams, aResultSet);
        }
    }

//    @Override
//    public void onClick(View aView) {
//        if (aView.getId() == mControl_KeyboardButton) {
//            mControlForKeyboard = mControlId_DocumentCode;
//            mIsKeyboard = !mIsKeyboard;
//            showKeyboard(mIsKeyboard);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        mControlId_DocumentCode = R.id.user_input;
        EditText view = findViewById(R.id.user_input);
        //view.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
        view.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    qp("vcScanValue").setValue(getTextViewText(mControlId_DocumentCode));
                    querySql(mSqlId_Form);

                    return true;
                }
                return false;
            }
        });

        Button submit = findViewById(R.id.TfmDocForm_btKeyboard);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qp("vcScanValue").setValue(getTextViewText(mControlId_DocumentCode));
                querySql(mSqlId_Form);
            }
        });
    }


}

