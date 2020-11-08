package com.samyotech.laundry.model;

import java.io.Serializable;

public class WelcomeDTO implements Serializable {
    int background;
    String heading = "";
    String desc = "";

    public WelcomeDTO(int background, String heading, String desc) {
        this.background = background;
        this.heading = heading;
        this.desc = desc;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
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
