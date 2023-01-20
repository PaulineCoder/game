package com.game.util;

public class PlayerErrorResponse {
    private String message;

    public PlayerErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
