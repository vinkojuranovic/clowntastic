package com.ferit.clowntastic.models;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public enum Package {
    SMALL(0, 99.99),
    MEDIUM(1, 350.00),
    BIG(2, 700.01);

    private final Integer packageNumber;
    private final Double price;

    Package(Integer packageNumber, Double price) {
        this.packageNumber = packageNumber;
        this.price = price;
    }

    private static Map<Integer, Package> map = new HashMap<>();

    static {
        for (Package packageEnum : Package.values()) {
            map.put(packageEnum.packageNumber, packageEnum);
        }
    }

    public static Package valueOf(int packageNumber) {
        return map.get(packageNumber);
    }

    @Override
    public String toString() {
        return super.toString().toUpperCase() + " " + String.valueOf(price) + " €";
    }

    public Integer getId() {
        return packageNumber;
    }

}
