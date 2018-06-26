package com.personales.proyectos.autolearningwords.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static private session _instance;


    private session(Context ctx){
        prefs = ctx.getSharedPreferences("tab_app",Context.MODE_PRIVATE);
        editor = prefs.edit();

    }

    public static session getInstance(final Context context){
        if(_instance==null){
            _instance = new session(context);
        }
        return _instance;
    }

    public static session getInstance(){
        return _instance;
    }

    public void set_language_translation(int language_id){
        editor.putInt("translation_to",language_id);
        editor.commit();
    }

    public int get_language_translation(){
        return prefs.getInt("translation_to",-1);
    }
}