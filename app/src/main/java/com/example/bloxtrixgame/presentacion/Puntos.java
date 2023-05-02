package com.example.bloxtrixgame.presentacion;

import android.graphics.Point;

public class Puntos {
    public final int x,y;
    public boolean puntodeCaida;
    public TipodePunto tipo;

    public Puntos(int x, int y){
        this.x=x;
        this.y=y;
        this.tipo = TipodePunto.VACIO;
        this.puntodeCaida = false;

    }

    public Puntos(int x,int y, TipodePunto tipo, boolean puntodeCaida){
        this.x=x;
        this.y=y;
        this.tipo = tipo;
        this.puntodeCaida = puntodeCaida;
    }
    public boolean puntosEstables(){
        return !puntodeCaida && tipo == TipodePunto.CAJA;
    }
}
