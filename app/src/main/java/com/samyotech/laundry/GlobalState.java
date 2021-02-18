package com.samyotech.laundry;

import android.app.Application;

import com.samyotech.laundry.helper.AppSignatureHelper;
import com.samyotech.laundry.model.ItemDTO;
import com.samyotech.laundry.model.ItemServiceDTO;
import com.samyotech.laundry.model.PopLaundryDTO;

import java.util.ArrayList;

public class GlobalState extends Application {

    private static GlobalState mInstance;
    ArrayList<ItemServiceDTO> categoryArrayList;

    ItemDTO itemServiceDTO;

    PopLaundryDTO popLaundryDTO;
    String cost = "", costbefo = "0", discountcost = "", promoCode = "";
    String quantity = "";

    public static synchronized GlobalState getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();

    }

    public ItemDTO itemServiceDTO() {
        return itemServiceDTO;
    }


    public void setItem(ItemDTO itemServiceDTO) {

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