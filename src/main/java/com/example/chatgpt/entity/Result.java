package com.example.chatgpt.entity;

public class Result<Body> {
    boolean success;
    Body body;

    public Result(boolean success, Body body) {
        this.success = success;
        this.body = body;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public Body getBody() {
        return this.body;
    }
}
