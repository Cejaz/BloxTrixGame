package com.example.bloxtrixgame.modelos;

import com.example.bloxtrixgame.modelos.BlockNineModelo;
import com.example.bloxtrixgame.modelos.TipoJuego;
import com.example.bloxtrixgame.presentacion.modeloJuego;

public class ModeloJuegoFactory {
    private ModeloJuegoFactory() {
    }
    public static modeloJuego newGameModel(TipoJuego gameType) {
        switch (gameType) {
            case BLOCKNINE:
                return new BlockNineModelo();
            default:
                return null;
        }}
}




