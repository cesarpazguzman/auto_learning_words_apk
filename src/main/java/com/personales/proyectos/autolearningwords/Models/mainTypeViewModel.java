package com.personales.proyectos.autolearningwords.Models;

import android.view.View;

import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Holders.folderViewHolder;
import com.personales.proyectos.autolearningwords.Holders.itemViewHolder;
import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;

public class mainTypeViewModel implements TypeViewModelFactory {

    @Override
    public int type(folder_model viewModel) {
        return folderViewHolder.LAYOUT;
    }

    @Override
    public int type(item_model viewModel) {
        return itemViewHolder.LAYOUT;
    }

    @Override
    public baseViewHolder holder(View view, int type) {
        switch(type){
            case folderViewHolder.LAYOUT:
                return new folderViewHolder(view);
            case itemViewHolder.LAYOUT:
                return new itemViewHolder(view);
            default:
                return new folderViewHolder(view);
        }
    }
}
