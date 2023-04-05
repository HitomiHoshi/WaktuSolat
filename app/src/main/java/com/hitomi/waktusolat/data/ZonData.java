package com.hitomi.waktusolat.data;

public class ZonData {
    public String id;
    public String value;

    public ZonData(String value, String id){
        this.value = value;
        this.id = id;
    }

    public String toString() {
        return value;
    }
}
