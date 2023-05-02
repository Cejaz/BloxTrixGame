package com.example.bloxtrixgame.vistas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.bloxtrixgame.presentacion.Puntos;

public class marcojuego extends View {
    public marcojuego(Context context) {
        super(context);
    }

    public marcojuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public marcojuego(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public marcojuego(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Puntos [][] mPuntos;
    private int mEspacioCaja;
    private int mRellenoCaja;
    private int mEspacioJuego;

    private final Paint mPaint = new Paint();

    public void inicio(int espacioJuego){
        mEspacioJuego = espacioJuego;
        getViewTreeObserver().addOnGlobalLayoutListener(() ->{
            mRellenoCaja = Math.min(getWidth(), getHeight()) / mEspacioJuego;
            mRellenoCaja = mEspacioCaja / 10;
        });
    }

    void enviarPuntos(Puntos [][] puntos){
        mPuntos = puntos;
    }

    private Puntos obtenerPuntos(int x, int y){
        return mPuntos[y][x];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0,0, mEspacioJuego, mEspacioJuego, mPaint);
        if(mPaint == null ) {
            return;
        }
        for (int i = 0; i < mEspacioJuego; i++){
            for (int j=0; j<mEspacioJuego; j++){
                Puntos puntos = obtenerPuntos(i, j);
                int izquierda, derecha, arriba, abajo, boton;
                mPaint.setColor(Color.WHITE);
                switch (puntos.tipo) {
                    case CAJA:
                        izquierda = mEspacioCaja * puntos.x + mRellenoCaja;
                        derecha = izquierda + mEspacioCaja - mRellenoCaja;
                        arriba = mEspacioCaja * puntos.y + mRellenoCaja;
                        boton = arriba + mEspacioCaja - mRellenoCaja;
                        break;
                        case LINEA_VERTICAL:
                        izquierda = mEspacioCaja * puntos.x;
                        derecha = izquierda + mRellenoCaja;
                        arriba = mEspacioCaja * puntos.y;
                        boton = arriba + mEspacioCaja;
                        break;
                    case LINEA_HORIZONTAL:
                        izquierda = mEspacioCaja + puntos.y;
                        derecha = izquierda + mEspacioCaja;
                        arriba = mEspacioCaja * puntos.y;
                        boton = arriba + mRellenoCaja;
                        break;
                    case VACIO:
                    default:
                        izquierda = mEspacioCaja * puntos.x;
                        derecha = izquierda +mEspacioCaja;
                        arriba = mEspacioCaja * puntos.y;
                        boton = arriba + mEspacioCaja;
                        mPaint.setColor(Color.BLACK);
                }
                canvas.drawRect(izquierda,arriba,derecha,boton,mPaint);
            }
        }
    }
}
