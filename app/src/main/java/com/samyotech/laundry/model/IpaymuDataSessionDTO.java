package com.samyotech.laundry.model;

import java.io.Serializable;

public class IpaymuDataSessionDTO implements Serializable {
    public String SessionID;
    public String Url;

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @Override
    public String toString() {
        return "IpaymuDataSessionDTO{" +
                "SessionID='" + SessionID + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
