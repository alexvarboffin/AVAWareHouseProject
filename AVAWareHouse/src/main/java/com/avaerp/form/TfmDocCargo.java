package com.avaerp.form;

import android.os.Bundle;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDocForm;

public class TfmDocCargo extends TDocForm {
    @Override
    public void onCreate(Bundle aBundle) {
        mLayoutId_Form = R.layout.tfm_doc_cargo;
        mLayoutId_Row = R.layout.tfm_doc_cargo_row;

        mControlId_DataSet = R.id.TfmDocCargo_gvData;
        mFieldMap.qp("vcName").setValue(R.id.TfmDocCargo_lbName);
        super.onCreate(aBundle);
        qp("iDocumentTypeId").setValue(25);
    }
}
