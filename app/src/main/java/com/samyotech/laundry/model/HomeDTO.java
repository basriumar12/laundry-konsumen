package com.samyotech.laundry.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDTO implements Serializable {

    ArrayList<GetBannerDTO> advertis = new ArrayList<>();
    ArrayList<ServicesDTO> service = new ArrayList<>();
    ArrayList<PopLaundryDTO> laundry = new ArrayList<>();
    ArrayList<SpecialOfferPkgDTO> offer = new ArrayList<>();
    ArrayList<NearBYDTO> near_by = new ArrayList<>();


    public ArrayList<GetBannerDTO> getAdvertis() {
        return advertis;
    }

    public void setAdvertis(ArrayList<GetBannerDTO> advertis) {
        this.advertis = advertis;
    }

    public ArrayList<ServicesDTO> getService() {
        return service;
    }

    public void setService(ArrayList<ServicesDTO> service) {
        this.service = service;
    }

    public ArrayList<PopLaundryDTO> getLaundry() {
        return laundry;
    }

    public void setLaundry(ArrayList<PopLaundryDTO> laundry) {
        this.laundry = laundry;
    }

    public ArrayList<SpecialOfferPkgDTO> getOffer() {
        return offer;
    }

    public void setOffer(ArrayList<SpecialOfferPkgDTO> offer) {
        this.offer = offer;
    }

    public ArrayList<NearBYDTO> getNear_by() {
        return near_by;
    }

    public void setNear_by(ArrayList<NearBYDTO> near_by) {
        this.near_by = near_by;
    }
}
