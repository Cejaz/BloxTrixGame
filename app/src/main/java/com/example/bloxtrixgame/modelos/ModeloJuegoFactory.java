package com.example.bloxtrixgame.modelos;
import com.example.bloxtrixgame.modelos.BlockNineModelo;
import com.example.bloxtrixgame.modelos.TipoJuego;
import com.example.bloxtrixgame.presentacion.modeloJuego;

public class ModeloJuegoFactory {
    public static BlockNineModelo newGameModel(TipoJuego tipo) {
        if (tipo == TipoJuego.BLOCKNINE) {
            return new BlockNineModelo(); // Retornando BlockNineModelo directamente
        }
        return null;
    }
}
