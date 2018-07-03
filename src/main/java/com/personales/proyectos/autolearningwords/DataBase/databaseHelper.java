package com.personales.proyectos.autolearningwords.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.personales.proyectos.autolearningwords.DataBase.Tables.folder;
import com.personales.proyectos.autolearningwords.DataBase.Tables.item;
import com.personales.proyectos.autolearningwords.DataBase.Tables.language;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "customWords.db";
    private static final int DATABASE_SCHEME_VERSION = 1;

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        //Se ejecuta cuando se crea la BD. Aqui creamos las tablas
        db.execSQL(language.CREATE_TABLE);
        db.execSQL(folder.CREATE_TABLE1);
        db.execSQL(item.CREATE_TABLE2);

        //Creación de índices
        db.execSQL(folder.CREATE_INDEX1);
        db.execSQL(item.CREATE_INDEX1);
        db.execSQL(folder.CREATE_INDEX2);
        db.execSQL(item.CREATE_INDEX2);

        System.out.println("CREA TABLA");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Realiza las diferentes modificaciones para la siguiente version de la BD
        //añadir un campo, modificando un campo, etc.

    }
}
