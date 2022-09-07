package com.calvindoaldisutanto.akbresto.API.Response;

import com.calvindoaldisutanto.akbresto.API.ClientAccess.MenuClientAccess;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuResponse {
    @SerializedName("data")
    @Expose
    private List<MenuClientAccess> menu;

    public MenuClientAccess getSingleMenu() {
        return SingleMenu;
    }

    public void setSingleKost(MenuClientAccess singleMenu) {
        SingleMenu = singleMenu;
    }

    @SerializedName("menu")
    @Expose
    private MenuClientAccess SingleMenu;

    @SerializedName("message")
    @Expose
    private String message;

    public List<MenuClientAccess> getMenu() {
        return menu;
    }

//    private MenuClientAccess SingleMenu() { }

    public void setMenu(List<MenuClientAccess> users) {
        this.menu = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
