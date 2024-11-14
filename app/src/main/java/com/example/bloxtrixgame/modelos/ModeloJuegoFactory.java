package com.example.bloxtrixgame.modelos;

import com.example.bloxtrixgame.presentacion.modeloJuego;

public class ModeloJuegoFactory {
    public static modeloJuego newGameModel(TipoJuego tipo) {
        if (tipo == TipoJuego.BLOCKNINE) {
            return new BlockNineModelo(); // Devuelve un modelo compatible con modeloJuego
        }
        return null;
    }
}
