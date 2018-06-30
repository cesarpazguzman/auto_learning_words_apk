package com.personales.proyectos.autolearningwords.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.personales.proyectos.autolearningwords.R;

public class dialog_warning extends DialogFragment {

    private String text_warning;
    public dialog_warning(){

    }

    public static dialog_warning newInstance(String text_warning) {
        dialog_warning frag = new dialog_warning();
        frag.text_warning = text_warning;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(text_warning)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
