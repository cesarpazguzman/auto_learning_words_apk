package com.personales.proyectos.autolearningwords.Interfaces;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Map;

public interface tableInterface<T> {
    //Insertar registro en la base de datos y devuelve un objeto con el elemento insertado
    T insert(ContentValues vals);

    void delete(Integer id);

    ArrayList<T> get_all_elements(int parent);
    ArrayList<T> get_all_elements();
    void update(Integer id, ContentValues vals);
}
