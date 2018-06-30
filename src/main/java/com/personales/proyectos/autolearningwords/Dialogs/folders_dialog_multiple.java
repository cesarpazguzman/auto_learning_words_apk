package com.personales.proyectos.autolearningwords.Dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.databaseManager;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class folders_dialog_multiple extends DialogFragment {
    public interface ListFoldersMultipleDialogListener {
        void onChoiceMultiple(ArrayList<Integer> folders_selected);
    }

    @BindView(R.id.list_folders)
    ListView list_folders;
    @BindView(R.id.bt_move)
    Button bt_do_test;
    @BindView(R.id.bt_close) Button bt_close;
    @BindView(R.id.text_info)
    TextView text_info;

    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> elements_list;
    private ArrayList<Integer> current_selection;

    public folders_dialog_multiple(){
        elements_list = new ArrayList<>();
        current_selection = new ArrayList<>();
    }

    public static folders_dialog_multiple newInstance() {
        folders_dialog_multiple frag = new folders_dialog_multiple();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_list_folders, container);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        bt_do_test.setText(R.string.do_test);
        text_info.setText(R.string.select_folder_test);

        databaseManager db_manager = databaseManager.getInstance(getContext());
        ArrayList<itemVisitable> folders = db_manager.get_table_instance(folder.NAME_TABLE).get_all_elements();

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice,
                get_items_sorted(folders, 0, 0,""));

        list_folders.setAdapter(adapter);
        list_folders.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list_folders.setOnItemClickListener(list_folders_listener());

        bt_close.setEnabled(false);
        bt_do_test.setEnabled(false);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseViewHolder.MULTISELECT_ACTIVED = false;
                dismiss();
            }
        });

        bt_do_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                do_test();
            }
        });


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    private ArrayList<String> get_items_sorted(ArrayList<itemVisitable> items, int parent_id, int level, String number){
        ArrayList<String> r = new ArrayList<>();
        if(parent_id == 0){
            r.add(getResources().getString(R.string.main));
            elements_list.add(0);
        }
        int count = 1;
        for(itemVisitable item : items){
            if(item.getParent_id() == parent_id) {
                int level2 = level + 1;
                r.add(number+count+"- "+item.getName());
                elements_list.add(item.getId());
                r.addAll(get_items_sorted(items, item.getId(), level2,number+count+"."));
                ++count;
            }
        }

        return r;
    }

    private AdapterView.OnItemClickListener list_folders_listener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                SparseBooleanArray a = list_folders.getCheckedItemPositions();

                current_selection = new ArrayList<>();
                for(int i = 0; i < a.size() ; i++)
                {
                    current_selection.add(elements_list.get(a.keyAt(i)));
                }

                bt_close.setEnabled(list_folders.getCheckedItemCount()!=0);
                bt_do_test.setEnabled(list_folders.getCheckedItemCount()!=0);
            }
        };
    }

    private void do_test(){
        ListFoldersMultipleDialogListener listener = (ListFoldersMultipleDialogListener) getActivity();
        listener.onChoiceMultiple(current_selection);
        dismiss();
    }
}
