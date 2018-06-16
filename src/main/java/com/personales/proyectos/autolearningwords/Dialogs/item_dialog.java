package com.personales.proyectos.autolearningwords.Dialogs;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import com.personales.proyectos.autolearningwords.Base.SwipeHelper;
import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.Tables.item;
import com.personales.proyectos.autolearningwords.DataBase.databaseManager;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class item_dialog extends DialogFragment {
    public interface ItemDialogListener {
        void onSaveItemDialog(String original, String traduccion, String example_1,
                              String example_2, String comment, boolean open_again);
    }


    @BindView(R.id.add_example_item) ImageButton add_example_item;
    @BindView(R.id.et_item_original) EditText et_item_original;
    @BindView(R.id.et_item_traduccion) EditText et_item_traduccion;
    @BindView(R.id.et_example_one) EditText et_example_one;
    @BindView(R.id.et_example_two) EditText et_example_two;
    @BindView(R.id.et_comments) EditText et_comments;
    @BindView(R.id.bt_save_close) Button bt_save_close;
    @BindView(R.id.bt_save_new) Button bt_save_new;
    @BindView(R.id.bt_close_item) Button bt_close_item;

    //true if item is being modified, false if item is being created
    private boolean new_item = true;
    private InputMethodManager imm;

    public item_dialog(){

    }

    public static item_dialog newInstance(String original, String traduccion, String example_1,
                                          String example_2, String comment) {
        item_dialog frag = new item_dialog();
        frag.new_item = false;
        Bundle args = new Bundle();
        args.putString("original", original );
        args.putString("traduccion", traduccion);
        args.putString("example_1", example_1);
        args.putString("example_2", example_2);
        args.putString("comment", comment);
        frag.setArguments(args);
        return frag;
    }

    public static item_dialog newInstance() {
        item_dialog frag = new item_dialog();
        frag.new_item = true;
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_item, container);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.TOP);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        add_example_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_example_two.setVisibility(View.VISIBLE);
                et_example_two.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                add_example_item.setVisibility(View.GONE);
            }
        });

        bt_save_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(false);
            }
        });

        bt_save_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(true);
            }
        });

        bt_close_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        if(!new_item){
            fill_values();
        }else{
            et_item_original.requestFocus();
        }

        if(!et_example_two.getText().toString().isEmpty()){
            et_example_two.setVisibility(View.VISIBLE);
            add_example_item.setVisibility(View.GONE);
        }


        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private void save(boolean open_again){
        String comment = !et_comments.getText().toString().isEmpty() ? et_comments.getText().toString() : "";
        String example1 = !et_example_one.getText().toString().isEmpty() ? et_example_one.getText().toString() : "";
        String example2 = !et_example_two.getText().toString().isEmpty() ? et_example_two.getText().toString() : "";

        ItemDialogListener listener = (ItemDialogListener) getActivity();
        if(new_item){
            listener.onSaveItemDialog(et_item_original.getText().toString(), et_item_traduccion.getText().toString(),
                    example1, example2, comment, open_again);
        }else{
            //HACER EDITAR
        }

        // Close the dialog and return back to the parent activity
        dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    private void fill_values(){
        String original = getArguments().getString("original");
        String traduccion = getArguments().getString("traduccion");
        String example_1 = getArguments().getString("example_1");
        String example_2 = getArguments().getString("example_2");
        String comment = getArguments().getString("comment");

        et_comments.setText(comment);
        et_example_two.setText(example_2);
        et_example_one.setText(example_1);
        et_item_original.setText(original);
        et_item_traduccion.setText(traduccion);
    }
}
