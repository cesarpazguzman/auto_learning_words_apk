package com.personales.proyectos.autolearningwords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.personales.proyectos.autolearningwords.Activity.BaseActivity;
import com.personales.proyectos.autolearningwords.Adapter.custom_adapter;
import com.personales.proyectos.autolearningwords.Base.AlertDialogHelper;
import com.personales.proyectos.autolearningwords.Base.RecyclerItemClickListener;
import com.personales.proyectos.autolearningwords.Base.SwipeHelper;
import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.Tables.item;
import com.personales.proyectos.autolearningwords.DataBase.databaseManager;
import com.personales.proyectos.autolearningwords.Holders.folderViewHolder;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Interfaces.tableInterface;
import com.personales.proyectos.autolearningwords.Models.mainTypeViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements AlertDialogHelper.AlertDialogListener{

    private custom_adapter custom_adapter;
    @BindView(R.id.rv_general) RecyclerView rv_folders;

    private mainTypeViewModel mainTypeViewModel;
    private databaseManager db_manager;
    private int current_level = 0;
    private boolean isMultiSelect = false;
    private ActionMode mActionMode;
    private AlertDialogHelper alertDialogHelper;
    private InputMethodManager imm;
    private SwipeHelper swipeHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");

        mainTypeViewModel = new mainTypeViewModel();
        alertDialogHelper =new AlertDialogHelper(this);
        db_manager = new databaseManager(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_folders.setLayoutManager(mLayoutManager);

        swipeHelper = new SwipeHelper(this, rv_folders) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#223a60"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));
            }
            };


        custom_adapter = new custom_adapter(mainTypeViewModel);
        rv_folders.setAdapter(custom_adapter);

        Intent intent = getIntent();
        if(intent.hasExtra(folderViewHolder.EXTRA_PARENTID)){
            current_level = intent.getIntExtra(folderViewHolder.EXTRA_PARENTID,current_level);
        }
        ArrayList<itemVisitable> view_ids = ((folder)db_manager.get_table_instance("folder")).getAllFolders(current_level);
        view_ids.addAll(((item)db_manager.get_table_instance("item")).getAllFolders(current_level));
        custom_adapter.updateListView(view_ids);

        getSupportActionBar().setDisplayHomeAsUpEnabled(current_level!=0);

        rv_folders.addOnItemTouchListener(new RecyclerItemClickListener(this, rv_folders,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);

                if(!SwipeHelper.IS_TOUCH){
                    swipeHelper.reset_swipe(SwipeHelper.swipedPos);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect && !SwipeHelper.IS_TOUCH) {

                    custom_adapter.clear_selected_item();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));
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
                            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_folder, null);
                            alertDialogHelper.showAlertDialog("","",
                                    "ACEPTAR","CANCELAR", 2, true, view);
                            break;

                        case R.id.add_item:
                            inflater = MainActivity.this.getLayoutInflater();
                            view = inflater.inflate(R.layout.dialog_item, null);
                            ImageButton add_example_item = view.findViewById(R.id.add_example_item);
                            final EditText et_example_two = view.findViewById(R.id.et_example_two);
                            add_example_item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    et_example_two.setVisibility(View.VISIBLE);
                                    et_example_two.requestFocus();
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                }
                            });

                            alertDialogHelper.showAlertDialog("","",
                            "ACEPTAR","CANCELAR", 3, true, view);
                            break;
                    }
                    return false;
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    alertDialogHelper.showAlertDialog("","Desea eliminar los "+
                                    ((Integer)custom_adapter.get_selected_items().size()).toString()+" elementos seleccionados?",
                            "ELIMINAR","CANCELAR", 1, true, null);
                    return true;
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
        final tableInterface folder_table = db_manager.get_table_instance("folder");
        final tableInterface item_table = db_manager.get_table_instance("item");

        if(from==1)
        {
            if(custom_adapter.get_selected_items().size()>0)
            {
                for(int i=0;i<custom_adapter.get_selected_items().size();i++){
                    custom_adapter.remove_element(custom_adapter.get_selected_items().get(i));
                }

                final ArrayList<itemVisitable> aux= new ArrayList<itemVisitable>();

                aux.addAll(custom_adapter.get_selected_items());

                String message = " eliminado";
                if(custom_adapter.get_selected_items().size()>1){
                    message +="s";
                }
                Snackbar snackbar = Snackbar.make(rv_folders, custom_adapter.get_selected_items().size()+message, Snackbar.LENGTH_LONG);

                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        //Si no se marcó deshacer, se elimina entonces de la base de datos
                        if(event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                            for (int i = 0; i < aux.size(); i++) {
                                itemVisitable it = aux.get(i);
                                if (it.type() == itemVisitable.FOLDER) {
                                    folder_table.delete(it.getId());
                                } else if (it.type() == itemVisitable.ITEM) {
                                    item_table.delete(it.getId());
                                }
                            }
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
                snackbar.setAction("DESHACER ", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i=0;i<aux.size();i++){
                            itemVisitable it = aux.get(i);
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
        }else if(from==2){
            EditText et_folder_name = view.findViewById(R.id.et_item_original);
            itemVisitable ins = folder_table.insert(((folder)folder_table).values(
                    et_folder_name.getText().toString(), current_level));
            custom_adapter.add_element(ins);

            if(!SwipeHelper.IS_TOUCH){
                swipeHelper.reset_swipe(SwipeHelper.swipedPos);
            }
        }
        else if(from==3){
            EditText et_item_original = view.findViewById(R.id.et_item_original);
            EditText et_item_traduccion = view.findViewById(R.id.et_item_traduccion);
            EditText et_example_one = view.findViewById(R.id.et_example_one);
            EditText et_example_two = view.findViewById(R.id.et_example_two);
            EditText et_comments = view.findViewById(R.id.et_comments);

            String comment = !et_comments.getText().toString().isEmpty() ? et_comments.getText().toString() : "";
            String example1 = !et_example_one.getText().toString().isEmpty() ? et_example_one.getText().toString() : "";
            String example2 = !et_example_two.getText().toString().isEmpty() ? et_example_two.getText().toString() : "";

            itemVisitable ins = item_table.insert(((item)item_table).values(
                    et_item_original.getText().toString(),
                    et_item_traduccion.getText().toString(), current_level,
                    comment,example1,
                    example2));
            custom_adapter.add_element(ins);


            ArrayList<itemVisitable> view_ids = ((folder)db_manager.get_table_instance("folder")).getAllFolders(current_level);
            view_ids.addAll(((item)db_manager.get_table_instance("item")).getAllFolders(current_level));

            if(!SwipeHelper.IS_TOUCH){
                swipeHelper.reset_swipe(SwipeHelper.swipedPos);
            }
        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }


}
