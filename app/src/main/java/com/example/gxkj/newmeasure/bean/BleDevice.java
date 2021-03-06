package com.example.gxkj.newmeasure.bean;

/**
 * Created by Endless on 2017/10/16.
 */

public class BleDevice {
    private String name;
    private String address;
    private int rssi;

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BleDevice(String n, String u, int r) {
        name = n;
        address = u;
        rssi = r;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
