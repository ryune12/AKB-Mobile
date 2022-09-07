package com.calvindoaldisutanto.akbresto.models;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

@Entity(tableName = "cartModel")
public class Cart {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "id_menu")
    public String id_menu;

    @ColumnInfo(name = "nama_menu")
    public String nama_menu;

    @ColumnInfo(name = "harga")
    public double harga;

    @ColumnInfo(name = "kategori")
    public String kategori;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "jumlah")
    private int jumlah;

    @ColumnInfo(name = "kuantitas")
    private int kuantitas;

    public Cart(String id_menu, String nama_menu, double harga, String kategori, String image, int jumlah, int serving_size, String deskripsi, int kuantitas) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.harga = harga;
        this.kategori = kategori;
        this.image = image;
        this.jumlah = jumlah;
        this.serving_size = serving_size;
        this.deskripsi = deskripsi;
        this.kuantitas = kuantitas;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getServing_size() {
        return serving_size;
    }

    public void setServing_size(int serving_size) {
        this.serving_size = serving_size;
    }

    @ColumnInfo(name = "serving_size")
    private int serving_size;

    @ColumnInfo(name = "deskripsi")
    private String deskripsi;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String imgURL){
        Glide.with(imageView)
                .load(imgURL)
                .into(imageView);
    }
}
