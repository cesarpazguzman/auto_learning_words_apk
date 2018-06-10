package com.personales.proyectos.autolearningwords.Interfaces;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Map;

public interface tableInterface {
    //Insertar registro en la base de datos y devuelve un objeto con el elemento insertado
    itemVisitable insert(ContentValues vals);

    void delete(Integer id);

    ArrayList<itemVisitable> get_all_elements(int parent);

    void update(Integer id, ContentValues vals);
}
