package com.avaerp.form;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.core.content.FileProvider;

import com.avaerp.apps.warehouse.BuildConfig;
import com.avaerp.apps.warehouse.R;
import com.avaerp.form.adam.TDBForm;
import com.avaerp.util.TParams;
import com.avaerp.util.TSqlExecutor;
import com.avaerp.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TfmLogin extends TDBForm {

    private static final int KEY_UPDATE = 8814;
    private static final String TAG = "@@@";

    private static final boolean SHOW_DEMO_DATA = BuildConfig.DEBUG;
    public static final String KEY_SETTINGS = "xxx";


    @Override
    public void onCreate(Bundle aState) {
        super.onCreate(aState);
        setContentView(R.layout.tfm_login);
        String versionName = getApp().getVersion();
        String versionCaption = Util.getString(this, R.string.fmLogin_lbVersion);
        getTextView(R.id.TfmLogin_lbVersion).setText(String.format(versionCaption, versionName));
        setRequires(getTextView(R.id.TfmLogin_edStaffId), getTextView(R.id.TfmLogin_edPassword),
                getTextView(R.id.TfmLogin_edHostName), getTextView(R.id.TfmLogin_edPort),
                getTextView(R.id.TfmLogin_edSid));
        loadControls();
    }

    private void saveControls() {

        SharedPreferences prefs = getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_name", getTextViewText(R.id.TfmLogin_edStaffId));
        editor.putString("password", getTextViewText(R.id.TfmLogin_edPassword));
        editor.putString("host_name", getTextViewText(R.id.TfmLogin_edHostName));
        editor.putString("port", getTextViewText(R.id.TfmLogin_edPort));
        editor.putString("sid", getTextViewText(R.id.TfmLogin_edSid));
        editor.putBoolean("options", getCheckBox(R.id.TfmLogin_cbParams).isChecked());
        editor.apply();




//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("user_name", getTextViewText(R.id.TfmLogin_edStaffId));
//        Log.d(TAG, "@@@@12 : " + prefs1.getString("user_name", ""));
    }

    private void loadControls() {
        SharedPreferences preferences = getSharedPreferences(KEY_SETTINGS, MODE_PRIVATE);
        getTextView(R.id.TfmLogin_edStaffId).setText(preferences.getString("user_name",
                (SHOW_DEMO_DATA) ? (getString(R.string.prefix_staff_id) + "0") : ""
        ));
        getTextView(R.id.TfmLogin_edPassword).setText(preferences.getString("password",

                (SHOW_DEMO_DATA) ? "00000000000" : ""
        ));
        getTextView(R.id.TfmLogin_edHostName).setText(preferences.getString("host_name",
                (SHOW_DEMO_DATA) ? "127.0.0.1" : ""
        ));
        getTextView(R.id.TfmLogin_edPort).setText(preferences.getString("port",
                (SHOW_DEMO_DATA) ? "1521" : ""
        ));
        getTextView(R.id.TfmLogin_edSid).setText(preferences.getString("sid",
                (SHOW_DEMO_DATA) ? "orcl" : ""
        ));
        getCheckBox(R.id.TfmLogin_cbParams).setChecked(preferences.getBoolean("options", true));
        onClick(getView(R.id.TfmLogin_cbParams));
    }


    private void login() {
        getApp().initialize(this);
        getApp().getLoginInfo().setValues(getTextViewText(R.id.TfmLogin_edStaffId),
                getTextViewText(R.id.TfmLogin_edHostName),
                getTextViewText(R.id.TfmLogin_edPort),
                getTextViewText(R.id.TfmLogin_edSid));
        qp("iStaffId").setValue(getUserID());
        String password = getTextViewText(R.id.TfmLogin_edPassword);
        qp("vcPassword").setValue(password);
        qp("iResult").setValue(0);
        executeSql(R.string.sql_check_user_password);
    }

    private void validateVersion() {
        qp("vcVersion").setValue(getApp().getVersion());
        say(R.string.fmLogin_Updating);
        querySql(R.string.sql_get_version_no);
    }

    protected boolean checkRequires() {
        boolean result = super.checkRequires();
        String staffId = getTextViewText(R.id.TfmLogin_edStaffId);
        if (result && (staffId.length() > 8 || staffId.length() == 0 || staffId.equals("0"))) {
            result = false;
            say(R.string.error_wrong_user_name);
        }
        return result;
    }

    @Override
    public void onClick(View aView) {
        super.onClick(aView);
        switch (aView.getId()) {
            case R.id.TfmLogin_cbParams: {
                getView(R.id.TfmLogin_pnParams).setVisibility(getCheckBox(aView).isChecked() ? View.VISIBLE : View.GONE);
                break;
            }
            case R.id.TfmLogin_btConnect: {
                if (checkRequires()) {
                    saveControls();
                    login();
                } else if (!getCheckBox(R.id.TfmLogin_cbParams).isChecked()) {
                    if ((getTextViewText(R.id.TfmLogin_edHostName).length() == 0) ||
                            (getTextViewText(R.id.TfmLogin_edPort).length() == 0) ||
                            (getTextViewText(R.id.TfmLogin_edSid).length() == 0)) {
                        getCheckBox(R.id.TfmLogin_cbParams).setChecked(true);
                        onClick(getView(R.id.TfmLogin_cbParams));
                    }
                }
                break;
            }
        }
    }

    @Override
    protected boolean onKeyDown(View aView, int aKeyCode) {
        if (aView.getId() == R.id.TfmLogin_edStaffId && aKeyCode == KeyEvent.KEYCODE_ENTER) {
            onClick(getView(R.id.TfmLogin_btConnect));
            return true;
        }
        return false;
    }

    @Override
    public void onSqlExecuted(int aSqlId, TParams aParams, TSqlExecutor.TExecuteStatus aStatus) {
        if (aStatus == TSqlExecutor.TExecuteStatus.SUCCESSFUL) {
            switch (aSqlId) {
                case R.string.sql_check_user_password: {
                    if (qp("iResult").getInteger() > 0) {
                        qpGlobal("isConnectedToDB").setValue(true);
                        validateVersion();
                    } else {
                        say(R.string.error_wrong_password);
                    }
                    break;
                }
                default: {
                    super.onSqlExecuted(aSqlId, aParams, aStatus);
                }
            }
        } else {
            super.onSqlExecuted(aSqlId, aParams, aStatus);
        }
    }

    @Override
    public void afterSqlQuery(int aSqlId, TParams aParams, ResultSet resultSet) {
        switch (aSqlId) {
            case R.string.sql_get_version_no: {
                try {
                    boolean value = resultSet.next();
                    if (!value || !needUpdate(resultSet)) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        updateVersion();
                    }
                } catch (SQLException e) {
                    e("Error while checking version");
                    sayLong(R.string.error_cannot_check_version);
                }
                break;
            }
            case R.string.sql_get_version: {
                try {
                    if (resultSet.next()) {
                        InputStream input = resultSet.getBlob("bApp").getBinaryStream();
                        File dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(dir, getApp().getPackageName() + "_update.apk");
                        FileOutputStream output = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = input.read(buffer)) != -1) {
                            output.write(buffer, 0, len);
                        }
                        output.close();
                        input.close();

                        //Uri uri = Uri.fromFile(file);
                        Uri uri = FileProvider.getUriForFile(this,
                                this.getApplicationContext().getPackageName()
                                        + ".provider", file);

                        if (file.exists()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "application/vnd.android.package-archive");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, KEY_UPDATE);
                        }

                        Log.d(TAG, "afterSqlQuery: " + uri
                                + " ### " + file.exists()
                                + " ### " + file.length());
                    }
                } catch (SQLException e) {
                    e("Error (SQL) updating version", e);
                    sayLong(R.string.error_cannot_load_version);
                } catch (IOException e) {
                    e("Error (IO) updating version", e);
                    sayLong(R.string.error_cannot_load_version);
                }
            }
        }
    }

    private boolean needUpdate(ResultSet resultSet) throws SQLException {
        //VERSION_CODE инкрементится при каждой сборке gradl'ом
        //Текущая версия в файле version.properties
        Log.d(TAG, "needUpdate: " + BuildConfig.VERSION_CODE + " " + BuildConfig.VERSION_NAME);
        return BuildConfig.VERSION_CODE < 0000 /* resultSet(GET_VERSION) */;
    }

    private void updateVersion() {
        querySql(R.string.sql_get_version);
    }

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData) {
        super.onActivityResult(aRequestCode, aResultCode, aData);
        Log.d(TAG, "onActivityResult: " + ((aData == null) ? null : aData.toString()));
    }
}
