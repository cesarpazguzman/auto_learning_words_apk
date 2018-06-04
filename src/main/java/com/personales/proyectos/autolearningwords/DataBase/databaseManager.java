package com.personales.proyectos.autolearningwords.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.Tables.item;
import com.personales.proyectos.autolearningwords.Interfaces.tableInterface;

import java.util.HashMap;
import java.util.Vector;

//Clase que se va a encargar en definir las tablas, que campos tendrá la tabla
//Métodos de acceso a la BD, para separar la lógica
public class databaseManager {

    private databaseHelper helper;
    private SQLiteDatabase db;

    private HashMap<String, tableInterface> tables;

    public databaseManager(Context context) {
        //BASE DE DATOS
        helper = new databaseHelper(context);
        db = helper.getWritableDatabase();

        tables = new HashMap<String, tableInterface>();

        instance_all_tables();
    }

    private void instance_all_tables(){
        tables.put("folder",new folder(db));
        tables.put("item", new item(db));
    }

    public tableInterface get_table_instance(String key){
        if (tables.containsKey(key))
            return tables.get(key);

        return null;
    }


}


