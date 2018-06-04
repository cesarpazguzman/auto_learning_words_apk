package com.personales.proyectos.autolearningwords.DataBase.Tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Interfaces.tableInterface;
import com.personales.proyectos.autolearningwords.Models.folder_model;

import java.util.ArrayList;

public class folder implements tableInterface {

    public static final String NAME_TABLE = "folder";
    private SQLiteDatabase db;

    public folder(SQLiteDatabase db) {
        this.db = db;
    }

    public class col{
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String PARENT_ID = "parent_id";
    }

    public static final String CREATE_TABLE1 = "CREATE TABLE "+NAME_TABLE+ "("
            +col.ID+" integer primary key autoincrement,"
            +col.NAME+" text not null, "
            +col.PARENT_ID+" integer, "
            +" FOREIGN KEY ("+col.PARENT_ID+") REFERENCES "+ NAME_TABLE+ "("+col.ID+") ON DELETE CASCADE);";

    public static final String CREATE_INDEX1 = "CREATE INDEX "+NAME_TABLE+"_"+col.PARENT_ID+
            "_index ON "+NAME_TABLE+"("+col.PARENT_ID+")";


    public ContentValues values(String name, int parent){
        ContentValues values = new ContentValues();
        values.put(col.NAME, name);
        values.put(col.PARENT_ID, parent);
        return values;
    }

    public itemVisitable insert(ContentValues vals){
        db.insert(NAME_TABLE, null, vals);

        Cursor cursor = db.rawQuery("SELECT "+col.ID+" from "+NAME_TABLE+" order by "+col.ID+ " desc limit 1",
                new String[] {});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(col.ID));

            return new folder_model(id, vals.getAsString(col.NAME),
                    vals.getAsInteger(col.PARENT_ID));
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

    public ArrayList<itemVisitable> getAllFolders(int parent) {
        ArrayList<itemVisitable> listItems = new ArrayList<itemVisitable>();

        String sql = "SELECT * from " + NAME_TABLE;
        sql+=" WHERE "+col.PARENT_ID+"="+parent;

        Cursor cursor = db.rawQuery(sql, new String[] {});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(col.ID));
                String name = cursor.getString(cursor.getColumnIndex(col.NAME));
                int parent_id = cursor.getInt(cursor.getColumnIndex(col.PARENT_ID));

                listItems.add(new folder_model(id, name, parent_id));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }



    /*public Cursor cargarCursorFolders(){
        String[] columnas = new String[]{T1.ID, T1.NAME, T1.PARENT_ID};

        return db.query(T1.NAME_TABLE, columnas, null, null, null, null, null);
    }*/
}
