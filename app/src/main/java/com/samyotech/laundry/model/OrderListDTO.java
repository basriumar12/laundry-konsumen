package com.samyotech.laundry.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderListDTO implements Serializable {


    String s_no = "";
    String order_id = "";
    String user_id = "";
    String shop_id = "";
    String price = "";
    String discount = "";
    String final_price = "";
    String currency_code = "";
    String payment_status = "";
    String order_status = "";
    ArrayList<ItemDetailsDTO> item_details = new ArrayList<>();
    String shipping_address = "";
    String landmark = "";
    String latitude = "";
    String longitude = "";
    String pickup_date = "";
    String pickup_time = "";
    String delivery_date = "";
    String delivery_time = "";
    String status = "";
    String created_at = "";
    String updated_at = "";
    String shop_name = "";
    String shop_image = "";
    String service_name = "";

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public ArrayList<ItemDetailsDTO> getItem_details() {
        return item_details;
    }

    public void setItem_details(ArrayList<ItemDetailsDTO> item_details) {
        this.item_details = item_details;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
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

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    @Override
    public String toString() {
        return "OrderListDTO{" +
                "s_no='" + s_no + '\'' +
                ", order_id='" + order_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", price='" + price + '\'' +
                ", discount='" + discount + '\'' +
                ", final_price='" + final_price + '\'' +
                ", currency_code='" + currency_code + '\'' +
                ", payment_status='" + payment_status + '\'' +
                ", order_status='" + order_status + '\'' +
                ", item_details=" + item_details +
                ", shipping_address='" + shipping_address + '\'' +
                ", landmark='" + landmark + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", pickup_date='" + pickup_date + '\'' +
                ", pickup_time='" + pickup_time + '\'' +
                ", delivery_date='" + delivery_date + '\'' +
                ", delivery_time='" + delivery_time + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", shop_image='" + shop_image + '\'' +
                ", service_name='" + service_name + '\'' +
                '}';
    }
}
