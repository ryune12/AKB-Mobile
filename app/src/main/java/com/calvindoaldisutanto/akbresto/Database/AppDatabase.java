package com.calvindoaldisutanto.akbresto.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.calvindoaldisutanto.akbresto.models.Cart;

@Database(entities = {Cart.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();
}