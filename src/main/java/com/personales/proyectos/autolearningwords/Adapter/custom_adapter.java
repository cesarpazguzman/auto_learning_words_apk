package com.personales.proyectos.autolearningwords.Adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Models.folder_model;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class custom_adapter extends multi_select_adapter{
    private List<itemVisitable> viewModels;
    private TypeViewModelFactory typeViewModelFactory;

    public custom_adapter(TypeViewModelFactory typeViewModelFactory){
        this.viewModels = new ArrayList<>();
        this.typeViewModelFactory = typeViewModelFactory;
    }

    public void updateListView(List<itemVisitable> viewModels){
        Collections.sort(viewModels,order);
        this.viewModels = viewModels;
        notifyDataSetChanged();
    }

    public void add_element(itemVisitable item){
        this.viewModels.add(item);
        Collections.sort(this.viewModels, order);
        notifyDataSetChanged();
    }

    public void remove_element(itemVisitable item){
        this.viewModels.remove(item);
        Collections.sort(this.viewModels, order);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewModels.get(position).type(typeViewModelFactory);
    }

    public List<itemVisitable> get_viewModels(){
        return viewModels;
    }

    @Override
    public baseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return typeViewModelFactory.holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(baseViewHolder holder, int position) {
        holder.bind(viewModels.get(position));

        set_color_holder(holder, viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public Comparator<itemVisitable> order = new Comparator<itemVisitable>() {

        public int compare(itemVisitable s1, itemVisitable s2) {
            //Se ordena primero por tipo
            if(s1.order()==s2.order()){
                //Y luego por nombre
                String name1 = s1.getName().toUpperCase();
                String name2 = s2.getName().toUpperCase();

                return name1.compareTo(name2);
            }

            return s1.order().compareTo(s2.order());
        }
    };
}
