package com.avaerp.form;

import android.os.Bundle;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDocForm;

public class TfmInvent extends TDocForm {
    @Override
    public void onCreate(Bundle aBundle) {
        mLayoutId_Form = R.layout.tfm_invent;
        mLayoutId_Row = R.layout.tfm_invent_row;

        mControlId_DataSet = R.id.TfmInvent_gvData;
        mFieldMap.qp("vcName").setValue(R.id.TfmInvent_lbName);
        super.onCreate(aBundle);
        qp("iDocumentTypeId").setValue(5);
    }
}
