package com.avaerp.util;

import com.avaerp.form.adam.TLogForm;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TSqlParser {
    private char[] mChars = {'0','1','2','3','4','5','6','7','8','9','_',
                            'a','b','c','d','e','f','g','h','i','j','k','l','m',
                            'n','o','p','q','r','s','t','u','v','w','x','y','z',
                            'A','B','C','D','E','F','G','H','I','J','K','L','M',
                            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private ArrayList<String> mParamNames = new ArrayList<String>();
    public TSqlParser() {Arrays.sort(mChars);}
    private boolean isCharInArray(char aChar, char[] aArray) {
        return Arrays.binarySearch(aArray, aChar) >= 0;
    }
    public void parse(String aSql) {
        boolean inParam = false;
        StringBuilder paramName = new StringBuilder();
        for (char c: aSql.toCharArray()) {
            boolean inChars = isCharInArray(c, mChars);
            if (inParam&inChars) {
                paramName.append(c);
                continue;
            }
            if (inParam&!inChars) {
                inParam = false;
                if (paramName.length() > 0) {
                    mParamNames.add(paramName.toString());
                    paramName.setLength(0);
                }
                continue;
            }
            if (c == ':') {
                inParam = true;
            }
        }
    }
    public void setParams(TLogForm aLogForm, CallableStatement aStatement, TParams aParams) {
        for (int idx = 1; idx <= mParamNames.size(); idx++) {
            TParams.TParam param = aParams.qp(mParamNames.get(idx - 1));
            try {
                aStatement.registerOutParameter(idx, param.getOraType());
                switch (param.getParamType()) {
                    case STRING: {aStatement.setString(idx, param.getString()); break;}
                    case INTEGER: {aStatement.setLong(idx, param.getInteger()); break;}
                    case FLOAT: {aStatement.setFloat(idx, param.getFloat()); break;}
                    case DATE: {aStatement.setDate(idx, param.getDate()); break;}
                    case BOOLEAN: {aStatement.setBoolean(idx, param.getBoolean()); break;}
                }
            } catch (SQLException e) {
                aLogForm.w("Can't register parameter %s or setting it's value %s",
                        param.getName(), param.getString(), e);
            }
        }
    }
    public void getParams(TLogForm aLogForm, CallableStatement aStatement, TParams aParams) {
        for (int idx = 1; idx <= mParamNames.size(); idx++) {
            TParams.TParam param = aParams.qp(mParamNames.get(idx - 1));
            try {
                switch (param.getParamType()) {
                    case STRING: {param.setValue(aStatement.getString(idx)); break;}
                    case INTEGER: {param.setValue(aStatement.getInt(idx)); break;}
                    case FLOAT: {param.setValue(aStatement.getFloat(idx)); break;}
                    case DATE: {param.setValue(aStatement.getDate(idx)); break;}
                    case BOOLEAN: {param.setValue(aStatement.getBoolean(idx)); break;}
                }
            } catch (SQLException e) {
                aLogForm.w("Can't getting value from statement for parameter %s", param.getName(), e);
            }
        }
    }
}
