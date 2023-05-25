package com.example.bloxtrixgame.modelos;



import android.graphics.Color;
import android.os.Handler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bloxtrixgame.presentacion.EstadoJuego;
import com.example.bloxtrixgame.presentacion.PresenterCompletableObserver;
import com.example.bloxtrixgame.presentacion.PresenterObserver;
import com.example.bloxtrixgame.presentacion.Puntos;
import com.example.bloxtrixgame.presentacion.TipodePunto;
import com.example.bloxtrixgame.presentacion.TurnoJuego;
import com.example.bloxtrixgame.presentacion.modeloJuego;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;



public class BlockNineModelo implements modeloJuego {

    private static final String TAG = "TetrisGameModel";

    private static final int GAME_SIZE = 25;
    private static final int PLAYING_AREA_WIDTH = 10;
    private static final int PLAYING_AREA_HEIGHT = 25;
    private static final int UPCOMING_AREA_SIZE = 4;




    private Puntos[][] mPoints;
    private Puntos[][] mPlayingPoints;
    private Puntos[][] mUpcomingPoints;
    private int mScore;
    private int finalScore;
    private final AtomicBoolean mIsGamePaused = new AtomicBoolean();
    private final AtomicBoolean mIsTurning = new AtomicBoolean();
    private final LinkedList<Puntos> mFallingPoints = new LinkedList<>();

    private PresenterCompletableObserver mGameOverObserver;
    private PresenterObserver<Integer> mScoreUpdatedObserver;

    private final Handler mHandler = new Handler();
    private DatabaseReference mDatabase;
    private EstadoJuego stateGame;


    private enum BrickType {
        L(0), T(1), S(2), STICK(3), SQUARE(4), Z(5),J(6);
        final int value;

        BrickType(int value) {
            this.value = value;
        }

        static BrickType fromValue(int value) {
            switch (value) {
                case 1:
                    return T;
                case 2:
                    return S;
                case 3:
                    return STICK;
                case 4:
                    return SQUARE;
                case 5:
                    return Z;
                case 6:
                    return J;
                case 0:
                default:
                    return L;
            }
        }

        static BrickType random() {
            Random random = new Random();
            return fromValue(random.nextInt(7));
        }
    }

