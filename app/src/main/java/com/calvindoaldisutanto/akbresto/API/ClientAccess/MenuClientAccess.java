package com.calvindoaldisutanto.akbresto.API.ClientAccess;

import com.google.gson.annotations.SerializedName;

public class MenuClientAccess {
    @SerializedName("id")
    private String id;

    @SerializedName("nama_menu")
    private String nama_menu;

    @SerializedName("harga")
    private String harga;

    @SerializedName("kategori")
    private String kategori;

    @SerializedName("image")
    private String image;

    @SerializedName("unit")
    private String unit;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("jumlah")
    private String jumlah;

    @SerializedName("serving_size")
    private String serving_size;

    public MenuClientAccess(String id, String nama_menu, String harga, String kategori, String image, String unit, String deskripsi, String jumlah, String serving_size) {
        this.id = id;
        this.nama_menu = nama_menu;
        this.harga = harga;
        this.kategori = kategori;
        this.image = image;
        this.unit = unit;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.serving_size = serving_size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getServing_size() {
        return serving_size;
    }

    public void setServing_size(String serving_size) {
        this.serving_size = serving_size;
    }
}
