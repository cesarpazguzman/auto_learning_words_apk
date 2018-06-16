package com.personales.proyectos.autolearningwords.Models;

import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;

public class folder_model implements itemVisitable {
    private int id, parent_id;
    private String name;

    public folder_model(int id, String name, int parent_id) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
    }

    public folder_model(int id, String name) {
        this.id = id;
        this.name = name;
        this.parent_id = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParent_id() {
        return parent_id;
    }

    public boolean get_expanded(){ return false; }
    @Override
    public int[] type(TypeViewModelFactory typeViewModelFactory) {
        return new int[]{typeViewModelFactory.type(this),itemVisitable.FOLDER};
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
