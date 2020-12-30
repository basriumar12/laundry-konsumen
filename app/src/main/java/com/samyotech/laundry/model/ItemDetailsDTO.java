package com.samyotech.laundry.model;

import java.io.Serializable;

public class ItemDetailsDTO implements Serializable {


    String item_id = "";
    String item_name = "";
    String price = "";
    String image = "";
    String quantity = "";
    String s_no = "";
    String status = "";
    String created_at = "";
    String updated_at = "";
    String service_name = "";

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    @Override
    public String toString() {
        return "ItemDetailsDTO{" +
                "item_id='" + item_id + '\'' +
                ", item_name='" + item_name + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", quantity='" + quantity + '\'' +
                ", s_no='" + s_no + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", service_name='" + service_name + '\'' +
                '}';
    }
}
