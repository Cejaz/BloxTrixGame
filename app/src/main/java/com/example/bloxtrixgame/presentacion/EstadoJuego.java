package com.example.bloxtrixgame.presentacion;

public enum EstadoJuego {

    START("START"),
    PLAYING("PLAYING"),
    OVER("GAME OVER"),
    PAUSED("GAME PAUSED");

    private final String value;

    EstadoJuego(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

