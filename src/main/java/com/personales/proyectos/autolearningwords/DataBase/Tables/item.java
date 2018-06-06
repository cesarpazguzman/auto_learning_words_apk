package com.personales.proyectos.autolearningwords.DataBase.Tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Interfaces.tableInterface;
import com.personales.proyectos.autolearningwords.Models.folder_model;
import com.personales.proyectos.autolearningwords.Models.item_model;

import java.util.ArrayList;

public class item implements tableInterface {

    public static final String NAME_TABLE = "item";

    public class col{
        public static final String ID = "_id";
        public static final String ORIGINAL = "original";
        public static final String TRANSLATION = "translation";
        public static final String FOLDER_ID = "folder_id";
        public static final String COMMENT = "comment";
        public static final String EXAMPLE1= "example_1";
        public static final String EXAMPLE2= "example_2";
    }

    public static final String CREATE_TABLE2 = "CREATE TABLE "+NAME_TABLE+ "("
            +col.ID+" integer primary key autoincrement,"
            +col.ORIGINAL+" text not null, "
            +col.TRANSLATION+" text not null, "
            +col.FOLDER_ID+" integer, "
            +col.COMMENT+" text, "
            +col.EXAMPLE1+" text, "
            +col.EXAMPLE2+" text, "
            +" FOREIGN KEY ("+col.FOLDER_ID+") REFERENCES "+ folder.NAME_TABLE+ "("+folder.col.ID+"));";

    private SQLiteDatabase db;

    public item(SQLiteDatabase db) {
        this.db = db;
    }

    public static final String CREATE_INDEX2 = "CREATE INDEX "+NAME_TABLE+"_"+col.FOLDER_ID+
            "_index ON "+NAME_TABLE+"("+col.FOLDER_ID+")";

    public ContentValues values(String original, String traduccion, int parent, String comment,
                                String example1, String example2){
        ContentValues values = new ContentValues();
        values.put(col.ORIGINAL, original);
        values.put(col.TRANSLATION, traduccion);
        values.put(col.FOLDER_ID, parent);
        if(!comment.isEmpty())
            values.put(col.COMMENT, comment);
        if(!example1.isEmpty())
            values.put(col.EXAMPLE1, example1);
        if(!example2.isEmpty())
            values.put(col.EXAMPLE2, example2);
        return values;
    }

    public itemVisitable insert(ContentValues vals){
        db.insert(NAME_TABLE, null, vals);

        Cursor cursor = db.rawQuery("SELECT "+ col.ID+" from "+NAME_TABLE+" order by "+ col.ID+ " desc limit 1",
                new String[] {});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(col.ID));
            return new item_model(id, vals.getAsString(col.ORIGINAL),
                    vals.getAsString(col.TRANSLATION),
                    vals.getAsInteger(col.FOLDER_ID),
                    vals.getAsString(col.COMMENT),
                    vals.getAsString(col.EXAMPLE1),
                    vals.getAsString(col.EXAMPLE2));
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
        sql+=" WHERE "+ col.FOLDER_ID+"="+parent;
        Cursor cursor = db.rawQuery(sql, new String[] {});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(folder.col.ID));
                String original = cursor.getString(cursor.getColumnIndex(col.ORIGINAL));
                String traduccion = cursor.getString(cursor.getColumnIndex(col.TRANSLATION));
                System.out.println("Original: "+original);
                String comment = "", example1 = "", example2 = "";
                if(cursor.getColumnIndex(col.COMMENT)!=-1)
                    comment = cursor.getString(cursor.getColumnIndex(col.COMMENT));
                if(cursor.getColumnIndex(col.EXAMPLE1)!=-1)
                    example1 = cursor.getString(cursor.getColumnIndex(col.EXAMPLE1));
                if(cursor.getColumnIndex(col.EXAMPLE2)!=-1)
                    example2 = cursor.getString(cursor.getColumnIndex(col.EXAMPLE2));

                int parent_id = cursor.getInt(cursor.getColumnIndex(col.FOLDER_ID));

                listItems.add(new item_model(id, original, traduccion, parent_id,
                        comment, example1, example2));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return listItems;
    }

}
