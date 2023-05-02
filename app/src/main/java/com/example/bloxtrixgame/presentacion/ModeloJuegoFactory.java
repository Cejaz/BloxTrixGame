package com.example.bloxtrixgame.presentacion;

import com.example.bloxtrixgame.modelos.BlockNineModelo;
import com.example.bloxtrixgame.modelos.TipoJuego;

public class ModeloJuegoFactory {
    private ModeloJuegoFactory() {
    }
    public static modeloJuego nuevoModeloJuego(TipoJuego tipo){

        switch (tipo){
            case BLOCKNINE:
                return new BlockNineModelo();
            default:
                return null;
        }
    }

}
