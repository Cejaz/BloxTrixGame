package com.example.bloxtrixgame.modelos;

import android.graphics.Point;
import android.os.Handler;
import android.widget.ThemedSpinnerAdapter;

import com.example.bloxtrixgame.presentacion.PresenterCompletableObserver;
import com.example.bloxtrixgame.presentacion.PresenterObserver;
import com.example.bloxtrixgame.presentacion.Puntos;
import com.example.bloxtrixgame.presentacion.TipodePunto;
import com.example.bloxtrixgame.presentacion.TurnoJuego;
import com.example.bloxtrixgame.presentacion.modeloJuego;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.LogRecord;

public class BlockNineModelo implements modeloJuego {

    private static final int ESPACIO_JUEGO = 15;
    private static final int AREA_JUEGO_ANCHO= 10;
    private static final int AREA_JUEGO_LARGO = ESPACIO_JUEGO;
    private static final int ESPACIO_AREA_PROXIMA = 4;

    private Puntos[][] mPuntos;
    private Puntos[][] mPuntosJugandos;
    private Puntos[][] mProximosPuntos;

    private int mPuntaje;
    private final AtomicBoolean mJuegoPausado = new AtomicBoolean();
    private final AtomicBoolean mVolteando = new AtomicBoolean();
    private final LinkedList<Puntos> mPuntajeCaida = new LinkedList<>();

    private final Handler mManipular = new Handler();

    private PresenterCompletableObserver mJuegoTerminadoObservar;
    private PresenterObserver<Integer> mObservarPuntajeActulizado;
    public BlockNineModelo(){

    }

    @Override
    public void inicio() {
        mPuntos = new Puntos[ESPACIO_JUEGO][ESPACIO_JUEGO];
        for (int i = 0; i < ESPACIO_JUEGO; i++){
             for (int j = 0; j < ESPACIO_JUEGO; j++){
                 mPuntos[i][j] = new Puntos(j,i);

             }
        }
        mPuntosJugandos = new Puntos[AREA_JUEGO_LARGO][AREA_JUEGO_ANCHO];
        for (int i =0; i <AREA_JUEGO_LARGO; i++){
            System.arraycopy(mPuntos[i],0, mPuntosJugandos[i], 0,AREA_JUEGO_ANCHO);
        }
        mProximosPuntos = new Puntos[ESPACIO_AREA_PROXIMA][ESPACIO_AREA_PROXIMA];
        for(int i = 0; i<ESPACIO_AREA_PROXIMA; i++){
            for (int j = 0; j<ESPACIO_AREA_PROXIMA; j++){
                mProximosPuntos[i][j] = mPuntos [i + i][AREA_JUEGO_ANCHO + 1 + j];
            }
        }
        for(int i = 0; i < AREA_JUEGO_LARGO; i++){
            mPuntos[i][AREA_JUEGO_ANCHO].tipo = TipodePunto.LINEA_VERTICAL;
        }
        nuevoJuego();
    }

    @Override
    public int obtenerEspacioJuego() {
        return ESPACIO_JUEGO;
    }

