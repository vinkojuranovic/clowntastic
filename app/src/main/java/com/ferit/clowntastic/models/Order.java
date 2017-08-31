package com.ferit.clowntastic.models;

import android.content.Context;
import android.graphics.Color;
import com.ferit.clowntastic.R;

public class Order {

    private Long id;

    private Long serverId;

    private String date;

    private Double latitude;

    private Double longtitude;

    private Long clownId;

    private Long customerId;

    private Package aPackage;

    private Boolean status;

    public Order(String date, Double latitude, Double longtitude, Long clownId, Long customerId, Package aPackage, Boolean status) {
        this.date = date;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.clownId = clownId;
        this.customerId = customerId;
        this.aPackage = aPackage;
        this.status = status;
    }

    public Order(Long serverId, String date, Double latitude, Double longtitude, Long clownId, Long customerId, Package aPackage, Boolean status) {
        this.serverId = serverId;
        this.date = date;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.clownId = clownId;
        this.customerId = customerId;
        this.aPackage = aPackage;
        this.status = status;
    }

    public Order(Long id, Long serverId, String date, Double latitude, Double longtitude, Long clownId, Long customerId, Package aPackage, Boolean status) {
        this.id = id;
        this.serverId = serverId;
        this.date = date;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.clownId = clownId;
        this.customerId = customerId;
        this.aPackage = aPackage;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Long getClownId() {
        return clownId;
    }

    public void setClownId(Long clownId) {
        this.clownId = clownId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getColor(Context context) {
        if(this.getStatus()) {
            return context.getResources().getColor(R.color.blue);
        } else {
            return context.getResources().getColor(R.color.red);
        }
    }
}
