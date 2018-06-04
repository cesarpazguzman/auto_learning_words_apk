package com.personales.proyectos.autolearningwords.Interfaces;

import android.content.ContentValues;

public interface tableInterface {
    //Insertar registro en la base de datos y devuelve un objeto con el elemento insertado
    itemVisitable insert(ContentValues vals);

    void delete(Integer id);

    void update(Integer id, ContentValues vals);
}
