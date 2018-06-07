package com.personales.proyectos.autolearningwords.Adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class multi_select_adapter extends RecyclerView.Adapter<baseViewHolder>{
    private ArrayList<itemVisitable> selected_items;

    public multi_select_adapter(){
        selected_items=new ArrayList<>();
    }

    public void add_selected_item(itemVisitable item){
        selected_items.add(item);
        baseViewHolder.MULTISELECT_ACTIVED = true;
        notifyDataSetChanged();
    }

    public void remove_selected_item(itemVisitable item){
        selected_items.remove(item);
        notifyDataSetChanged();
    }

    public void clear_selected_item(){
        selected_items = new ArrayList<>();
        baseViewHolder.MULTISELECT_ACTIVED = false;
        notifyDataSetChanged();
    }

    public ArrayList<itemVisitable> get_selected_items(){
        return selected_items;
    }

    @Override
    public baseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(baseViewHolder holder, int position) { }

    protected void set_color_holder(baseViewHolder holder, itemVisitable item) {
        if(selected_items.contains(item))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.getContext(), R.color.list_item_selected_state));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.getContext(), R.color.list_item_normal_state));
        System.out.println("selected itemS "+selected_items);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
