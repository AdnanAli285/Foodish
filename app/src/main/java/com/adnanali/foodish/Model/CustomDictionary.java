package com.adnanali.foodish.Model;

import java.io.Serializable;

/**
 * Created by Adnan Ali on 4/25/2016.
 */
public class CustomDictionary {
    private String key;
    private String value;
    private Serializable serValue;
    private boolean boolValue;

    public boolean isBoolValue() {
        return boolValue;
    }

    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }

    public CustomDictionary(String key, Serializable value) {
        this.key = key;
        this.serValue = value;
    }
    public CustomDictionary(String key, String value) {
        this.key = key;
        this.value =  value;
    }
    public CustomDictionary(String key, boolean value) {
        this.key = key;
        this.boolValue =  value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }
    public Serializable getSerializableValue() {
        return serValue;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
