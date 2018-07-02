package com.personales.proyectos.autolearningwords.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.personales.proyectos.autolearningwords.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class folder_dialog extends dialog_parent {
    public interface FolderDialogListener {
        void onSaveFolderDialog(String name, boolean open_again, int folder_id);
    }

    @BindView(R.id.et_folder_name) EditText et_folder_name;
    @BindView(R.id.bt_save_close) Button bt_save_close;
    @BindView(R.id.bt_save_new) Button bt_save_new;
    @BindView(R.id.bt_close_item) Button bt_close_item;
    @BindView(R.id.til_folder_name) TextInputLayout til_folder_name;

    //true if item is being modified, false if item is being created
    private boolean new_folder = true;

    public folder_dialog(){
        super();
        layout = R.layout.dialog_folder;
    }

    public static folder_dialog newInstance(int folder_id, String name) {
        folder_dialog frag = new folder_dialog();
        frag.new_folder = false;
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putInt("folder_id", folder_id);
        frag.setArguments(args);
        return frag;
    }

    public static folder_dialog newInstance() {
        folder_dialog frag = new folder_dialog();
        frag.new_folder = true;
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        if(!new_folder){
            fill_values();
            bt_save_close.setVisibility(View.INVISIBLE);
            bt_save_new.setText(R.string.save);
            bt_close_item.setGravity(Gravity.CENTER);
        }

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

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private void save(boolean open_again){
        if(et_folder_name== null || et_folder_name.getText().toString().equals("")){
            til_folder_name.setError(getActivity().getResources().getString(R.string.field_required));
            return;
        }
        FolderDialogListener listener = (FolderDialogListener) getActivity();
        if(new_folder){
            listener.onSaveFolderDialog(et_folder_name.getText().toString(), open_again, -1);
        }else{
            //HACER EDITAR
            int folder_id = getArguments().getInt("folder_id");
            listener.onSaveFolderDialog(et_folder_name.getText().toString(), open_again, folder_id);
        }

        // Close the dialog and return back to the parent activity
        dismiss();
    }

    private void fill_values(){
        String name = getArguments().getString("name");
        et_folder_name.setText(name);
    }
}
