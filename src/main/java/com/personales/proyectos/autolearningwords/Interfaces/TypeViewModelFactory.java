package com.personales.proyectos.autolearningwords.Interfaces;

import android.view.View;

import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Models.folder_model;
import com.personales.proyectos.autolearningwords.Models.item_model;

public interface TypeViewModelFactory {
    int type(folder_model viewModel);
    int type(item_model viewModel);

    baseViewHolder holder(View view, int type);
}
