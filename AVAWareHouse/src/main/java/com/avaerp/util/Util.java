package com.avaerp.util;

import android.content.Context;

public class Util {
    public static String getString(Context context, int aId) {
        return context.getResources().getString(aId);
    }
    public static String getResName(Context context, int aId) {
        return context.getResources().getResourceName(aId);
    }
}
