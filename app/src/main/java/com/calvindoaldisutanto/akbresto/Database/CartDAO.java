package com.calvindoaldisutanto.akbresto.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.calvindoaldisutanto.akbresto.models.Cart;

import java.util.List;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM cartModel")
    List<Cart> getAll();

    @Query("SELECT * FROM cartModel WHERE id_menu LIKE :id ")
    Cart getcart(int id);

    @Insert
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Query("UPDATE cartModel SET kuantitas = :jml WHERE id_menu = :id_menu")
    void updateJumlah(int jml, String id_menu);

    @Query("DELETE FROM cartModel")
    void nukeTable();

    @Delete
    void delete(Cart cart);
}
