package com.avaerp.util;

import android.content.Context;

public class Util {
    public static String getString(Context aContext, int aId) {
        return aContext.getResources().getString(aId);
    }
    public static String getResName(Context aContext, int aId) {
        return aContext.getResources().getResourceName(aId);
    }
}
