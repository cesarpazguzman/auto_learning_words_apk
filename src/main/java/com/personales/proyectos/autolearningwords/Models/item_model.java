package com.personales.proyectos.autolearningwords.Models;

import com.personales.proyectos.autolearningwords.Interfaces.TypeViewModelFactory;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;

public class item_model implements itemVisitable {
    private int id, folder_id;
    private String original, translation, comment, example1, example2;
    public boolean expanded = false;

    public item_model(int id, String original, String translation, int folder_id, String comment,
                      String example1, String example2) {
        this.id = id;
        this.original = original;
        this.translation = translation!=null ? translation : "";
        this.folder_id = folder_id;
        this.comment = comment!=null ? comment : "";
        this.example1 = example1!=null ? example1 : "";
        this.example2 = example2!=null ? example2 : "";
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

    public boolean get_expanded(){
        return expanded;
    }
    @Override
    public int[] type(TypeViewModelFactory typeViewModelFactory) {
        return new int[]{typeViewModelFactory.type(this),itemVisitable.ITEM};
    }

    public void setFolder_id(int folder_id) {
        this.folder_id = folder_id;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setExample1(String example1) {
        this.example1 = example1;
    }

    public void setExample2(String example2) {
        this.example2 = example2;
    }
}
