package com.personales.proyectos.autolearningwords;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.view.ActionMode;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.personales.proyectos.autolearningwords.Activity.BaseActivity;
import com.personales.proyectos.autolearningwords.Adapter.custom_adapter;
import com.personales.proyectos.autolearningwords.DataBase.Tables.language;
import com.personales.proyectos.autolearningwords.Dialogs.AlertDialogHelper;
import com.personales.proyectos.autolearningwords.Base.RecyclerItemClickListener;
import com.personales.proyectos.autolearningwords.Base.SwipeHelper;
import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.Tables.item;
import com.personales.proyectos.autolearningwords.DataBase.databaseManager;
import com.personales.proyectos.autolearningwords.Dialogs.create_language_dialog;
import com.personales.proyectos.autolearningwords.Dialogs.dialog_contact;
import com.personales.proyectos.autolearningwords.Dialogs.dialog_warning;
import com.personales.proyectos.autolearningwords.Dialogs.folder_dialog;
import com.personales.proyectos.autolearningwords.Dialogs.folders_dialog_multiple;
import com.personales.proyectos.autolearningwords.Dialogs.folders_dialog_simple;
import com.personales.proyectos.autolearningwords.Dialogs.item_dialog;
import com.personales.proyectos.autolearningwords.Dialogs.test_dialog;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Models.folder_model;
import com.personales.proyectos.autolearningwords.Models.item_model;
import com.personales.proyectos.autolearningwords.Models.language_model;
import com.personales.proyectos.autolearningwords.Models.mainTypeViewModel;
import com.personales.proyectos.autolearningwords.Models.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.personales.proyectos.autolearningwords.Holders.folderViewHolder.EXTRA_PARENTID;

