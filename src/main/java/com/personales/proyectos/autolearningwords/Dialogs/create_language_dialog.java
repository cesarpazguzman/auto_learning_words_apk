package com.personales.proyectos.autolearningwords.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
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

public class create_language_dialog extends dialog_parent {

    @BindView(R.id.et_lang_name) EditText et_lang_name;
    @BindView(R.id.bt_save) Button bt_save;
    @BindView(R.id.bt_close) Button bt_close;
    @BindView(R.id.til_lang_name) TextInputLayout til_lang_name;

    public create_language_dialog(){
        super();
        layout = R.layout.create_language_dialog;
    }

    public static create_language_dialog newInstance() {
        create_language_dialog frag = new create_language_dialog();
        return frag;
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

        if(et_lang_name== null || et_lang_name.getText().toString().equals("")){
            til_lang_name.setError("*Campo obligatorio");
            return;
        }

        Map<String, Object> map_vals = new HashMap<>();
        map_vals.put(language.col.NAME, et_lang_name.getText().toString().toUpperCase());

        databaseManager.getInstance(getContext()).insert(language.NAME_TABLE, map_vals);
        // Close the dialog and return back to the parent activity
        dismiss();
    }
}
