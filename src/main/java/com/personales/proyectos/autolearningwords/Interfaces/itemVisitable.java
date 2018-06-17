package com.personales.proyectos.autolearningwords.Interfaces;

import java.io.Serializable;

public interface itemVisitable extends Serializable{
    public static int ITEM = 2;
    public static int FOLDER = 1;

    boolean get_expanded();

    int[] type(TypeViewModelFactory typeViewModelFactory);

    String getName();
    int getParent_id();
    void setParent_id(int parent_id);
    int getId();
}
