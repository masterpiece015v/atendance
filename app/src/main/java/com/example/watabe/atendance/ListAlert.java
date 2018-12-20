package com.example.watabe.atendance;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class ListAlert {
    AlertDialog.Builder builder;
    public ListAlert( Context context ,String message,String positiveMessage) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public void show(){
        builder.show();
    }
}
