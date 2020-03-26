package com.example.booking.model;

public class CommentOfCustomer {
    private String avatar;
    private String content;

    public CommentOfCustomer(String avatar, String content) {
        this.avatar = avatar;
        this.content = content;
    }

    public CommentOfCustomer() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
