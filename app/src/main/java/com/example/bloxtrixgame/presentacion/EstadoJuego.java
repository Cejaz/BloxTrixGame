package com.example.bloxtrixgame.presentacion;

public enum EstadoJuego {

    INICIO("INICIO"),
    JUGANDO("JUGANDO"),
    TERMINADO("JUEGO TERMINADO"),
    PAUSA("JUEGO PAUSADO");

    private final String mValue;

    EstadoJuego(String value){
        mValue=value;
    }

    public String ObtenerValue(){
        return mValue;
    }
}
