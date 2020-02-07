package com.avaerp.form;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBForm;
import com.avaerp.util.TDataAdapter;
import com.avaerp.util.TDataArray;
import com.avaerp.util.TParams;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TfmTakeOff extends TDBForm {
    TDataAdapter mAdapter;
    TParams mFieldMap = new TParams();
    ArrayList<HashMap<String, Object>> mDataArray = new ArrayList<HashMap<String, Object>>();

    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_take_off);
        mFieldMap.qp("vcSerialsNum").setValue(R.id.TfmTakeOff_vcSerialsNum);
        mFieldMap.qp("vcFullName").setValue(R.id.TfmTakeOff_vcFullName);
        mFieldMap.qp("vcArticul").setValue(R.id.TfmTakeOff_vcArticul);
        mAdapter = new TDataAdapter(this, 0, mDataArray, R.layout.tfm_take_off_row, mFieldMap);
        querySql(R.string.sql_take_off_load);
    }
    //@Override
    protected boolean addRow(int aSqlId, HashMap<String, Object> aRow, ResultSet aResultSet) {
        if (aSqlId == R.string.sql_take_off_load) {
            mDataArray.add(aRow);
            return true;
        } else {
            return true;//super.addRow(aSqlId, aRow, aResultSet);
        }
    }

}
