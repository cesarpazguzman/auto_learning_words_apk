package com.personales.proyectos.autolearningwords.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.personales.proyectos.autolearningwords.DataBase.Tables.language;
import com.personales.proyectos.autolearningwords.DataBase.databaseManager;
import com.personales.proyectos.autolearningwords.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class create_language_dialog extends DialogFragment {

    @BindView(R.id.et_lang_name)
    EditText et_lang_name;
    @BindView(R.id.bt_save)
    Button bt_save;
    @BindView(R.id.bt_close) Button bt_close;

    public create_language_dialog(){

    }

    public static create_language_dialog newInstance() {
        create_language_dialog frag = new create_language_dialog();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_language_dialog, container);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private void save(){

        Map<String, Object> map_vals = new HashMap<>();
        map_vals.put(language.col.NAME, et_lang_name.getText().toString().toUpperCase());

        databaseManager.getInstance(getContext()).insert(language.NAME_TABLE, map_vals);
        // Close the dialog and return back to the parent activity
        dismiss();
    }
}
