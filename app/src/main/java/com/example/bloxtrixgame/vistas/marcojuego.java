package com.example.bloxtrixgame.vistas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.bloxtrixgame.presentacion.Puntos;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    private Puntos[][] mPoints;
    private int mCellSize;
    private int mGameWidth;
    private int mGameHeight;


    private final Paint mPaint = new Paint();

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
    }

    private Puntos getPoint(int x, int y) {
        return mPoints[y][x];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        if (mPoints == null) {
            return;
        }
        for (int i = 0; i < mGameHeight; i++) {
            for (int j = 0; j < mGameWidth; j++) {
                Puntos point = getPoint(j, i);
                int left = j * mCellSize;
                int top = i * mCellSize;
                int right = left + mCellSize;
                int bottom = top + mCellSize;

                switch (point.type) {
                    case BOX:
                        List<Integer> colorList = Arrays.asList(
                                Color.RED,
                                Color.BLUE,
                                Color.GREEN,
                                Color.YELLOW,
                                Color.MAGENTA
                        );
                        Random random = new Random();
                        int randomColor = colorList.get(random.nextInt(colorList.size()));
                        mPaint.setColor(randomColor);
                        int padding = mCellSize / 10;
                        canvas.drawRect(left + padding, top + padding, right - padding, bottom - padding, mPaint);
                        break;
                    case VERTICAL_LINE:
                        mPaint.setColor(Color.GREEN);
                        int lineWidth = mCellSize / 10;
                        int lineLeft = left + mCellSize / 2 - lineWidth / 2;
                        int lineRight = lineLeft + lineWidth;
                        canvas.drawRect(lineLeft, top, lineRight, bottom, mPaint);
                        break;

                    case HORIZONTAL_LINE:
                        mPaint.setColor(Color.GREEN);
                        int lineHeight = mCellSize / 10;
                        int lineTop = top + mCellSize / 2 - lineHeight / 2;
                        int lineBottom = lineTop + lineHeight;
                        canvas.drawRect(left, lineTop, right, lineBottom, mPaint);
                        break;
                    case EMPTY:
                    default:
                        mPaint.setColor(Color.BLACK);
                        canvas.drawRect(left, top, right, bottom, mPaint);
                        mPaint.setColor(Color.WHITE);
                        canvas.drawLine(left, top, right, top, mPaint); // Línea superior
                        canvas.drawLine(left, top, left, bottom, mPaint); // Línea izquierda
                        canvas.drawLine(right, top, right, bottom, mPaint); // Línea derecha
                        canvas.drawLine(left, bottom, right, bottom, mPaint); // Línea inferior


                        break;
                }
            }
        }
    }

}
