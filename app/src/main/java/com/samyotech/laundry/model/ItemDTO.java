package com.samyotech.laundry.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemDTO implements Serializable {


    String currency_code = "";
    ArrayList<ItemListDTO> item_list = new ArrayList<>();

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public ArrayList<ItemListDTO> getItem_list() {
        return item_list;
    }

    public void setItem_list(ArrayList<ItemListDTO> item_list) {
        this.item_list = item_list;
    }
}
