package com.samyotech.laundry.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BookingDTO implements Serializable {

    ArrayList<OrderListDTO> order_list = new ArrayList<>();

    public ArrayList<OrderListDTO> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<OrderListDTO> order_list) {
        this.order_list = order_list;
    }
}
