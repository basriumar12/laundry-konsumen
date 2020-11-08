package com.samyotech.laundry.model;

import java.io.Serializable;

public class ShopServicesDTO implements Serializable {


    String service_name = "";
    String description = "";
    String image = "";

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
