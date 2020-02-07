package com.avaerp.util;

import com.avaerp.form.adam.TLogForm;

import java.sql.Date;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import oracle.sql.BLOB;

public class TParams {
    public enum TParamType {STRING, INTEGER, FLOAT, DATE, BOOLEAN}

    public class TParam {
        private String mName;
        private String mValue;
        private TParamType mParamType;

        public TParamType getParamType() {
            return mParamType;
        }

        public int getOraType() {
            switch (mParamType) {
                case INTEGER: {
                    return Types.INTEGER;
                }
                case FLOAT: {
                    return Types.FLOAT;
                }
                case DATE: {
                    return Types.DATE;
                }
                case BOOLEAN: {
                    return Types.BOOLEAN;
                }
                default: {
                    return Types.VARCHAR;
                }
            }
        }

        public TParam(String aName) {
            mName = aName;
            setValue("");
        }

        public String getName() {
            return mName;
        }

        public void setValue(String aValue, TParamType aType) {
            setValue(aValue);
            mParamType = aType;
        }

        public void setValue(String aValue) {
            mValue = aValue;
            mParamType = TParamType.STRING;
        }

        public void setValue(Integer aValue) {
            setValue(Integer.toString(aValue));
            mParamType = TParamType.INTEGER;
        }

        public void setValue(Long aValue) {
            setValue(aValue.intValue());
        }

        public void setValue(Float aValue) {
            setValue(Float.toString(aValue));
            mParamType = TParamType.FLOAT;
        }

        public void setValue(Date aValue) {
            setValue(new SimpleDateFormat().format(aValue));
            mParamType = TParamType.DATE;
        }

        public void setValue(Boolean aValue) {
            setValue(Boolean.toString(aValue));
            mParamType = TParamType.BOOLEAN;
        }

        public String getString() {
            return mValue;
        }

        public Integer getInteger() {
            return isNull() ? 0 : Integer.parseInt(mValue);
        }

        public Float getFloat() {
            return isNull() ? 0 : Float.parseFloat(mValue);
        }

        public Date getDate() {
            return isNull() ? new Date(0) : Date.valueOf(mValue);
        }

        public Boolean getBoolean() {
            return isNull() ? false : Boolean.valueOf(mValue);
        }

        public boolean isNull() {
            return mValue.equals("");
        }
    }

    private ArrayList<TParam> mParams = new ArrayList<TParam>();

    public TParams() {
    }

    public TParams(TLogForm aLogForm, Object... aParams) {
        int idx = 0;
        while (idx + 1 < aParams.length) {
            String name = aParams[idx].toString();
            Object value = aParams[idx + 1];
            if (value instanceof String) {
                qp(name).setValue((String) value);
            } else if (value instanceof Integer) {
                qp(name).setValue((Integer) value);
            } else if (value instanceof Float) {
                qp(name).setValue((Float) value);
            } else if (value instanceof Date) {
                qp(name).setValue((Date) value);
            } else if (value instanceof Boolean) {
                qp(name).setValue((Boolean) value);
            } else {
                aLogForm.w("Parameter %s has value of unsupported type", name);
            }
            idx = +2;
        }
        if (idx != aParams.length) {
            aLogForm.w("Number of parameter names is not equal to number of values");
        }
    }

    public TParam qp(String aName) {
        TParam result = null;
        for (TParam param : mParams) {
            if (param.mName.contentEquals(aName)) {
                result = param;
                break;
            }
        }
        if (result == null) {
            result = new TParam(aName);
            mParams.add(result);
        }
        return result;
    }

    public int count() {
        return mParams.size();
    }

    public TParam getParam(int aIndex) {
        return mParams.get(aIndex);
    }

    @Override
    public String toString() {
        return "TParams{" +
                "mParams=" + mParams +
                '}';
    }
}