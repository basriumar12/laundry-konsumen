package com.samyotech.laundry.ModelClass;

import java.io.Serializable;

public class WelcomeDTO implements Serializable {
    int topImage;
    int btmImage;
    String heading="";
    String desc="";

    public WelcomeDTO(int topImage, int btmImage, String heading, String desc) {
        this.topImage = topImage;
        this.btmImage = btmImage;
        this.heading = heading;
        this.desc = desc;
    }

    public int getTopImage() {
        return topImage;
    }

    public void setTopImage(int topImage) {
        this.topImage = topImage;
    }

    public int getBtmImage() {
        return btmImage;
    }

    public void setBtmImage(int btmImage) {
        this.btmImage = btmImage;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
