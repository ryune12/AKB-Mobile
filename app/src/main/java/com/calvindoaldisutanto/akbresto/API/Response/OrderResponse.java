package com.calvindoaldisutanto.akbresto.API.Response;

import com.calvindoaldisutanto.akbresto.API.ClientAccess.OrderClientAccess;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {
    @SerializedName("data")
    @Expose
    private List<OrderClientAccess> order;

    public OrderClientAccess getSingleOrder() {
        return SingleOrder;
    }

    public void setSingleOrder(OrderClientAccess singleOrder) {
        SingleOrder = singleOrder;
    }

    @SerializedName("order")
    @Expose
    private OrderClientAccess SingleOrder;

    @SerializedName("message")
    @Expose
    private String message;

    public List<OrderClientAccess> getOrder() {
        return order;
    }

    public void setOrder(List<OrderClientAccess> orders) {
        this.order = orders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
