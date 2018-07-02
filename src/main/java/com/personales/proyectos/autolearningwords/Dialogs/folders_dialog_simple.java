package com.personales.proyectos.autolearningwords.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.personales.proyectos.autolearningwords.Adapter.multi_select_adapter;
import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.databaseManager;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class folders_dialog_simple extends dialog_parent {
    public interface ListFoldersDialogListener {
        void onMoveItemsDialog(int parent_id);
    }

    @BindView(R.id.list_folders) ListView list_folders;
    @BindView(R.id.bt_move) Button bt_move;
    @BindView(R.id.bt_close) Button bt_close;

    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> elements_list;
    private int current_selection = -1;

    private ArrayList<Integer> folder_ids_selected;

    public folders_dialog_simple(){
        super();
        elements_list = new ArrayList<>();
        layout = R.layout.dialog_list_folders;
        layout_height = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    public static folders_dialog_simple newInstance(ArrayList<Integer> folder_ids_selected) {
        folders_dialog_simple frag = new folders_dialog_simple();
        frag.folder_ids_selected = folder_ids_selected;
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        databaseManager db_manager = databaseManager.getInstance(getContext());
        ArrayList<itemVisitable> folders = db_manager.get_table_instance(folder.NAME_TABLE).get_all_elements();

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice,
                get_items_sorted(folders, 0, 0,""));

        list_folders.setAdapter(adapter);
        list_folders.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list_folders.setOnItemClickListener(list_folders_listener());

        bt_move.setEnabled(false);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multi_select_adapter.MULTISELECT_ACTIVED = false;
                dismiss();
            }
        });

        bt_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_items();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private ArrayList<String> get_items_sorted(ArrayList<itemVisitable> items, int parent_id, int level, String number){
        ArrayList<String> r = new ArrayList<>();
        if(parent_id == 0){
            r.add(getResources().getString(R.string.main));
            elements_list.add(0);
        }
        int count = 1;
        for(itemVisitable item : items){
            if(item.getParent_id() == parent_id && !folder_ids_selected.contains(item.getId())) {
                int level2 = level +1;
                r.add(number+count+"- "+item.getName());
                //r.add(repeat(level2, "     ")+"/"+item.getName());
                elements_list.add(item.getId());
                r.addAll(get_items_sorted(items,item.getId(), level2, number+count+"."));
            }
        }

        return r;
    }

    private AdapterView.OnItemClickListener list_folders_listener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                int a = list_folders.getCheckedItemPosition();
                current_selection = elements_list.get(pos);
                bt_move.setEnabled(true);
            }
        };
    }

    private void move_items(){
        ListFoldersDialogListener listener = (ListFoldersDialogListener) getActivity();
        listener.onMoveItemsDialog(current_selection);
        dismiss();
    }
}
