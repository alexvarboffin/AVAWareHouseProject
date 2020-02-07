package com.avaerp.form;

import android.os.Bundle;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TPosForm;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TfmGdsPos extends TPosForm {
    @Override
    public void onCreate(Bundle aState) {
        mLayoutId_Form = R.layout.tfm_gds_pos;
        mLayoutId_Row = R.layout.tfm_gds_pos_row;
        mSqlId_Form = R.string.sql_positions;
        mControlId_DataSet = R.id.TfmGdsPos_gvData;
        mFieldMap.qp("vcFullName").setValue(R.id.TfmGdsPos_lbName);
        mFieldMap.qp("vcArticul").setValue(R.id.TfmGdsPos_lbArticul);
        mFieldMap.qp("decQnt").setValue(R.id.TfmGdsPos_lbQnt);
        super.onCreate(aState);
    }
    @Override
    protected boolean addRow(int aSqlId, HashMap<String, Object> aRow, ResultSet aResultSet) {
        boolean result = true;
        try {
            aRow.put("iGdsClssId", aResultSet.getLong("iGdsClssId"));
            aRow.put("vcFullName", aResultSet.getString("vcFullName"));
            aRow.put("vcArticul", aResultSet.getString("vcArticul"));
            aRow.put("decQnt", aResultSet.getFloat("decQnt"));
        } catch (SQLException e) {
            result = false;
        }
        return result;
    }
}