public class MainActivity extends BaseActivity implements AlertDialogHelper.AlertDialogListener,
                                                          item_dialog.ItemDialogListener, folder_dialog.FolderDialogListener,
                                                          folders_dialog_simple.ListFoldersDialogListener,
                                                          SearchView.OnQueryTextListener, folders_dialog_multiple.ListFoldersMultipleDialogListener{

    private custom_adapter custom_adapter;
    @BindView(R.id.rv_general) RecyclerView rv_folders;

    private mainTypeViewModel mainTypeViewModel;
    private databaseManager db_manager;
    private int current_level = 0;
    private boolean isMultiSelect = false;
    private ActionMode mActionMode;
    private AlertDialogHelper alertDialogHelper;
    private InputMethodManager imm;
    private Menu menu_principal;
    private session _session;
    private FragmentManager fm;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _session = session.getInstance(this);
        setTitle("");

        mainTypeViewModel = new mainTypeViewModel();
        alertDialogHelper =new AlertDialogHelper(this);
        db_manager = databaseManager.getInstance(this);
        fm = getSupportFragmentManager();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_folders.setLayoutManager(mLayoutManager);

        swipeHelper = new SwipeHelper(this, rv_folders) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "EDIT",
                        0,
                        Color.parseColor("#ebebeb"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                itemVisitable item = custom_adapter.get_viewModels().get(pos);
                                if(item.type(mainTypeViewModel)[1]==itemVisitable.ITEM){
                                    item_model it = (item_model)item;
                                    add_element_dialog(it.getId(), it.getName(), it.getTranslation(),
                                            it.getExample1(), it.getExample2(), it.getComment());
                                }else if(item.type(mainTypeViewModel)[1]==itemVisitable.FOLDER){
                                    folder_model fd = (folder_model)item;
                                    add_folder_dialog(fd.getId(), fd.getName());
                                }
                                swipeHelper.reset_swipe(SwipeHelper.getSwipedPos());
                                swipeHelper.reset_swipe(-1);
                            }
                        }
                ));
            }
        };

        custom_adapter = new custom_adapter(mainTypeViewModel,this);
        rv_folders.setAdapter(custom_adapter);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_PARENTID)){
            current_level = intent.getIntExtra(EXTRA_PARENTID,current_level);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(current_level!=0);

        rv_folders.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                rv_folders,
                get_item_click_listener()));
    }

    private void update_list_items(){
        if(custom_adapter!=null && db_manager!=null){
            ArrayList<itemVisitable> view_ids = db_manager.get_table_instance(folder.NAME_TABLE).get_all_elements(current_level);
            view_ids.addAll(db_manager.get_table_instance(item.NAME_TABLE).get_all_elements(current_level));

            custom_adapter.updateListView(view_ids);
        }
    }

    private SwipeHelper swipeHelper;

    private RecyclerItemClickListener.OnItemClickListener get_item_click_listener(){
        return new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);

                if(!SwipeHelper.getISTOUCH() && SwipeHelper.getSwipedPos()!=-1){
                    if(SwipeHelper.getSwipedPos() != SwipeHelper.oldPos)
                        swipeHelper.reset_swipe(SwipeHelper.getSwipedPos());
                    swipeHelper.reset_swipe(-1);

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if(!SwipeHelper.getISTOUCH() && SwipeHelper.getSwipedPos()!=-1){
                    if(SwipeHelper.getSwipedPos() != SwipeHelper.oldPos)
                        swipeHelper.reset_swipe(SwipeHelper.getSwipedPos());
                    swipeHelper.reset_swipe(-1);

                }

                if (!isMultiSelect) {

                    custom_adapter.clear_selected_item();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        };
    }
    private void multi_select(int position) {
        if (mActionMode != null) {
            if (custom_adapter.get_selected_items().contains(custom_adapter.get_viewModels().get(position)))
                custom_adapter.remove_selected_item(custom_adapter.get_viewModels().get(position));
            else
                custom_adapter.add_selected_item(custom_adapter.get_viewModels().get(position));

            if (custom_adapter.get_selected_items().size() > 0)
                mActionMode.setTitle("" + custom_adapter.get_selected_items().size());
            else{
                mActionMode.setTitle("");
                mActionMode.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(this);

        menu_principal = menu;
        String name_lang = ((language)(db_manager.get_table_instance(language.NAME_TABLE))).get_name(_session.get_language_translation());
        if(name_lang == null || name_lang.isEmpty())
            name_lang = getResources().getString(R.string.language_menu);
        menu_principal.getItem(0).setTitle(_session.get_language_translation() == -1 ?getResources().getString(R.string.language_menu):
                name_lang);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_element) {
            PopupMenu popupMenu = new PopupMenu(this, findViewById(id));
            popupMenu.inflate(R.menu.menu_add);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.add_folder:
                            if(!warning_language()) {
                                add_folder_dialog();
                            }
                            break;

                        case R.id.add_item:
                            if(!warning_language()) {
                                add_element_dialog();
                            }
                            break;
                    }
                    return false;
                }
            });
            return true;
        }else if(id == R.id.action_more){
            PopupMenu popupMenu = new PopupMenu(this, findViewById(id));
            popupMenu.inflate(R.menu.menu_more);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.test:
                            if(!warning_language()){
                                folders_dialog_multiple.newInstance().show(fm, "new_folders_dialog_multiple");
                            }
                            break;

                        case R.id.contact:
                            dialog_contact.newInstance().show(fm, "new_dialog_contact_frag");
                            break;
                    }
                    return false;
                }
            });
            return true;
        }else if(id == R.id.action_language){
            PopupMenu popupMenu = new PopupMenu(this, findViewById(id));

            final ArrayList<language_model> languages = db_manager.get_table_instance(language.NAME_TABLE).get_all_elements();
            for(int i=0;i<languages.size();++i){
                SubMenu sMenu = popupMenu.getMenu().addSubMenu(0,languages.get(i).getId(),0,languages.get(i).getName());
                sMenu.add(0, 101+i, 0, R.string.select_min);
                sMenu.add(0, 201+i, 0, R.string.remove_min);
            }

            popupMenu.getMenu().add(0,100,0,R.string.add_language);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(final MenuItem item) {
                    if(item.getItemId() == 100){
                        create_language_dialog.newInstance().show(fm, "new_create_language_dialog");

                    }else if(item.getItemId()>100 && item.getItemId() < 201){
                        menu_principal.getItem(0).setTitle(languages.get(-101+item.getItemId()).getName());
                        _session.set_language_translation(languages.get(-101+item.getItemId()).getId());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        //update_list_items();
                    }
                    else if(item.getItemId()>200){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(R.string.remove_lang)
                                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                        db_manager.delete(language.NAME_TABLE, languages.get(-201+item.getItemId()).getId());
                                        if(languages.size()<=1){
                                            menu_principal.getItem(0).setTitle(R.string.language_menu);
                                        }
                                        if(languages.get(-201+item.getItemId()).getId() == session.getInstance().get_language_translation()){
                                            _session.set_language_translation(-1);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();

                    }
                    return false;
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean warning_language(){
        if(session.getInstance().get_language_translation()==-1){
            dialog_warning.newInstance(getResources().getString(R.string.warning_lang))
                    .show(fm, "new_folder_dialog");
            return true;
        }
        return false;
    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MainActivity.this.getMenuInflater().inflate(R.menu.menu_multiple_selection, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("",getResources().getString(R.string.remove_items)+" "+
                                    ((Integer)custom_adapter.get_selected_items().size()).toString()+" "+
                                    getResources().getString(R.string.items_selected),
                            getResources().getString(R.string.delete),
                            getResources().getString(R.string.cancel), "", 1, true, null);
                    return true;
                case R.id.action_move:
                    ArrayList<Integer> folder_ids_selected = new ArrayList<>();
                    for(itemVisitable it:custom_adapter.get_selected_items()){
                        if(it.type(mainTypeViewModel)[1] == itemVisitable.FOLDER){
                            folder_ids_selected.add(it.getId());
                        }
                    }

                    folders_dialog_simple.newInstance(folder_ids_selected)
                            .show(fm, "new_folders_dialog");
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            custom_adapter.clear_selected_item();
        }
    };

    @Override
    public void onPositiveClick(int from, View view) {
        if(from==1)
        {
            if(custom_adapter.get_selected_items().size()>0)
            {
                for(int i=0;i<custom_adapter.get_selected_items().size();i++){
                    custom_adapter.remove_element(custom_adapter.get_selected_items().get(i));
                }

                final ArrayList<itemVisitable> selecte_items_aux = custom_adapter.get_selected_items();

                String message = " "+getResources().getString(R.string.removed);
                if(custom_adapter.get_selected_items().size()>1){
                    //message +="s";
                }
                Snackbar snackbar = Snackbar.make(rv_folders, custom_adapter.get_selected_items().size()+message, Snackbar.LENGTH_LONG);

                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        //Si no se marcó deshacer, se elimina entonces de la base de datos
                        if(event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            for (int i = 0; i < selecte_items_aux.size(); i++) {
                                itemVisitable it = selecte_items_aux.get(i);
                                int type = it.type(mainTypeViewModel)[1];
                                if(type == itemVisitable.ITEM)
                                    db_manager.delete(item.NAME_TABLE, it.getId());
                                else if(type == itemVisitable.FOLDER)
                                    db_manager.delete(folder.NAME_TABLE, it.getId());
                            }
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
                snackbar.setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i=0;i<selecte_items_aux.size();i++){
                            itemVisitable it = selecte_items_aux.get(i);
                            custom_adapter.add_element(it);
                        }
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();


                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }
        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }


    private void add_folder_dialog(){
        folder_dialog.newInstance().show(fm, "new_folder_dialog");
    }

    private void add_folder_dialog(int folder_id, String name){
        folder_dialog.newInstance(folder_id, name).show(fm, "modify_item_dialog");
    }

    private void add_element_dialog(){
        item_dialog.newInstance().show(fm, "new_item_dialog");
    }

    private void add_element_dialog(int item_id, String original, String traduccion,
                                    String example_1, String example_2, String comment){

        item_dialog.newInstance(item_id, original, traduccion, example_1,
                example_2, comment).show(fm, "modify_item_dialog");
    }

    @Override
    public void onSaveItemDialog(String original, String traduccion, String example1, String example2,
                                 String comment, boolean open_again, int item_id) {
        Map<String, Object> vals = new HashMap<>();
        vals.put(item.col.ORIGINAL, original);
        vals.put(item.col.TRANSLATION, traduccion);
        vals.put(item.col.FOLDER_ID, current_level);
        vals.put(item.col.COMMENT, comment);
        vals.put(item.col.EXAMPLE1, example1);
        vals.put(item.col.EXAMPLE2, example2);

        SwipeHelper.setISTOUCH(false);
        if(item_id == -1){
            itemVisitable ins = (itemVisitable) db_manager.insert(item.NAME_TABLE, vals);

            custom_adapter.add_element(ins);

            if(open_again){
                add_element_dialog();
            }
        }else{
            item_model ins = (item_model) custom_adapter.get_element(itemVisitable.ITEM, item_id);
            if(ins!=null){
                db_manager.update(item.NAME_TABLE, item_id, vals);
                ins.setOriginal(original);
                ins.setTranslation(traduccion);
                ins.setExample1(example1);
                ins.setExample2(example2);
                ins.setComment(comment);
                custom_adapter.item_changed(ins);
            }

            swipeHelper.reset_swipe(SwipeHelper.getSwipedPos());
        }
    }


    @Override
    public void onSaveFolderDialog(String name, boolean open_again, int folder_id) {
        Map<String, Object> vals = new HashMap<>();
        vals.put(folder.col.NAME, name);
        vals.put(folder.col.PARENT_ID, current_level);

        SwipeHelper.setISTOUCH(false);

        if(folder_id == -1){
            itemVisitable ins = (itemVisitable) db_manager.insert(folder.NAME_TABLE, vals);

            custom_adapter.add_element(ins);

            if(open_again){
                add_folder_dialog();
            }
        }else{
            folder_model ins = (folder_model) custom_adapter.get_element(itemVisitable.FOLDER, folder_id);
            if(ins!=null){
                db_manager.update(folder.NAME_TABLE, folder_id, vals);
                ins.setName(name);
                custom_adapter.item_changed(ins);
            }
            swipeHelper.reset_swipe(SwipeHelper.getSwipedPos());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        update_list_items();

    }

    @Override
    public void onMoveItemsDialog(int parent_id) {
        ArrayList<itemVisitable> selected_items = new ArrayList<>();
        selected_items.addAll(custom_adapter.get_selected_items());
        for(itemVisitable items:selected_items){
            if(items.getParent_id()!=parent_id){
                Map<String, Object> vals = new HashMap<>();

                if(items.type(mainTypeViewModel)[1] == itemVisitable.ITEM){
                    vals.put(item.col.FOLDER_ID, parent_id);
                    db_manager.update(item.NAME_TABLE, items.getId(),vals);
                }else if (items.type(mainTypeViewModel)[1] == itemVisitable.FOLDER) {
                    vals.put(folder.col.PARENT_ID, parent_id);
                    db_manager.update(folder.NAME_TABLE, items.getId(), vals);
                }
                int pos_adapter = custom_adapter.get_viewModels().indexOf(items);
                multi_select(pos_adapter);
                custom_adapter.remove_element(items);
            }
        }
        String name_folder = getResources().getString(R.string.main);
        for(itemVisitable item:custom_adapter.get_viewModels()){
            if(item.getId()==parent_id) {
                name_folder = item.getName();
                break;
            }
        }
        Toast.makeText(this, getResources().getString(R.string.select_items_move)+" "+name_folder, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        ArrayList<itemVisitable> newList = new ArrayList<>();
        ArrayList<itemVisitable> items = db_manager.get_table_instance(item.NAME_TABLE).get_all_elements();
        ArrayList<itemVisitable> folders = db_manager.get_table_instance(folder.NAME_TABLE).get_all_elements();
        if(!s.equals("")){
            for(itemVisitable item:items){
                String name = item.getName().toLowerCase();
                if(name.contains(s)){
                    newList.add(item);
                }
            }
            for(itemVisitable item:folders){
                String name = item.getName().toLowerCase();
                if(name.contains(s)){
                    newList.add(item);
                }
            }
            custom_adapter.updateListView(newList);
        }else{
            update_list_items();
        }

        System.out.println("LIST: "+newList+" s: "+s);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public void onChoiceMultiple(ArrayList<Integer> folders_selected) {
        item table_item = (item)db_manager.get_table_instance("item");
        ArrayList<itemVisitable> items_to_test = new ArrayList<>();
        for(int folder_id:folders_selected){
            items_to_test.addAll(table_item.get_all_elements(folder_id));
        }

        if(items_to_test.size()>0){
            test_dialog.newInstance(items_to_test).show(fm, "new_test_dialog");
        }
    }
}
