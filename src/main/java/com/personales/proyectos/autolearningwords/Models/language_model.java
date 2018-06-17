package com.personales.proyectos.autolearningwords.Models;

public class language_model {
    private String name;
    private int id;

    public language_model(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
