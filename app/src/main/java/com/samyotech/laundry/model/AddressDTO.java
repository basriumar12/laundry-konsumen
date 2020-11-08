package com.samyotech.laundry.model;

import java.io.Serializable;

public class AddressDTO implements Serializable {
    String address = "";
    int image;


    public AddressDTO(String address, int image) {
        this.address = address;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
