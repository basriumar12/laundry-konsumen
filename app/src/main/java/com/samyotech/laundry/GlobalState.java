package com.samyotech.laundry;

import android.app.Application;

import com.samyotech.laundry.ModelClass.CategoryDTO;
import com.samyotech.laundry.ModelClass.ItemDTO;
import com.samyotech.laundry.ModelClass.ItemServiceDTO;
import com.samyotech.laundry.ModelClass.PopLaundryDTO;

import java.util.ArrayList;

public class GlobalState extends Application {

    private static GlobalState mInstance;
    ArrayList<ItemServiceDTO> categoryArrayList;

    ItemDTO itemServiceDTO;

    PopLaundryDTO popLaundryDTO;
    String cost="",costbefo="0",discountcost="",promoCode="";
    String quantity="";
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static synchronized GlobalState getInstance() {
        return mInstance;
    }

    public  ItemDTO itemServiceDTO() {
        return itemServiceDTO;
    }




    public void setItem(ItemDTO  itemServiceDTO) {


        this.itemServiceDTO = itemServiceDTO;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public PopLaundryDTO getPopLaundryDTO() {
        return popLaundryDTO;
    }

    public void setPopLaundryDTO(PopLaundryDTO popLaundryDTO) {
        this.popLaundryDTO = popLaundryDTO;
    }


    public String getDiscountcost() {
        return discountcost;
    }

    public void setDiscountcost(String discountcost) {
        this.discountcost = discountcost;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }


    public String getCostbefo() {
        return costbefo;
    }

    public void setCostbefo(String costbefo) {
        this.costbefo = costbefo;
    }
}