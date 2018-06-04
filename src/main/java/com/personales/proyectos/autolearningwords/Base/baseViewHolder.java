package com.personales.proyectos.autolearningwords.Base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class baseViewHolder<T> extends RecyclerView.ViewHolder {
    public static boolean MULTISELECT_ACTIVED = false;

    public baseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element);

    public Context getContext(){
        return itemView.getContext();

    }
}
