package com.personales.proyectos.autolearningwords.Adapter;

import android.content.Context;
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
    private Context context;

    public custom_adapter(TypeViewModelFactory typeViewModelFactory, Context context){
        super(typeViewModelFactory, context);

        this.viewModels = new ArrayList<>();
        this.typeViewModelFactory = typeViewModelFactory;
        this.context= context;
    }

    public void updateListView(List<itemVisitable> viewModels){
        Collections.sort(viewModels,order);
        this.viewModels = viewModels;
        notifyDataSetChanged();
    }

    public void add_element(itemVisitable item){
        boolean inserted = false;
        for(int i=0;i<viewModels.size();++i){
            if(viewModels.get(i).type(typeViewModelFactory)[1]==item.type(typeViewModelFactory)[1]){
                if(viewModels.get(i).getName().toUpperCase().compareTo(item.getName().toUpperCase())>0){
                    this.viewModels.add(i,item);
                    notifyItemInserted(i);
                    inserted = true;
                    break;
                }
            }
        }
        if(!inserted){
            if(item.type(typeViewModelFactory)[1]==itemVisitable.FOLDER){
                this.viewModels.add(0,item);
                notifyItemInserted(0);
            }else if(item.type(typeViewModelFactory)[1]==itemVisitable.ITEM){
                this.viewModels.add(viewModels.size(),item);
                notifyItemInserted(viewModels.size()-1);
            }
        }
    }

    public itemVisitable get_element(int type, int id){
        for(itemVisitable it : viewModels){
            if(it.getId() == id && it.type(typeViewModelFactory)[1]==type)
                return it;
        }
        return null;
    }

    public void item_changed(itemVisitable item){
        remove_element(get_element(item.type(typeViewModelFactory)[1], item.getId()));
        add_element(item);
    }

    public void remove_element(itemVisitable item){
        int pos_remove = this.viewModels.indexOf(item);
        this.viewModels.remove(pos_remove);
        notifyItemRemoved(pos_remove);
    }

    @Override
    public int getItemViewType(int position) {
        return viewModels.get(position).type(typeViewModelFactory)[0];
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
    public void add_selected_item(itemVisitable item) {
        super.add_selected_item(item);
        int pos = this.viewModels.indexOf(item);
        notifyItemChanged(pos);
    }

    @Override
    public void remove_selected_item(itemVisitable item) {
        super.remove_selected_item(item);
        int pos = this.viewModels.indexOf(item);
        notifyItemChanged(pos);
    }

    @Override
    public void clear_selected_item() {
        List<Integer> items_to_clear = new ArrayList<>();
        ArrayList<itemVisitable> items_selected = get_selected_items();
        for(int i=0;i<items_selected.size();++i){
            items_to_clear.add(this.viewModels.indexOf(items_selected.get(i)));
        }
        super.clear_selected_item();

        for(int i=0;i<items_to_clear.size();++i){
            notifyItemChanged(items_to_clear.get(i));
        }
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
            if(s1.type(typeViewModelFactory)[1]==s2.type(typeViewModelFactory)[1]){
                //Y luego por nombre
                String name1 = s1.getName().toUpperCase();
                String name2 = s2.getName().toUpperCase();

                return name1.compareTo(name2);
            }

            return ((Integer)s1.type(typeViewModelFactory)[1]).compareTo(s2.type(typeViewModelFactory)[1]);
        }
    };
}