    @Override
    public void nuevoJuego() {
    mPuntaje = 0;
    for (int i = 0; i < AREA_JUEGO_LARGO; i++){
        for (int j = 0; j < AREA_JUEGO_ANCHO; j++){
             mPuntosJugandos[i][j].tipo = TipodePunto.VACIO;
        }
    }
    mPuntajeCaida.clear();
    generarProximoBlque();
    }
    private void generarProximoBlque(){
        TipoBloque proximoBloque = TipoBloque.random();
        for (int i = 0; i < ESPACIO_AREA_PROXIMA; i++){
            for (int j = 0; j < ESPACIO_AREA_PROXIMA; j++){
                mProximosPuntos[i][j].tipo = TipodePunto.VACIO;
            }
        }

        switch (proximoBloque){
            case L:
                mProximosPuntos[1][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[3][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[3][2].tipo=TipodePunto.CAJA;
                break;
            case T:
                mProximosPuntos[1][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[3][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][2].tipo=TipodePunto.CAJA;
                break;
            case CHAIR:
                mProximosPuntos[1][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][2].tipo=TipodePunto.CAJA;
                mProximosPuntos[3][2].tipo=TipodePunto.CAJA;
                break;
            case STICK:
                mProximosPuntos[0][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[1][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[3][1].tipo=TipodePunto.CAJA;
                break;
            case SQUARE:
                mProximosPuntos[1][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[1][2].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][1].tipo=TipodePunto.CAJA;
                mProximosPuntos[2][2].tipo=TipodePunto.CAJA;
                break;
        }
    }


    @Override
    public void iniciarJuego(PresenterObserver<Puntos[][]> dibujandoJuego) {
        mJuegoPausado.set(false);
        final long tiempodormido = 1000 /FPS;
        new Thread(()->{long contar = 0;
                        while (!mJuegoPausado.get()){
                            try {
                                Thread.sleep(tiempodormido);
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            if(contar % SPEED == 0){
                                if (mVolteando.get()){
                                    continue;
                                }
                                siguiente();
                                mManipular.post(()-> dibujandoJuego.observe(mPuntosJugandos));
                            }
                            contar++;
                        }
        }).start();
    }

    private synchronized void siguiente(){
        actualizarPuntosCaida();
        if (isNextMerget()){
            if (estaAfuera()) {
                if(mJuegoTerminadoObservar != null){
                    mManipular.post(mJuegoTerminadoObservar::observe);
                }
             mJuegoPausado.set(true);
                return;
            }
            int y = mPuntajeCaida.stream().mapToInt(p ->p.y).max().orElse(-1);
            while (y >= 0 ){
                boolean seApunta = true;
                for (int i = 0; i < AREA_JUEGO_ANCHO; i++){
                    Puntos punto = obtenerPuntosJugando(i,y);
                    if (punto.tipo == TipodePunto.VACIO){
                        seApunta = false;
                        break;
                    }
                }
                if(seApunta){
                    mPuntaje++;
                    if (mObservarPuntajeActulizado !=null){
                        mManipular.post(()-> mObservarPuntajeActulizado.observe(mPuntaje));
                    }
                    LinkedList<Puntos> tmPuntos = new LinkedList<>();
                    for (int i = 0; i<= y; i ++){
                        for (int j = 0; j < AREA_JUEGO_ANCHO; j++){
                            Puntos punto = obtenerPuntosJugando(j,i);
                            if (punto.tipo ==TipodePunto.CAJA){
                                punto.tipo = TipodePunto.VACIO;
                                if(i != y){
                                    tmPuntos.add(new Puntos(punto.x,punto.y + 1, TipodePunto.CAJA,false));
                                }
                            }
                        }
                    }
                    tmPuntos.forEach(this::actualizarpuntosJuego);
                }else{
                    y--;
                }
            }
            mPuntajeCaida.forEach(p -> p.puntodeCaida = false );
            mPuntajeCaida.clear();
        }else {
            LinkedList<Puntos> tmPunto = new LinkedList<>();
            for(Puntos puntoDeCaida:mPuntajeCaida){
                puntoDeCaida.tipo=TipodePunto.VACIO;
                puntoDeCaida.puntodeCaida = false;
                tmPunto.add(new Puntos(puntoDeCaida.x, puntoDeCaida.y+1,TipodePunto.CAJA,true ));
                mPuntajeCaida.clear();
                mPuntajeCaida.addAll(tmPunto);
                mPuntajeCaida.forEach(this::actualizarpuntosJuego);
            }
        }
    }
    private void actualizarPuntosCaida(){
        if(mPuntajeCaida.isEmpty()) {
            for (int i = 0; i < ESPACIO_AREA_PROXIMA; i++){
                for (int j = 0; j < ESPACIO_AREA_PROXIMA; j++){
                    if(mProximosPuntos[i][j].tipo == TipodePunto.CAJA){
                        mPuntajeCaida.add(new Puntos(j +3,i -4, TipodePunto.CAJA,true));
                    }
                }
            }
            generarProximoBlque();
        }
    }

    private boolean isNextMerget(){
        for (Puntos puntajeCaida:mPuntajeCaida){
            if (puntajeCaida.y + 1 >= 0 && (puntajeCaida.y==AREA_JUEGO_LARGO - 1||
                    obtenerPuntosJugando(puntajeCaida.x,puntajeCaida.y + 1).puntosEstables())){
                return true;
            }

        }
        return false;
    }
    private boolean estaAfuera (){
        for( Puntos puntajeCaida: mPuntajeCaida){
            if(puntajeCaida.y <0){
                return true;
            }
        }
        return false;
    }
    private void actualizarpuntosJuego(Puntos puntos){
        if (puntos.x>=0 && puntos.x < AREA_JUEGO_LARGO && puntos.y >= 0 && puntos.y < AREA_JUEGO_ANCHO){
            mPuntos[puntos.x][puntos.y] = puntos;
            mPuntosJugandos[puntos.y][puntos.x]= puntos;
        }

    }
    private Puntos obtenerPuntosJugando(int x, int y ){
        if(x >=0 && y >= 0 && x < AREA_JUEGO_ANCHO && y < AREA_JUEGO_LARGO){
            return mPuntosJugandos[y][x];
        }
        return null;
    }

    @Override
    public void pausarJuego() {
        mJuegoPausado.set(true);
    }

    @Override
    public void voltear(TurnoJuego voltear) {

    }

    @Override
    public void enviarJuegoterminado(PresenterCompletableObserver juegoTerminadoActivado) {
 mJuegoTerminadoObservar = juegoTerminadoActivado;

    }

    @Override
    public void enviarActualizacionPuntaje(PresenterObserver<Integer> actualizacionPuntajeActivada) {
    mObservarPuntajeActulizado = actualizacionPuntajeActivada;
    }

    private enum TipoBloque{
        L(0), T(1), CHAIR(2), STICK(3), SQUARE(4);
        final int mValor;

        TipoBloque(int valor){
            mValor = valor;
            }
        static TipoBloque delValor(int valor){
            switch (valor){
                case 1:
                    return T;
                case 2:
                    return CHAIR;
                case 3:
                    return STICK;
                case 4:
                    return SQUARE;
                case 0:
                default:
                    return L;
            }
        }

        static TipoBloque random(){
            Random random = new Random();
            return delValor(random.nextInt(5));
        }
    }


}
