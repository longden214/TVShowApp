package com.quanglong.tvshowapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.quanglong.tvshowapp.dao.TVShowDAO;
import com.quanglong.tvshowapp.models.TVShow;

@Database(entities = TVShow.class, version = 1, exportSchema = false)
public abstract class TVShowDatabase extends RoomDatabase {
    private static TVShowDatabase tvShowDatabase;

    public static synchronized TVShowDatabase getTvShowDatabase(Context context){
        if (tvShowDatabase == null){
            tvShowDatabase = Room.databaseBuilder(
                    context,
                    TVShowDatabase.class,
                    "tv_show_db"
            ).build();
        }

        return tvShowDatabase;
    }

    public abstract TVShowDAO tvShowDAO();
}
