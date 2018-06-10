package com.personales.proyectos.autolearningwords.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.Tables.item;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Interfaces.tableInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

    private ContentValues values(Map<String, Object> vals) {
        Iterator<Map.Entry<String, Object>> it = vals.entrySet().iterator();
        ContentValues values = new ContentValues();
        while (it.hasNext()) {
            Map.Entry<String, Object> pair = it.next();
            if(pair.getValue() instanceof Integer){
                values.put(pair.getKey(), (int)pair.getValue());
            }else if(pair.getValue() instanceof String){
                values.put(pair.getKey(), (String)pair.getValue());
            }
        }

        return values;
    }

    private void instance_all_tables(){
        tables.put(folder.NAME_TABLE,new folder(db));
        tables.put(item.NAME_TABLE, new item(db));
    }

    public void delete(String name_table, Integer id){
        tableInterface table = get_table_instance(name_table);
        if(table!=null)
            get_table_instance(name_table).delete(id);
    }

    public itemVisitable insert(String name_table, Map<String, Object> map_vals) {
        tableInterface table = get_table_instance(name_table);
        ContentValues vals = values(map_vals);
        return table!=null ? table.insert(vals): null;
    }

    public ArrayList<itemVisitable> get_all_elements(String name_table, int parent){
        tableInterface table = get_table_instance(name_table);

        return table!=null ? table.get_all_elements(parent) : new ArrayList<itemVisitable>();
    }

    private tableInterface get_table_instance(String key){
        if (tables.containsKey(key))
            return tables.get(key);

        return null;
    }


}


