package com.ferit.clowntastic.models;

import java.util.HashMap;
import java.util.Map;

public enum Type {
    CLOWN(0),
    CUSTOMER(1);

    private Integer typeInteger;

    private static Map<Integer, Type> map = new HashMap<Integer, Type>();

    static {
        for (Type typeEnum : Type.values()) {
            map.put(typeEnum.typeInteger, typeEnum);
        }
    }

    private Type(final int typeInt) {
        typeInteger = typeInt;
    }

    public static Type valueOf(int typeInteger) {
        return map.get(typeInteger);
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();

    }

}
