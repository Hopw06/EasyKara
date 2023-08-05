package com.example.easykara.common;

public enum PlayStatus {
    IDLE("Idle"),
    PLAYING("Playing"),
    PLAYED("Played");

    private String status;

    PlayStatus(String status) {
        this.status = status;
    }
}
