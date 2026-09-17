package org.example.proiect_sgbd_vizionare_filme.DAO;

import java.sql.Date;

public interface ViewDAO {

    void createView(int id_user, int id_version, int watched_minutes);
    int getPreviousWatchedMinutes(int id_user, int id_version);
    int getLastViewIdForMovie(int id_user, int id_movie);

}
