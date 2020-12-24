package com.samyotech.laundry.model;

import java.io.Serializable;

public class ItemServiceDTO implements Serializable {


    String s_no = "";

    String item_id = "";

    String shop_id = "";
    String service_id = "";
    String item_name = "";

    String price = "";

    String image = "";

    String status = "";

    String type = "";

    String created_at = "";

    String updated_at = "";

    String service_name = "";

    String count = "0";

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getType() {
        return type;
    }

    public void setSatuan(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "ItemServiceDTO{" +
                "s_no='" + s_no + '\'' +
                ", item_id='" + item_id + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", service_id='" + service_id + '\'' +
                ", item_name='" + item_name + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", service_name='" + service_name + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
