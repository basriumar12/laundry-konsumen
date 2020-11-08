package com.samyotech.laundry.model;

import java.io.Serializable;

/**
 * Created by VARUN on 01/01/19.
 */
public class TicketDTO implements Serializable {


    String tiket_id = "";
    String user_id = "";
    String title = "";
    String description = "";
    String status = "";
    String created_at = "";

    public String getTiket_id() {
        return tiket_id;
    }

    public void setTiket_id(String tiket_id) {
        this.tiket_id = tiket_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
