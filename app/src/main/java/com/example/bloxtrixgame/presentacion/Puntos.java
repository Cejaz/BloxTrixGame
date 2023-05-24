package com.example.bloxtrixgame.presentacion;

import android.graphics.Point;

public class Puntos {
    public final int x, y;
    public boolean isFallingPoint;
    public TipodePunto type;
    public int color;

    public Puntos(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = TipodePunto.EMPTY;
        this.isFallingPoint = false;
    }

    public Puntos(int x, int y, TipodePunto type, boolean isFallingPoint) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.isFallingPoint = isFallingPoint;
    }

    public boolean isStablePoint() {
        return !isFallingPoint && type == TipodePunto.BOX;
    }

}
