package com.avaerp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Будда on 06.07.2014.
 */
public class TDataAdapter extends ArrayAdapter<HashMap<String, Object>> {
    private Context mContext;
    private int mLayoutId_Row;
    private TParams mFieldMap;
    public int mDelBtnId;
    public TDataAdapter(Context aContext, int aResId, ArrayList<HashMap<String, Object>> aItems, int aLayoutId_Row, TParams aFieldMap) {
        super(aContext, aResId, aItems);
        setNotifyOnChange(true);
        mContext = aContext;
        mLayoutId_Row = aLayoutId_Row;
        mFieldMap = aFieldMap;
    }
    public View getView(int aPosition, View aView, ViewGroup aParent) {
        View view = aView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mLayoutId_Row, null);
        }
        HashMap<String, Object> item = getItem(aPosition);
        if (item != null) {
            for (String fieldName: item.keySet()) {
                int controlId = mFieldMap.qp(fieldName).getInteger();
                if (controlId != 0) {
                    TextView textView = (TextView) view.findViewById(controlId);
                    if (textView != null) {
                        if (item.get(fieldName) != null) {
                            textView.setText(item.get(fieldName).toString());
                        } else {
                            textView.setVisibility(View.GONE);
                        }
                    }
                }
                if (fieldName == "iMobWhScanId") {
                    Button button = (Button) view.findViewById(mDelBtnId);//R.id.TfmFilling_Row_btDelRow);
                    if (button != null) {
                        button.setTag(item.get(fieldName));
                    }
                }
            }
        }
        return view;
    }
}
