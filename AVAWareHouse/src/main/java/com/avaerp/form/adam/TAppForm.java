package com.avaerp.form.adam;

import android.app.Activity;
import android.os.Bundle;

import com.avaerp.apps.warehouse.TApplication;

public abstract class TAppForm extends Activity {
    private TApplication mApp = null;

    public TApplication getApp() {
        return mApp;
    }

    private String mName = null;

    public String getName() {
        return mName;
    }

    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        if (getApplication() instanceof TApplication) {
            mApp = (TApplication) getApplication();
            mName = "@@@" + getClass().getName();
        }
    }
}
