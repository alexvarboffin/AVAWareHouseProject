package com.avaerp.form;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TStoredForm;

public class TfmKeyboard extends TStoredForm {
    TextView mValueControl;

    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_keyboard);
        mValueControl = getTextView(R.id.TfmKeyboard_lbValue);
    }

    @Override
    public void onClick(View aView) {
        switch (aView.getId()) {
            case R.id.TfmKeyboard_btClear: {
                mValueControl.setText(null);
                break;
            }
            case R.id.TfmKeyboard_btBackspace: {
                String mValue = mValueControl.getText().toString();
                if (mValue != null && mValue.length() > 0) {
                    mValueControl.setText(mValue.substring(0, mValue.length() - 1));
                }
                break;
            }
            case R.id.TfmKeyboard_btCancel: {
                setResult(RESULT_CANCELED);
                finish();
                break;
            }
            case R.id.TfmKeyboard_btOK: {
                qpGlobal("KeyboardValue").setValue(mValueControl.getText().toString());
                setResult(RESULT_OK);
                finish();
                break;
            }
            default: {
                if (aView instanceof Button) {
                    mValueControl.setText(mValueControl.getText().toString()
                            .concat(((Button) aView).getText().toString()));
                }
            }
        }
    }
}
