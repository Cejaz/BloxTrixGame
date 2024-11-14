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

    private Puntos[][] mPoints;
    private int mCellSize;
    private int mGameWidth;
    private int mGameHeight;
    private final Paint mPaint = new Paint();

    public marcojuego(Context context) {
        super(context);
    }

    public marcojuego(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(int gameWidth, int gameHeight) {
        mGameWidth = gameWidth;
        mGameHeight = gameHeight;
        getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int width = getWidth();
            int height = getHeight();
            mCellSize = Math.min(width / mGameWidth, height / mGameHeight);
        });
    }

    void setPoints(Puntos[][] points) {
        mPoints = points;
        invalidate(); // Refresca la vista después de actualizar los puntos
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Limpia el fondo
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

        if (mPoints == null) return;

        // Dibuja las piezas
        for (int i = 0; i < mGameHeight; i++) {
            for (int j = 0; j < mGameWidth; j++) {
                Puntos point = mPoints[i][j];
                int left = j * mCellSize;
                int top = i * mCellSize;
                int right = left + mCellSize;
                int bottom = top + mCellSize;

                if (point != null && point.type == Puntos.Type.BOX) {
                    mPaint.setColor(Color.GREEN);
                    canvas.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }
}
