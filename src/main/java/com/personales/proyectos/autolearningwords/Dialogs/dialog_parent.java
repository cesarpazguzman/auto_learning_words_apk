package com.personales.proyectos.autolearningwords.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.personales.proyectos.autolearningwords.R;

public abstract class dialog_parent extends DialogFragment {

    protected int layout_width;
    protected int layout_height;
    protected int layout;

    public dialog_parent(){
        layout_height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layout_width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout, container);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        getDialog().getWindow().setLayout(layout_width, layout_height);
        getDialog().getWindow().setGravity(Gravity.CENTER);
    }
}
