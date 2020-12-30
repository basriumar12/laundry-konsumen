package com.samyotech.laundry.model;

import java.io.Serializable;

public class IpaymuDataDTO implements Serializable {
    public int Status;
    public String Message;
    public IpaymuDataSessionDTO Data;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public IpaymuDataSessionDTO getData() {
        return Data;
    }

    public void setData(IpaymuDataSessionDTO data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "IpaymuDataDTO{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", Data=" + Data +
                '}';
    }
}
