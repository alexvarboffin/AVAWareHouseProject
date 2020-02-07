package com.avaerp.util;

import android.util.Log;

public class Hash {
    private char[] masKeys = {'q', 'C', 'b', '%', 'V', 'u', 'T', 'z', 'W', 'L',
                      'Y', 'y', 'l', 'Z', 's', 's', 'n', 'p', 'p', 'q'};
    private String[] masSymbols = {"$o", "p", "bh", "_!", "a_", "bV", "J)", "Df",  "oA", "N!",
                           "lK", "7%", "y", "Y", "?D", "VK", "s", "d", "Rs", "e$"};

    public String encode(final String aValue) {
        String TAG = "Hash";
        Log.d(TAG, String.format("Encoding user name %s", aValue));
        StringBuilder userName = new StringBuilder(aValue);
        int nc = 0;
        while (userName.length() < 14) {
            int p1 = (int) userName.charAt(5)/20;
            if (p1 + nc > 20) {p1 = p1 - nc;}
            int p2 = (int) userName.charAt(4)/20;
            if (p2 + nc > 20) {p2 = p2 - nc;}
            int p3 = (int) userName.charAt(3)/20;
            if (p3 + nc > 20) {p3 = p3 - nc;}
            userName.append(masKeys[p1 + nc - 1]).append(masKeys[p2 + nc - 1]).append(masKeys[p3 + nc - 1]);
            nc++;
        }
        userName.reverse();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < userName.length(); i++) {
            result.append(userName.substring(i, i + 1)).append(masSymbols[i]);
        }
        if (result.length() > 21) {
            result.delete(1, 4).setLength(24);
        }
        Log.d(TAG, String.format("Encoded user name is %s", result.toString()));
        return result.toString();
    }
}
