package com.nothing.timing.wearound.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.nothing.timing.wearound.ui.MainActivity;

public class DialogAndToast {

    public static void shortToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void executeDialog(Context context, String msg, DialogInterface.OnClickListener dialog) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setPositiveButton("Yep", dialog)
                .setNegativeButton("Nope", dialog).show();

    }
}
