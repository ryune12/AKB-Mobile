package com.calvindoaldisutanto.akbresto.API.Response;

import com.calvindoaldisutanto.akbresto.API.ClientAccess.DetailClientAccess;
import com.calvindoaldisutanto.akbresto.API.ClientAccess.MenuClientAccess;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailResponse {
    @SerializedName("data")
    @Expose
    private List<DetailClientAccess> detail;

    public DetailClientAccess getSingleDetail() {
        return SingleDetail;
    }

    public void setSingleDetail(DetailClientAccess singleDetail) {
        SingleDetail = singleDetail;
    }

    @SerializedName("detail")
    @Expose
    private DetailClientAccess SingleDetail;

    @SerializedName("message")
    @Expose
    private String message;

    public List<DetailClientAccess> getDetail() {
        return detail;
    }

//    private MenuClientAccess SingleMenu() { }

    public void setDetail(List<DetailClientAccess> detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
