package com.samyotech.laundry.model;

import java.io.Serializable;

public class IpaymuDTO implements Serializable {
    public int status;
    public String message;
    public IpaymuDataDTO data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IpaymuDataDTO getData() {
        return data;
    }

    public void setData(IpaymuDataDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IpaymuDTO{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