    @Override
    public void init() {
        mPoints = new Puntos[GAME_SIZE][GAME_SIZE];
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                mPoints[i][j] = new Puntos(j, i);
            }
        }
        mPlayingPoints = new Puntos[PLAYING_AREA_HEIGHT][PLAYING_AREA_WIDTH];
        for (int i = 0; i < PLAYING_AREA_HEIGHT; i++) {
            System.arraycopy(mPoints[i], 0, mPlayingPoints[i], 0, PLAYING_AREA_WIDTH);
        }
        mUpcomingPoints = new Puntos[UPCOMING_AREA_SIZE][UPCOMING_AREA_SIZE];
        for (int i = 0; i < UPCOMING_AREA_SIZE; i++) {
            for (int j = 0; j < UPCOMING_AREA_SIZE; j++) {
                mUpcomingPoints[i][j] = mPoints[1 + i][PLAYING_AREA_WIDTH + 1 + j];
            }
        }
        for (int i = 0; i < PLAYING_AREA_HEIGHT; i++) {
            mPoints[i][PLAYING_AREA_WIDTH].type = TipodePunto.VERTICAL_LINE;
        }
        newGame();
    }


    @Override
    public int getGameSize() {
        return GAME_SIZE;
    }

    @Override
    public void newGame() {
        mScore = 0;
        for (int i = 0; i < PLAYING_AREA_HEIGHT; i++) {
            for (int j = 0; j < PLAYING_AREA_WIDTH; j++) {
                mPlayingPoints[i][j].type = TipodePunto.EMPTY;
            }
        }
        mFallingPoints.clear();
        generateUpcomingBrick();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.child("scores").push().setValue(finalScore)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Score saved successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to save score: " + e.getMessage());
                        }
                    });
        }
    }
    private void generateUpcomingBrick() {
        BrickType upcomingBrick = BrickType.random();
        for (int i = 0; i < UPCOMING_AREA_SIZE; i++) {
            for (int j = 0; j < UPCOMING_AREA_SIZE; j++) {
                mUpcomingPoints[i][j].type = TipodePunto.EMPTY;

            }
        }

        switch (upcomingBrick) {
            case L:
                mUpcomingPoints[1][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][1].type = TipodePunto.BOX;
                mUpcomingPoints[3][1].type = TipodePunto.BOX;
                mUpcomingPoints[3][2].type = TipodePunto.BOX;

                break;
            case T:
                mUpcomingPoints[2][1].type = TipodePunto.BOX;
                mUpcomingPoints[1][1].type = TipodePunto.BOX;
                mUpcomingPoints[3][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][2].type = TipodePunto.BOX;

                break;
            case S:
                mUpcomingPoints[1][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][2].type = TipodePunto.BOX;
                mUpcomingPoints[3][2].type = TipodePunto.BOX;

                break;
            case STICK:
                mUpcomingPoints[0][1].type = TipodePunto.BOX;
                mUpcomingPoints[1][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][1].type = TipodePunto.BOX;
                mUpcomingPoints[3][1].type = TipodePunto.BOX;

                break;
            case J:
                mUpcomingPoints[1][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][1].type = TipodePunto.BOX;
                mUpcomingPoints[3][1].type = TipodePunto.BOX;
                mUpcomingPoints[3][0].type = TipodePunto.BOX;

                break;
            case Z:
                mUpcomingPoints[1][0].type = TipodePunto.BOX;
                mUpcomingPoints[1][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][2].type = TipodePunto.BOX;

                break;

            case SQUARE:
                mUpcomingPoints[1][1].type = TipodePunto.BOX;
                mUpcomingPoints[1][2].type = TipodePunto.BOX;
                mUpcomingPoints[2][1].type = TipodePunto.BOX;
                mUpcomingPoints[2][2].type = TipodePunto.BOX;

                break;
        }

    }

    @Override
    public void startGame(PresenterObserver<Puntos[][]> onGameDrawnListener) {
        mIsGamePaused.set(false);
        final long sleepTime = 1000 / FPS;
        new Thread(() -> {
            long count = 0;
            while (!mIsGamePaused.get()) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count % SPEED == 0) {
                    if (mIsTurning.get()) {
                        continue;
                    }
                    next();
                    mHandler.post(() -> onGameDrawnListener.observe(mPoints));
                }
                count++;
            }
        }).start();
    }

    private synchronized void next() {
        updateFallingPoints();

        if (isNextMerged()) {
            if (isOutSide()) {
                if (mGameOverObserver != null) {
                    mHandler.post(mGameOverObserver::onNext);
                }
                mIsGamePaused.set(true);
                return;
            }
            int y = mFallingPoints.stream().mapToInt(p -> p.y).max().orElse(-1);
            while (y >= 0) {
                boolean isScored = true;
                for (int i = 0; i < PLAYING_AREA_WIDTH; i++) {
                    Puntos point = getPlayingPoint(i, y);
                    if (point.type == TipodePunto.EMPTY) {
                        isScored = false;
                        break;
                    }
                }
                if (isScored) {
                    mScore++;
                    finalScore = mScore;
                    if (mScoreUpdatedObserver != null) {
                        mHandler.post(() -> mScoreUpdatedObserver.observe(mScore));
                    }
                    LinkedList<Puntos> tmPoints = new LinkedList<>();
                    for (int i = 0; i <= y; i++) {
                        for (int j = 0; j < PLAYING_AREA_WIDTH; j++) {
                            Puntos point = getPlayingPoint(j, i);
                            if (point.type == TipodePunto .BOX) {
                                point.type = TipodePunto.EMPTY;
                                if (i != y) {
                                    tmPoints.add(new Puntos(point.x, point.y + 1, TipodePunto.BOX, false));
                                }

                            }
                        }
                    }
                    tmPoints.forEach(this::updatePlayingPoint);
                } else {
                    y--;
                }
            }
            mFallingPoints.forEach(p -> p.isFallingPoint = false);
            mFallingPoints.clear();
        } else {
            LinkedList<Puntos> tmPoints = new LinkedList<>();
            for (Puntos fallingPoint : mFallingPoints) {
                fallingPoint.type = TipodePunto.EMPTY;
                fallingPoint.isFallingPoint = false;
                tmPoints.add(new Puntos(fallingPoint.x, fallingPoint.y + 1, TipodePunto.BOX, true));
            }
            mFallingPoints.clear();
            mFallingPoints.addAll(tmPoints);
            mFallingPoints.forEach(this::updatePlayingPoint);
        }

    }

    private boolean isNextMerged() {
        for (Puntos fallingPoint : mFallingPoints) {
            if (fallingPoint.y + 1 >= 0 && (fallingPoint.y == PLAYING_AREA_HEIGHT - 1 ||
                    getPlayingPoint(fallingPoint.x,fallingPoint.y + 1).isStablePoint())) {
                return true;
            }
        }
        return false;
    }


    private boolean isOutSide() {
        for (Puntos fallingPoint : mFallingPoints) {
            if (fallingPoint.y < 0) {
                return true;
            }
        }
        return false;
    }

    private void updatePlayingPoint(Puntos point) {
        if (point.x >= 0 && point.x < PLAYING_AREA_WIDTH &&
                point.y >= 0 && point.y < PLAYING_AREA_HEIGHT) {
            mPoints[point.y][point.x] = point;
            if (point.y < PLAYING_AREA_HEIGHT) {
                mPlayingPoints[point.y][point.x] = point;
            }
        }
    }


    private Puntos getPlayingPoint(int x, int y) {
        if (x >= 0 && y >= 0 && x < PLAYING_AREA_WIDTH && y < PLAYING_AREA_HEIGHT) {
            return mPlayingPoints[y][x];
        }
        return null;
    }


    private void updateFallingPoints() {
        if (mFallingPoints.isEmpty()) {
            for (int i = 0; i < UPCOMING_AREA_SIZE; i++) {
                for (int j = 0; j < UPCOMING_AREA_SIZE; j++) {
                    if (mUpcomingPoints[i][j].type == TipodePunto.BOX) {
                        mFallingPoints.add(new Puntos(j + 3, i - 4, TipodePunto.BOX, true));
                    }
                }
            }
            generateUpcomingBrick();
        }
    }

    @Override
    public void pauseGame() {
        mIsGamePaused.set(true);
    }

    @Override
    public void turn(TurnoJuego turn) {
        if (mIsGamePaused.get() || mIsTurning.get()) {
            return;
        }
        mIsTurning.set(true);
        LinkedList<Puntos> tmPoints;
        boolean canTurn;
        switch (turn) {
            case LEFT:
                updateFallingPoints();
                canTurn = true;
                for (Puntos fallingPoint : mFallingPoints) {
                    if (fallingPoint.y >= 0 && (fallingPoint.x == 0 ||
                            getPlayingPoint(fallingPoint.x - 1, fallingPoint.y).isStablePoint())) {
                        canTurn = false;
                        break;
                    }
                }
                if (canTurn) {
                    tmPoints = new LinkedList<>();
                    for (Puntos fallingPoint : mFallingPoints) {
                        tmPoints.add(new Puntos(fallingPoint.x - 1, fallingPoint.y, TipodePunto.BOX, true));
                        fallingPoint.type = TipodePunto.EMPTY;
                        fallingPoint.isFallingPoint = false;
                    }
                    mFallingPoints.clear();
                    mFallingPoints.addAll(tmPoints);
                    mFallingPoints.forEach(this::updatePlayingPoint);
                }
                break;
            case RIGHT:
                updateFallingPoints();
                canTurn = true;
                for (Puntos fallingPoint : mFallingPoints) {
                    if (fallingPoint.y >= 0 && (fallingPoint.x == PLAYING_AREA_WIDTH - 1 ||
                            getPlayingPoint(fallingPoint.x + 1, fallingPoint.y).isStablePoint())) {
                        canTurn = false;
                        break;
                    }
                }
                if (canTurn) {
                    tmPoints = new LinkedList<>();
                    for (Puntos fallingPoint : mFallingPoints) {
                        tmPoints.add(new Puntos(fallingPoint.x + 1, fallingPoint.y, TipodePunto.BOX, true));
                        fallingPoint.type = TipodePunto.EMPTY;
                        fallingPoint.isFallingPoint = false;
                    }
                    mFallingPoints.clear();
                    mFallingPoints.addAll(tmPoints);
                    mFallingPoints.forEach(this::updatePlayingPoint);
                }
                break;
            case DOWN:
                next();
                break;
            case FIRE:
                rotateFallingPoints();
                break;
            case UP:
            default:
                break;
        }
        mIsTurning.set(false);
    }

    private void rotateFallingPoints() {
        updateFallingPoints();
        int left = mFallingPoints.stream().mapToInt(p -> p.x).min().orElse(-1);
        int right = mFallingPoints.stream().mapToInt(p -> p.x).max().orElse(-1);
        int top = mFallingPoints.stream().mapToInt(p -> p.y).min().orElse(-1);
        int bottom = mFallingPoints.stream().mapToInt(p -> p.y).max().orElse(-1);
        int size = Math.max(right - left, bottom - top) + 1;
        if (rotatePoints(left, top, size)) {
            return;
        }
        if (rotatePoints(right - size + 1, top, size)) {
            return;
        }
        if (rotatePoints(left, bottom - size + 1, size)) {
            return;
        }
        rotatePoints(right - size + 1, bottom - size + 1, size);
    }

    public boolean rotatePoints(int x, int y, int size) {
        Log.i(TAG, "rotatePoints: x = " + x + ", y = " + y + ", size = " + size);
        if (x + size - 1 < 0 || x + size - 1 >= PLAYING_AREA_WIDTH) {
            return false;
        }
        boolean canRotate = true;
        Puntos[][] points = new Puntos[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Puntos point = getPlayingPoint(j + x, i + y);
                if (point == null) {
                    final int tmX = j + x;
                    final int tmY = i + y;
                    point = mFallingPoints.stream()
                            .filter(p -> p.x == tmX && p.y == tmY)
                            .findFirst()
                            .orElse(new Puntos(j + x, i + y));
                }
                if (point.isStablePoint() && getPlayingPoint(x + size - 1 - i, y + j).isFallingPoint) {
                    canRotate = false;
                    break;
                }
                if (point.isFallingPoint) {
                    Log.i(TAG, "rotatePoints: Punto en caída: " + point.x + ", " + point.y + "; tipo: " + point.type);
                }
                points[i][j] = new Puntos(x + size - 1 - i, y + j, point.type, point.isFallingPoint);
            }
            if (!canRotate) {
                break;
            }
        }
        for (Puntos point: mFallingPoints) {
            Log.i(TAG, "rotatePoints: Punto en caída 2: " + point.x + ", " + point.y + ", tipo = " + point.type);
        }
        if (!canRotate) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Puntos point = getPlayingPoint(i + y, j + x);
                if (point == null) {
                    continue;
                }
                point.type = TipodePunto.EMPTY;
            }
        }
        mFallingPoints.clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                updatePlayingPoint(points[i][j]);
                if (points[i][j].isFallingPoint) {
                    mFallingPoints.add(points[i][j]);
                }
            }
        }


        Log.i(TAG, "rotatePoints: falling points size = " + mFallingPoints.size());
        return true;
    }

    public BlockNineModelo() {
        // Obtén la instancia de la base de datos de Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    private boolean isGameOver() {
        if (stateGame == EstadoJuego.OVER) {
            System.out.println(mScore);
            guardarPuntajeFinalEnDatabase(); // Guarda el puntaje final en Realtime Database
            return true;
        }
        return false;
    }

    private void guardarPuntajeFinalEnDatabase() {
        // Crea un nodo en la base de datos con un ID único (por ejemplo, utilizando el método push())
        DatabaseReference puntajeRef = mDatabase.child("puntajes").push();

        // Crea un objeto para representar el puntaje final con sus atributos
        Puntaje scoreFinal = new Puntaje(finalScore);

        // Guarda el objeto puntajeFinal en la base de datos
        puntajeRef.setValue(scoreFinal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // El puntaje final se guardó exitosamente en la base de datos
                        // Realiza cualquier acción adicional que necesites
                        // Por ejemplo, mostrar un mensaje de éxito al usuario
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al guardar el puntaje final en la base de datos
                        // Maneja el error de acuerdo a tus necesidades
                    }
                });
    }

    // Clase auxiliar para representar el puntaje final
    public static class Puntaje {
        private int puntaje;

        public Puntaje() {
            // Constructor vacío requerido para la deserialización de Firebase
        }

        public Puntaje(int puntaje) {
            this.puntaje = puntaje;
        }

        public int getPuntaje() {
            return puntaje;
        }
    }

    @Override
    public void setGameOverListener(PresenterCompletableObserver onGameOverListener) {
        mGameOverObserver = onGameOverListener;
    }

    @Override
    public void setScoreUpdatedListener(PresenterObserver<Integer> onScoreUpdatedListener) {
        mScoreUpdatedObserver = onScoreUpdatedListener;
    }
}
