package com.avaerp.form.adam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.avaerp.apps.warehouse.R;
import com.avaerp.form.TfmKeyboard;
import com.avaerp.util.TParams;
import com.avaerp.util.Util;

import java.util.Set;

public abstract class TControlForm extends TInteractiveForm {

    private final class TClickListener implements View.OnClickListener {
        public void onClick(View aView) {
            TControlForm.this.v("View %s clicked", aView.toString());
            TControlForm.this.onClick(aView);
        }
    }

    private final TClickListener mClickListener = new TClickListener();

    private final class TKeyListener implements View.OnKeyListener {
        public boolean onKey(View aView, int aKeyCode, KeyEvent aKeyEvent) {
            return (aKeyEvent.getAction() == KeyEvent.ACTION_DOWN && onKeyDown(aView, aKeyCode));
        }
    }

    private final TKeyListener mKeyListener = new TKeyListener();

    private final class TFocusListener implements View.OnFocusChangeListener {
        public void onFocusChange(View aView, boolean aHasFocus) {
            onFocusChanged(aView, aHasFocus);
        }
    }

    private final TFocusListener mFocusListener = new TFocusListener();

    private final class TEditorActionListener implements TextView.OnEditorActionListener {
        public boolean onEditorAction(TextView aTextView, int aActionId, KeyEvent aEvent) {
            return aActionId != EditorInfo.IME_NULL && onSystemKey(aTextView, aActionId);
        }
    }

    private final TEditorActionListener mEditorActionListener = new TEditorActionListener();

    private final class TListItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> aListView, View aView, int aPosition, long aId) {
            if (aListView instanceof ListView) {
                onListItemClick((ListView) aListView, aView, aPosition, aId);
            }
        }
    }

    private final TListItemClickListener mListItemClickListener = new TListItemClickListener();

    protected void setListeners() {
        enumViews(R.integer.enum_set_listeners);
    }

    protected void setViewProps() {
        enumViews(R.integer.enum_set_view_props);
    }

    protected void onClick(View aView) {
    }

    protected boolean onKeyDown(View aView, int aKeyCode) {
        return false;
    }

    protected boolean onSystemKey(TextView aView, int aActionId) {
        return false;
    }

    protected void onFocusChanged(View aView, boolean aHasFocus) {
    }

    protected void onListItemClick(ListView aListView, View aView, int aPosition, long aId) {
    }

    protected final void enumViews(int aEnumId) {
        enumViews(aEnumId, null);
    }

    protected final void enumViews(int aEnumId, TParams aData) {
        v("Enumerating views %s started", Util.getResName(this, aEnumId));
        enumViews(aEnumId, getWindow().getDecorView(), aData);
        v("Enumerating views %s finished", Util.getResName(this, aEnumId));
    }

    private boolean enumViews(int aEnumId, View aView, TParams aData) {
        v("Enumerating view %s for %s", aView.toString(), Util.getResName(this, aEnumId));
        boolean result = onEnumViews(aEnumId, aView, aData);
        if (result) {
            ViewGroup viewGroup = aView instanceof ViewGroup ? (ViewGroup) aView : null;
            if (viewGroup != null) {
                for (int idx = 0; idx < viewGroup.getChildCount(); idx++) {
                    result = enumViews(aEnumId, viewGroup.getChildAt(idx), aData);
                    if (!result) {
                        break;
                    }
                }
            }
        } else {
            v("Enumerating views %s aborted by view %s", Util.getResName(this, aEnumId), aView.toString());
        }
        return result;
    }

    protected boolean onEnumViews(int aEnumId, View aView, TParams aData) {
        switch (aEnumId) {
            case R.integer.enum_set_listeners: {
                if (aView instanceof AdapterView) {
                    ((AdapterView) aView).setOnItemClickListener(mListItemClickListener);
                } else {
                    aView.setOnClickListener(mClickListener);
                }
                aView.setOnKeyListener(mKeyListener);
                aView.setOnFocusChangeListener(mFocusListener);
                if (aView instanceof TextView) {
                    ((TextView) aView).setOnEditorActionListener(mEditorActionListener);
                    ((TextView) aView).setImeOptions(((TextView) aView).getImeOptions() + EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                }
            }
            case R.integer.enum_set_view_props: {
                if (aView instanceof TextView) {
                    TextView view = (TextView) aView;
                    /** Don't expand view while editing in landscape mode */
                    view.setImeOptions(view.getImeOptions() + EditorInfo.IME_FLAG_NO_FULLSCREEN);
                }
            }
        }
        return true;
    }

    protected View getView(int aViewId) {
        return findViewById(aViewId);
    }

    protected TextView getTextView(int aViewId) {
        return getTextView(getView(aViewId));
    }

    protected TextView getTextView(View aView) {
        return aView instanceof TextView ? (TextView) aView : null;
    }

    protected CheckBox getCheckBox(int aViewId) {
        return getCheckBox(getView(aViewId));
    }

    protected CheckBox getCheckBox(View aView) {
        return aView instanceof CheckBox ? (CheckBox) aView : null;
    }

    protected EditText getEditText(int aViewId) {
        return getEditText(getView(aViewId));
    }

    protected EditText getEditText(View aView) {
        return aView instanceof EditText ? (EditText) aView : null;
    }

    protected Button getButton(int aViewId) {
        return getButton(getView(aViewId));
    }

    protected Button getButton(View aView) {
        return aView instanceof Button ? (Button) aView : null;
    }

    protected ListView getListView(int aViewId) {
        return getListView(getView(aViewId));
    }

    protected ListView getListView(View aView) {
        return aView instanceof ListView ? (ListView) aView : null;
    }

    protected String getTextViewText(int aViewId) {
        TextView view = getTextView(aViewId);
        return getTextViewText(view);
    }

    protected String getTextViewText(TextView aView) {
        return (aView != null && aView.getText() != null) ? aView.getText().toString() : "";
    }

    protected void setTextViewText(int aViewId, String aValue) {
        TextView view = getTextView(aViewId);
        setTextViewText(view, aValue);
    }

    protected void setTextViewText(TextView aView, String aValue) {
        if (aView != null) {
            aView.setText(aValue);
        }
    }

    protected void showForm(Class aClass, boolean aIsModal, TParams aParams) {
        Intent intent = new Intent(this, aClass);
        if (aParams != null) {
            for (int i = 0; i < aParams.count(); i++) {
                TParams.TParam param = aParams.getParam(i);
                intent.putExtra(param.getParamType().name().concat("$").concat(param.getName()), param.getString());
            }
        }
        if (aIsModal) {
            startActivityForResult(intent, aClass.hashCode());
        } else {
            startActivity(intent);
        }
    }

    protected void showForm(Class aClass, boolean aIsModal) {
        showForm(aClass, aIsModal, null);
    }

    protected void onModalForm(int aCode, boolean aResultOk) {
    }

    protected void onKeyboardClose(String aValue) {
    }

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData) {
        onModalForm(aRequestCode, aResultCode == RESULT_OK);
    }

    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onContentChanged() {
        v("Content changed");
        setListeners();
        setViewProps();
    }

    protected void selectAllText(EditText aEditText) {
        if (aEditText.getText() != null) {
            aEditText.setSelection(0, aEditText.getText().length());
        }
    }

    protected void showKeyboard(boolean aVisible) {
        showForm(TfmKeyboard.class, true, null);
        Context ctx = getApplicationContext();
        InputMethodManager imm = null;
        if (ctx != null) {
            imm = (InputMethodManager) ctx.getSystemService(INPUT_METHOD_SERVICE);
        }
        if (imm != null) {
            if (aVisible) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            }
        }
    }
}
