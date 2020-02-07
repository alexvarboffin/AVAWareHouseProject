package com.avaerp.form;

import android.os.Bundle;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDocForm;

public class TfmInvDoc extends TDocForm {

    @Override
    public void onCreate(Bundle aBundle) {
        mLayoutId_Form = R.layout.tfm_inv_doc;
        mLayoutId_Row = R.layout.tfm_inv_doc_row;

        mControlId_DataSet = R.id.TfmInvDoc_gvData;
        mFieldMap.qp("vcName").setValue(R.id.TfmInvDoc_lbName);
        super.onCreate(aBundle);
        qp("iDocumentTypeId").setValue(98);
    }
}
