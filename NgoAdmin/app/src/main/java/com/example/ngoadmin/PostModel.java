package com.example.ngoadmin;

public class PostModel {


    private String pushKey;
    private String title;
    private String description;
    private String date;
    private String category;
    private String area;
    private String userUuid;
    private String imageUrl;
    private boolean verify;


    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public PostModel(String title, String description, String date, String category, String area, String userUuid, String imageUrl, boolean verify) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
        this.area = area;
        this.userUuid = userUuid;
        this.imageUrl = imageUrl;
        this.verify = verify;
    }


    public PostModel(String title, String description, String date, String category, String area, String userUuid, boolean verify) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
        this.area = area;
        this.userUuid = userUuid;
        this.verify = verify;
    }

    public PostModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}
