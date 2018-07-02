package com.personales.proyectos.autolearningwords.Dialogs;

import android.support.design.widget.TextInputLayout;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.personales.proyectos.autolearningwords.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class item_dialog extends dialog_parent {

    public interface ItemDialogListener {
        void onSaveItemDialog(String original, String traduccion, String example_1,
                              String example_2, String comment, boolean open_again,
                              int item_id);
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
    @BindView(R.id.til_original) TextInputLayout til_original;

    //true if item is being modified, false if item is being created
    private boolean new_item = true;
    private InputMethodManager imm;

    public item_dialog(){
        super();
        layout = R.layout.dialog_item;
    }

    public static item_dialog newInstance(int item_id, String original, String traduccion, String example_1,
                                          String example_2, String comment) {
        item_dialog frag = new item_dialog();
        frag.new_item = false;
        Bundle args = new Bundle();
        args.putString("original", original );
        args.putString("traduccion", traduccion);
        args.putString("example_1", example_1);
        args.putString("example_2", example_2);
        args.putString("comment", comment);
        args.putInt("item_id", item_id);
        frag.setArguments(args);
        return frag;
    }

    public static item_dialog newInstance() {
        item_dialog frag = new item_dialog();
        frag.new_item = true;
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if(!new_item){
            bt_save_close.setVisibility(View.INVISIBLE);
            bt_save_new.setText(R.string.save);
            bt_close_item.setGravity(Gravity.CENTER);
        }

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
        if(et_item_original== null || et_item_original.getText().toString().equals("")){
            til_original.setError(getActivity().getResources().getString(R.string.field_required));
            return;
        }
        String comment = !et_comments.getText().toString().isEmpty() ? et_comments.getText().toString() : "";
        String example1 = !et_example_one.getText().toString().isEmpty() ? et_example_one.getText().toString() : "";
        String example2 = !et_example_two.getText().toString().isEmpty() ? et_example_two.getText().toString() : "";


        ItemDialogListener listener = (ItemDialogListener) getActivity();
        if(new_item){
            listener.onSaveItemDialog(et_item_original.getText().toString(), et_item_traduccion.getText().toString(),
                    example1, example2, comment, open_again, -1);
        }else{
            //EDITAR
            int item_id = getArguments().getInt("item_id");
            listener.onSaveItemDialog(et_item_original.getText().toString(), et_item_traduccion.getText().toString(),
                    example1, example2, comment, open_again, item_id);
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
