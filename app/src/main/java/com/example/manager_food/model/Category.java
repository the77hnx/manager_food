package com.example.manager_food.model;

public class Category {
    private static String name;
    private int imageResId;

    public Category(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public static String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
