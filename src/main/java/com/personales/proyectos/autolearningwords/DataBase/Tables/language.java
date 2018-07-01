package com.personales.proyectos.autolearningwords.DataBase.Tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.personales.proyectos.autolearningwords.Interfaces.tableInterface;
import com.personales.proyectos.autolearningwords.Models.language_model;

import java.util.ArrayList;

public class language implements tableInterface {

    public static final String NAME_TABLE = "language";
    private SQLiteDatabase db;

    public language(SQLiteDatabase db) {
        this.db = db;
    }

    public class col{
        public static final String ID = "_id";
        public static final String NAME = "name";
    }

    public static final String CREATE_TABLE = "CREATE TABLE "+NAME_TABLE+ "("
            + col.ID+" integer primary key autoincrement,"
            + col.NAME+" text not null) ;";


    public language_model insert(ContentValues vals){
        if(exist_language(vals.getAsString(col.NAME))){
            return null;
        }
        db.insert(NAME_TABLE, null, vals);

        Cursor cursor = db.rawQuery("SELECT "+ col.ID+" from "+NAME_TABLE+" order by "+ col.ID+ " desc limit 1",
                new String[] {});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(col.ID));

            return new language_model(id, vals.getAsString(col.NAME));
        }

        return null;
    }

    public void delete(Integer id){
        String[] where = new String[]{id.toString()};
        db.delete(NAME_TABLE, col.ID+"=?",where);
    }

    public void update(Integer id, ContentValues vals){
        String[] where = new String[]{id.toString()};
        db.update(NAME_TABLE, vals,
                col.ID+"=?",where);
    }

    public ArrayList<String> get_all_elements(int parent) {
        return null;
    }

    public ArrayList<language_model> get_all_elements() {
        ArrayList<language_model> listItems = new ArrayList<language_model>();

        String sql = "SELECT * from " + NAME_TABLE+ " ORDER BY "+ col.NAME+" ASC";

        Cursor cursor = db.rawQuery(sql, new String[] {});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(folder.col.ID));
                String name = cursor.getString(cursor.getColumnIndex(folder.col.NAME));

                listItems.add(new language_model(id, name));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }
    public String get_name(Integer id){
        String sql = "SELECT * from " + NAME_TABLE+ " WHERE "+ col.ID+"="+id.toString();

        Cursor cursor = db.rawQuery(sql, new String[] {});

        String res = "";
        if (cursor.moveToFirst()) {
            do {
                res = cursor.getString(cursor.getColumnIndex(folder.col.NAME));
                break;
            } while (cursor.moveToNext());
        }

        cursor.close();

        return res;
    }

    private boolean exist_language(String name){
        String sql = "SELECT * from "+NAME_TABLE+" WHERE "+col.NAME+" = "+'"'+name+'"';

        Cursor cursor = db.rawQuery(sql, new String[]{});
        boolean res = false;
        if(cursor.moveToFirst()){
            res = true;
        }
        cursor.close();
        return res;
    }



}
