package com.avaerp.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Будда on 10.07.2015.
 */
public class TOfflineInAdapter extends ArrayAdapter<HashMap<String, Object>> {
    private Context mContext;
    private int mLayoutId_Row;
    public TOfflineInAdapter(Context aContext, int aResId, ArrayList<HashMap<String, Object>> aItems, int aLayoutId_Row) {
        super(aContext, aResId, aItems);
        setNotifyOnChange(true);
        mContext = aContext;
        mLayoutId_Row = aLayoutId_Row;
    }
    private void setValue(View aView, int aCtrlId, HashMap<String, Object> aData, String aName) {
        TextView view = (TextView) aView.findViewById(aCtrlId);
        if (view != null) {
            Object text = aData.get(aName);
            if (text != null) {
                view.setText(text.toString());
            }
        }
    }
    public View getView(int aPosition, View aView, ViewGroup aParent) {
        View view = aView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mLayoutId_Row, null);
        }
        HashMap<String, Object> item = getItem(aPosition);
        if (item != null) {
            setValue(view, R.id.TfmOfflineIn_Row_Serials, item, "vcSerials");
            setValue(view, R.id.TfmOfflineIn_Row_WhZone, item, "vcWhZone");
            setValue(view, R.id.TfmOfflineIn_Row_Result, item, "vcResult");
            if (item.get("vcResult") == null) {
                view.findViewById(R.id.TfmOfflineIn_Row_Result).setVisibility(View.GONE);
                view.setBackgroundColor(Color.WHITE);
            } else {
                view.findViewById(R.id.TfmOfflineIn_Row_Result).setVisibility(View.VISIBLE);
                if (item.get("vcResult").equals("OK")) {
                    view.setBackgroundColor(Color.GREEN);
                } else if (!item.get("vcResult").equals("")) {
                     view.setBackgroundColor(Color.RED);
                }
            }
            view.findViewById(R.id.TfmOfflineIn_Row_btDelRow).setTag(aPosition);
        }
        return view;
    }
}
