package com.calvindoaldisutanto.akbresto.API.ClientAccess;

import com.google.gson.annotations.SerializedName;

public class DetailClientAccess {
    @SerializedName("id")
    private String id;

    @SerializedName("id_order")
    private String id_order;

    @SerializedName("nama_menu")
    private String nama_menu;

    @SerializedName("id_menu")
    private String id_menu;

    @SerializedName("kuantitas")
    private String kuantitas;

    @SerializedName("subtotal")
    private String subtotal;

    public DetailClientAccess(String id, String id_order, String id_menu, String kuantitas, String subtotal, String nama_menu) {
        this.id = id;
        this.id_order = id_order;
        this.id_menu = id_menu;
        this.kuantitas = kuantitas;
        this.subtotal = subtotal;
        this.nama_menu = nama_menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(String kuantitas) {
        this.kuantitas = kuantitas;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}
