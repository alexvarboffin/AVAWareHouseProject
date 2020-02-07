package com.avaerp.form.adam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.avaerp.util.Util;

public class TInteractiveForm extends TLogForm {
    private int idYesNo;
    protected final void say(String aMessage, boolean aIsLong) {
        Toast.makeText(this, aMessage, aIsLong? Toast.LENGTH_SHORT: Toast.LENGTH_SHORT).show();
    }
    protected final void say(String aMessage) {
        say(aMessage, false);
    }
    protected final void say(int aMessageId) {
        say(Util.getString(this, aMessageId));
    }
    protected final void sayLong(String aMessage) {
        say(aMessage, true);
    }
    protected final void sayLong(int aMessageId) {
        sayLong(Util.getString(this, aMessageId));
    }

    protected final void sayModal(String aMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Сообщение");
        builder.setMessage(aMessage);
        builder.setCancelable(true);
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    protected void onYesNo(int aDialogId, boolean aResult) {
    }
    protected final void yesNo(int aDialogId, String aMessage) {
        idYesNo = aDialogId;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вопрос");
        builder.setMessage(aMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TInteractiveForm.this.onYesNo(TInteractiveForm.this.idYesNo, true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TInteractiveForm.this.onYesNo(TInteractiveForm.this.idYesNo, false);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
