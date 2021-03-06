package com.personales.proyectos.autolearningwords.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class AlertDialogHelper {
    private Context context;
    private AlertDialog alertDialog=null;
    private AlertDialogListener callBack;
    private Activity current_activity;

    public AlertDialogHelper(Context context)
    {
        this.context = context;
        this.current_activity = (Activity) context;
        callBack = (AlertDialogListener) context;
    }

    public void showAlertDialog(String title,String message,String positive,String negative,
                                String neutral,final int from,boolean isCancelable, final View view)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(current_activity);

        if(!TextUtils.isEmpty(title))
            alertDialogBuilder.setTitle(title);
        if(!TextUtils.isEmpty(message))
            alertDialogBuilder.setMessage(message);

        if(view!=null){
            alertDialogBuilder.setView(view);
        }
        if(!TextUtils.isEmpty(positive)) {
            alertDialogBuilder.setPositiveButton(positive,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            callBack.onPositiveClick(from, view);
                            alertDialog.dismiss();
                        }
                    });
        }
        if(!TextUtils.isEmpty(neutral)) {
            alertDialogBuilder.setNeutralButton(neutral,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            callBack.onNeutralClick(from);
                            alertDialog.dismiss();
                        }
                    });
        }
        if(!TextUtils.isEmpty(negative))
        {
            alertDialogBuilder.setNegativeButton(negative,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            callBack.onNegativeClick(from);
                            alertDialog.dismiss();
                        }
                    });
        }
        else
        {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Button negative_button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        negative_button.setVisibility(View.GONE);

                        Looper.loop();

                    }
                }.start();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        alertDialogBuilder.setCancelable(isCancelable);


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public interface AlertDialogListener
    {
        public void onPositiveClick(int from, View view);
        public void onNegativeClick(int from);
        public void onNeutralClick(int from);
    }

}