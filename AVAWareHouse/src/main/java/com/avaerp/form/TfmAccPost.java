package com.avaerp.form;

import android.os.Bundle;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDocForm;

public class TfmAccPost extends TDocForm {
    @Override
    public void onCreate(Bundle aBundle) {
        mLayoutId_Form = R.layout.tfm_acc_post;
        mLayoutId_Row = R.layout.tfm_acc_post_row;

        mControlId_DataSet = R.id.TfmAccPost_gvData;
        mFieldMap.qp("vcName").setValue(R.id.TfmAccPost_lbName);
        super.onCreate(aBundle);
        qp("iDocumentTypeId").setValue(219);
    }
}
