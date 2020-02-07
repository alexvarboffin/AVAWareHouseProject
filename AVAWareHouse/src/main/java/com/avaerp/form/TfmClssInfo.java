package com.avaerp.form;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBForm;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;
import com.avaerp.util.Util;

public class TfmClssInfo extends TDBForm {
    private void loadData() {
        selectAllText(getEditText(R.id.TfmClssInfo_edClssId));
        showKeyboard(true);
        if (checkRequires()) {
            String value = getTextViewText(R.id.TfmClssInfo_edClssId);
            int clssId = Integer.parseInt(value.substring(2));
            qp("iGdsClssId").setValue(clssId);
            qp("iResult").setValue(0);
            executeSql(R.string.sql_get_clss_info);
        }
    }
    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_clss_info);
        setRequires(getTextView(R.id.TfmClssInfo_edClssId));
    }
    @Override
    protected boolean checkRequires() {
        boolean result = super.checkRequires();
        if (result) {
            TextView view = getTextView(R.id.TfmClssInfo_edClssId);
            if (view != null && view.getText() != null && view.getText().length() > 0) {
                if (!view.getText().toString().startsWith(Util.getString(this, R.string.prefix_clss_id))) {
                    result = false;
                    v("Article doesn't start with valid prefix");
                    say(R.string.error_wrong_clss_id);
                }
            }
        }
        return result;
    }
    @Override
    public void onClick(View aView) {
        switch (aView.getId()) {
            case R.id.TfmClssInfo_btSearch: {
                loadData();
                break;
            }
            case R.id.TfmClssInfo_btKeyboard: {
                showKeyboard(true);
                break;
            }
        }
    }
    @Override
    protected boolean onKeyDown(View aView, int aKeyCode) {
        boolean result = false;
        View view = getTextView(R.id.TfmClssInfo_edClssId);
        if (view == aView && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            loadData();
            result = true;
        }
        return result;
    }
    @Override
    protected boolean onSystemKey(TextView aTextView, int aActionId) {
        boolean result = super.onSystemKey(aTextView, aActionId);
        switch (aActionId) {
            case EditorInfo.IME_ACTION_SEARCH: {
                switch (aTextView.getId()) {
                    case R.id.TfmClssInfo_edClssId: {
                        loadData();
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }
    @Override
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        if (aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
            if (qp("iResult").getInteger() == 1) {
                getTextView(R.id.TfmClssInfo_lbFullName).setText(qp("vcFullName").getString());
                getTextView(R.id.TfmClssInfo_lbArticle).setText(qp("vcArticle").getString());
                getTextView(R.id.TfmClssInfo_lbColor).setText(qp("vcColorName").getString());
                //getTextView(R.id.TfmClssInfo_lbColor).setBackgroundColor( 255);//qp("iColor").getInteger());
                getTextView(R.id.TfmClssInfo_lbOst3).setText(qp("vcOst3").getString());
            } else {
                getTextView(R.id.TfmClssInfo_edClssId).setError(Util.getString(this, R.string.error_clss_not_found));
                getTextView(R.id.TfmClssInfo_edClssId).requestFocus();
                sayLong(R.string.error_clss_not_found);
            }
        } else {
            super.onSqlExecuted(aSqlId, aParams, aStatus);
        }
    }
    @Override
    protected void onFocusChanged(View aView, boolean aHasFocus) {
        if (aView.getId() == R.id.TfmClssInfo_edClssId && aHasFocus) {
            showKeyboard(true);
        }
    }

    @Override
    protected void onModalForm(int aCode, boolean aResultOK) {
        if (aCode == TfmKeyboard.class.hashCode()) {
            if (aResultOK) {
                setTextViewText(getTextView(R.id.TfmClssInfo_edClssId), qpGlobal("KeyboardValue").getString());
                onKeyDown(getTextView(R.id.TfmClssInfo_edClssId), KeyEvent.KEYCODE_ENTER);
            }
        }
    }
}
