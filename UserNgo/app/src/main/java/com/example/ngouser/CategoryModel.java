package com.example.ngouser;

public class CategoryModel {

    private String categoryName;
    private String categoryPushKey;

    public CategoryModel(String categoryName, String categoryPushKey) {
        this.categoryName = categoryName;
        this.categoryPushKey = categoryPushKey;
    }

    public CategoryModel() {
    }


    public CategoryModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryPushKey() {
        return categoryPushKey;
    }

    public void setCategoryPushKey(String categoryPushKey) {
        this.categoryPushKey = categoryPushKey;
    }
}

