package com.personales.proyectos.autolearningwords.Models;

import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;

import java.util.Comparator;

public class folder_model implements itemVisitable {
    private int id;
    private String name;
    private int parent_id;


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

    @Override
    public int type(TypeViewModelFactory typeViewModelFactory) {
        return typeViewModelFactory.type(this);
    }

    @Override
    public Integer order() {
        return itemVisitable.FOLDER;
    }

    @Override
    public int type() {
        return itemVisitable.FOLDER;
    }
}
