package com.calvindoaldisutanto.akbresto.API.ClientAccess;

import com.google.gson.annotations.SerializedName;

public class OrderClientAccess {
    @SerializedName("id")
    public int id;

    @SerializedName("id_reservasi")
    public int id_reservasi;


    @SerializedName("status")
    public String status;

    @SerializedName("status_pembayaran")
    public String status_pembayaran;

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public void setStatus_pembayaran(String status_pembayaran) {
        this.status_pembayaran = status_pembayaran;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_reservasi() {
        return id_reservasi;
    }

    public void setId_reservasi(int id_reservasi) {
        this.id_reservasi = id_reservasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderClientAccess(int id, int id_reservasi, String status_pembayaran, String status) {
        this.id = id;
        this.status_pembayaran = status_pembayaran;
        this.id_reservasi = id_reservasi;
        this.status = status;
    }
}
