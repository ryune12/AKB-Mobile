package com.calvindoaldisutanto.akbresto.Database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient databaseClient;

    private AppDatabase database;

    private DatabaseClient(Context context){
        this.context = context;
        database = Room.databaseBuilder(context, AppDatabase.class, "order").build();
    }

    public static synchronized DatabaseClient getInstance(Context context){
        if (databaseClient==null){
            databaseClient = new DatabaseClient(context);
        }
        return databaseClient;
    }

    public AppDatabase getDatabase(){
        return database;
    }
}