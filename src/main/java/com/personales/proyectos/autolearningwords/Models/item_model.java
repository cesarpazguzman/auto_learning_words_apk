package com.personales.proyectos.autolearningwords.Models;

import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;

public class item_model implements itemVisitable {
    private int id;
    private String original;
    private String translation;
    private int folder_id;
    private String comment;
    private String example1;
    private String example2;

    public item_model(int id, String original, String translation, int folder_id, String comment,
                      String example1, String example2) {
        this.id = id;
        this.original = original;
        this.translation = translation;
        this.folder_id = folder_id;
        this.comment = comment;
        this.example1 = example1;
        this.example2 = example2;
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

    public String getComment() {
        return comment;
    }

    public String getExample1() {
        return example1;
    }

    public String getExample2() {
        return example2;
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
