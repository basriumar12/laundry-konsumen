package com.samyotech.laundry.model;

import java.io.Serializable;

public class TicketCommentDTO implements Serializable {

    String tiket_detail_id = "";
    String tiket_id = "";
    String message = "";
    String is_admin = "";
    String is_read = "";
    String created_at = "";
    String updated_at = "";

    public String getTiket_detail_id() {
        return tiket_detail_id;
    }

    public void setTiket_detail_id(String tiket_detail_id) {
        this.tiket_detail_id = tiket_detail_id;
    }

    public String getTiket_id() {
        return tiket_id;
    }

    public void setTiket_id(String tiket_id) {
        this.tiket_id = tiket_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
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
}
