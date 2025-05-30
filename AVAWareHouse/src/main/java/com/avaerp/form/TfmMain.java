package com.avaerp.form;

import android.os.Bundle;
import android.view.View;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBForm;
import com.avaerp.form.adam.TDocForm;
import com.avaerp.util.TParams;

public class TfmMain extends TDBForm {
    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_main);
        if (!qpGlobal("isConnectedToDB").getBoolean()) {
            getView(R.id.TfmMain_btClssInfo).setEnabled(false);
            showForm(TfmLogin.class, true);
        }
    }
    @Override
    protected void onModalForm(int aCode, boolean aResultOk) {
        if (aCode == TfmLogin.class.hashCode()) {
            if (!aResultOk) {
                finish();
            } else {
                getView(R.id.TfmMain_btClssInfo).setEnabled(true);
            }
        }
    }
    @Override
    protected void onClick(View aView) {
        TParams params = new TParams();
        qp("iDocumentTypeId").setValue(0);
        int id = aView.getId();
        if (id == R.id.TfmMain_btFilling) {
            qpGlobal("WorkMode").setValue(1);
            showForm(TfmFilling.class, false, params);
        } else if (id == R.id.TfmMain_btTakeIn) {
            qpGlobal("WorkMode").setValue(2);
            showForm(TfmTakeIn.class, false, params);
        } else if (id == R.id.TfmMain_btAccPost) {
            params.qp("iDocumentTypeId").setValue(219);
        } else if (id == R.id.TfmMain_btInvDoc) {
            params.qp("iDocumentTypeId").setValue(98);
        } else if (id == R.id.TfmMain_btInvent) {
            params.qp("iDocumentTypeId").setValue(5);
        } else if (id == R.id.TfmMain_btAssemble) {
            params.qp("iDocumentTypeId").setValue(35);
        } else if (id == R.id.TfmMain_btDocCargo) {
            params.qp("iDocumentTypeId").setValue(25);
        } else if (id == R.id.TfmMain_btClssInfo) {
            showForm(TfmClssInfo.class, false);
        } else if (id == R.id.TfmMain_btOfflineFilling) {
            qpGlobal("WorkMode").setValue(1);
            showForm(TfmOfflineFilling.class, false);
        } else if (id == R.id.TfmMain_btOfflineIn) {
            qpGlobal("WorkMode").setValue(2);
            showForm(TfmOfflineIn.class, false);
        }
        if (params.qp("iDocumentTypeId").getInteger() != 0) {
            showForm(TDocForm.class, false, params);
        }
    }
}
