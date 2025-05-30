package com.avaerp.form;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TStoredForm;

public class TfmWorkMode extends TStoredForm {
    public final static int WM_TAKE_OFF = 0x0001;
    public final static int WM_TAKE_ON = 0x0002;
    public final static int WM_FILLING = 0x0004;
    public final static int WM_VIEW = 0x0008;
    private Button btTakeOff;
    private Button btTakeOn;
    private Button btFilling;
    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_work_mode);
        btTakeOff = getButton(R.id.TfmWorkMode_btTakeOff);
        btTakeOn = getButton(R.id.TfmWorkMode_btTakeOn);
        btFilling = getButton(R.id.TfmWorkMode_btFilling);
        int i = qp("iDocumentTypeId").getInteger();
        btTakeOff.setVisibility(i == 25? View.VISIBLE: View.GONE);
        btTakeOn.setVisibility(i == 25|i == 98|i == 219? View.VISIBLE: View.GONE);
        btFilling.setVisibility(i == 35? View.VISIBLE: View.GONE);
    }
    @Override
    public void onClick(View aView) {
        int id = aView.getId();
        if (id == R.id.TfmWorkMode_btTakeOff) {
            qpGlobal("WorkMode").setValue(WM_TAKE_OFF);
            setResult(RESULT_OK);
            finish();
        } else if (id == R.id.TfmWorkMode_btTakeOn) {
            qpGlobal("WorkMode").setValue(WM_TAKE_ON);
            setResult(RESULT_OK);
            finish();
        } else if (id == R.id.TfmWorkMode_btFilling) {
            qpGlobal("WorkMode").setValue(WM_FILLING);
            setResult(RESULT_OK);
            finish();
        } else if (id == R.id.TfmWorkMode_btView) {
            qpGlobal("WorkMode").setValue(WM_VIEW);
            setResult(RESULT_OK);
            finish();
        }
    }
}
