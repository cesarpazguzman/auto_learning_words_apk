package com.personales.proyectos.autolearningwords.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;

public class multi_select_adapter extends RecyclerView.Adapter<baseViewHolder>{
    private ArrayList<itemVisitable> selected_items;
    private TypeViewModelFactory typeViewModelFactory;
    private Context context;
    public static boolean MULTISELECT_ACTIVED = false;

    public multi_select_adapter(TypeViewModelFactory typeViewModelFactory, final Context context){
        selected_items=new ArrayList<itemVisitable>(){
            @Override
            public boolean add(itemVisitable itemVisitable) {
                if(contains(itemVisitable))
                    return false;
                return super.add(itemVisitable);
            }
        };


        this.typeViewModelFactory = typeViewModelFactory;
        this.context= context;
    }

    public void add_selected_item(itemVisitable item){
        selected_items.add(item);
        MULTISELECT_ACTIVED = true;
    }

    public int getItemViewType_selected(int position) {
        return selected_items.get(position).type(typeViewModelFactory)[1];
    }

    public void remove_selected_item(itemVisitable item){
        selected_items.remove(item);

        MULTISELECT_ACTIVED = selected_items.size()!=0;
    }

    public void clear_selected_item(){
        selected_items = new ArrayList<>();
        MULTISELECT_ACTIVED = false;
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
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
