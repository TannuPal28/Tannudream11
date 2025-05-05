package com.elevendreamer.Bean;

import java.io.Serializable;

public class UserDummy implements Serializable {
    private String name;
    private int imageResId;

    public UserDummy(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
