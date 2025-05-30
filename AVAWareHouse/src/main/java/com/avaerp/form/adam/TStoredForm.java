package com.avaerp.form.adam;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.util.TParams;
import com.avaerp.util.Util;

import java.util.ArrayList;

public class TStoredForm extends TControlForm {

    private final TParams mParams = new TParams();

    protected TParams getParams0() {
        return mParams;
    }

    protected TParams.TParam qp(String aParamName) {
        return mParams.qp(aParamName);
    }

    protected TParams.TParam qpGlobal(String aParamName) {
        return getApp().qp(aParamName);
    }

    private final ArrayList<TextView> mRequires = new ArrayList<TextView>();

    protected void setRequires(TextView... aTextViews) {
        mRequires.clear();
        d("Requires cleaned");
        addRequires(aTextViews);
    }

    protected void addRequires(TextView... aTextViews) {
        for (TextView view : aTextViews) {
            mRequires.add(view);
            d("TextView %s marked as required", view.toString());
        }
    }

    protected boolean checkRequires() {
        boolean result = true;
        v("Checking if all of required TextViews have text");
        for (TextView view : mRequires) {
            if (getTextViewText(view).isEmpty()) {
                view.setError(Util.getString(this, R.string.message_field_is_required));
                result = false;
            }
        }
        v("%s of required TextView have text", result ? "All" : "Not all");
        return result;
        //String paramName = Util.getResName(this, R.integer.enum_required_views) ;
        //TParams result = new TParams(this, paramName, true);
        //enumViews(R.integer.enum_required_views, result);
        //v("%s of required TextView have text", result.qp(paramName).getBoolean()? "All": "Not all");
        //return result.qp(paramName).getBoolean();
    }

    @Override
    protected boolean onEnumViews(int aEnumId, View aView, TParams aData) {
        return super.onEnumViews(aEnumId, aView, aData);
        /*boolean result = super.onEnumViews(aEnumId, aView, aData);
        switch (aEnumId) {
            case R.integer.enum_required_views: {
                TextView view = aView instanceof TextView ? (TextView) aView : null;
                if (view != null && mRequires.contains(view) && view.getText() != null && view.getText().length() == 0) {
                    aData.qp(Util.getResName(this, R.integer.enum_required_views)).setValue(false);
                    view.setError(Util.getString(this, R.string.message_field_is_required));
                    v("Required view %s is empty", view.toString());
                } else {
                    if (view != null) {
                        view.setError(null);
                    }
                }
            }
        }
        return result;*/
    }

    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String[] keys = new String[bundle.keySet().size()];
            bundle.keySet().toArray(keys);
            for (int i = 0; i < keys.length; i++) {
                TParams.TParamType[] types = TParams.TParamType.values();
                for (int j = 0; j < types.length; j++) {
                    if (keys[i].startsWith(types[j].name().concat("$"))) {
                        qp(keys[i].replace(types[j].name().concat("$"), "")).setValue(bundle.getString(keys[i]), types[j]);
                        break;
                    }
                }
            }
        }

    }
}
