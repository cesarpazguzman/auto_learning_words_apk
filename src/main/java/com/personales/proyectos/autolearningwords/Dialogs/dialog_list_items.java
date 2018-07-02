package com.personales.proyectos.autolearningwords.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.personales.proyectos.autolearningwords.Adapter.custom_adapter;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Models.mainTypeViewModel;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class dialog_list_items extends dialog_parent {

    private ArrayList<itemVisitable> all_words;
    private custom_adapter adapter;

    @BindView(R.id.rv_general)
    RecyclerView rv_general;

    public dialog_list_items(){
        super();
        layout_height = ViewGroup.LayoutParams.MATCH_PARENT;
        layout = R.layout.content_main;
    }

    public static dialog_list_items newInstance(ArrayList<itemVisitable> all_words) {
        dialog_list_items frag = new dialog_list_items();
        frag.all_words = all_words;
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_general.setLayoutManager(mLayoutManager);

        mainTypeViewModel mainTypeViewModel = new mainTypeViewModel();
        adapter = new custom_adapter(mainTypeViewModel,getContext());
        rv_general.setAdapter(adapter);
        adapter.updateListView(all_words);


        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
