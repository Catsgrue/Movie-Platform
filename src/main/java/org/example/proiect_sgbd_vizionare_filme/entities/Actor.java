package org.example.proiect_sgbd_vizionare_filme.entities;

import java.sql.Date;

public class Actor {
    private int id_actor;
    private String stage_name;
    private String first_name;
    private String last_name;
    private Date date_of_birth;

    public Actor(int id_actor, String stage_name, String first_name, String last_name, Date date_of_birth) {
        this.id_actor = id_actor;
        this.stage_name = stage_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
    }

    public String getStage_name() {
        return stage_name;
    }

    public int getId_actor() {
        return id_actor;
    }
}
