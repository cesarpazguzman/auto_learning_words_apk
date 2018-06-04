package com.personales.proyectos.autolearningwords.Models;

import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;

public class item_model implements itemVisitable {
    private int id;
    private String original;
    private String translation;
    private int folder_id;

    public item_model(int id, String original, String translation, int folder_id) {
        this.id = id;
        this.original = original;
        this.translation = translation;
        this.folder_id = folder_id;
    }

    public item_model(int id, String original, String translation) {
        this.id = id;
        this.original = original;
        this.translation = translation;
        this.folder_id = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return original;
    }

    public String getTranslation() {
        return translation;
    }

    public int getFolder_id() {
        return folder_id;
    }

    @Override
    public int type(TypeViewModelFactory typeViewModelFactory) {
        return typeViewModelFactory.type(this);
    }

    @Override
    public Integer order() {
        return itemVisitable.ITEM;
    }

    @Override
    public int type() {
        return itemVisitable.ITEM;
    }
}
