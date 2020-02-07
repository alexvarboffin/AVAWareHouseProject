package com.avaerp.form.adam;

import android.os.Bundle;
import android.util.Log;

public abstract class TLogForm extends TAppForm {

    private String format(String aMessage, Object... aParams) {
        return String.format(aMessage, aParams);
    }

    public int d(String aMessage, Object... aParams) {
        return Log.d(getName(), format(aMessage, aParams));
    }

    // * Is never used
    //public int d(String aMessage, Throwable aError, Object... aParams) {
    //    return Log.d(getName(), format(aMessage, aParams), aError);
    //}
    public int e(String aMessage, Object... aParams) {
        return Log.e(getName(), format(aMessage, aParams));
    }

    public int e(String aMessage, Throwable aError, Object... aParams) {
        return Log.e(getName(), format(aMessage, aParams), aError);
    }

    // * Is never used
    //public int i(String aMessage, Object... aParams) {
    //    return Log.i(getName(), format(aMessage, aParams));
    //}
    // * Is never used
    //public int i(String aMessage, Throwable aError, Object... aParams) {
    //    return Log.i(getName(), format(aMessage, aParams), aError);
    //}
    public int v(String aMessage, Object... aParams) {
        return Log.v(getName(), format(aMessage, aParams));
    }

    // * Is never used
    //public int v(String aMessage, Throwable aError, Object... aParams) {
    //    return Log.v(getName(), format(aMessage, aParams), aError);
    //}
    public int w(String aMessage, Object... aParams) {
        Throwable error = null;
        if (aParams.length > 0 && aParams[aParams.length - 1] instanceof Throwable) {
            error = (Throwable) aParams[aParams.length - 1];
        }
        if (error != null) {
            return Log.w(getName(), format(aMessage, aParams), error);
        } else {
            return Log.w(getName(), format(aMessage, aParams));
        }
    }

    // Is never used
    //public int w(Throwable aError) {
    //    return Log.w(getName(), aError);
    //}
    // * Is never used
    //public int w(String aMessage, Throwable aError, Object... aParams) {
    //    return Log.w(getName(), format(aMessage, aParams), aError);
    //}
    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        d("Form %s created", getName());
    }
}
