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
        int id = aView.getId();
        if (id == R.id.TfmKeyboard_btClear) {
            mValueControl.setText(null);
        } else if (id == R.id.TfmKeyboard_btBackspace) {
            String mValue = mValueControl.getText().toString();
            if (mValue != null && !mValue.isEmpty()) {
                mValueControl.setText(mValue.substring(0, mValue.length() - 1));
            }
        } else if (id == R.id.TfmKeyboard_btCancel) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.TfmKeyboard_btOK) {
            qpGlobal("KeyboardValue").setValue(mValueControl.getText().toString());
            setResult(RESULT_OK);
            finish();
        } else {
            if (aView instanceof Button) {
                mValueControl.setText(mValueControl.getText().toString()
                        .concat(((Button) aView).getText().toString()));
            }
        }
    }
}
