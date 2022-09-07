package com.calvindoaldisutanto.akbresto.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Menu")
public class Menu  implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nama_menu")
    public int nama_menu;

    @ColumnInfo(name = "harga")
    public Double harga;

    @ColumnInfo(name = "kategori")
    public String kategori;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "stok")
    private Integer stok;

    @ColumnInfo(name = "deskripsi")
    private String deskripsi;
}
