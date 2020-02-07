package com.avaerp.form;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBForm;
import com.avaerp.form.adam.TDBListForm;
import com.avaerp.form.adam.TDocForm;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TfmAssemble extends TDocForm {
    @Override
    public void onCreate(Bundle aBundle) {
        mLayoutId_Form = R.layout.tfm_assemble;
        mLayoutId_Row = R.layout.tfm_assemble_row;

        mControlId_DataSet = R.id.TfmAssemble_gvData;
        mFieldMap.qp("vcName").setValue(R.id.TfmAssemble_lbName);
        super.onCreate(aBundle);
        qp("iDocumentTypeId").setValue(35);
    }
}
