package com.personales.proyectos.autolearningwords.Interfaces;

import java.io.Serializable;

public interface itemVisitable extends Serializable{
    public static int ITEM = 2;
    public static int FOLDER = 1;

    int type(TypeViewModelFactory typeViewModelFactory);

    int type();

    Integer order();

    String getName();

    int getId();
}
