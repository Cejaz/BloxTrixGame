package com.example.bloxtrixgame.presentacion;

public class presentacionjuego {

    private vistajuego mvistajuego;
    private EstadoJuego mEstado;
    private modeloJuego mmodeloJuego;

    public void enviaVistaJuego(vistajuego Juegovista){
        mvistajuego = Juegovista;
    }

    public void enviaModeloJuego(modeloJuego Juegomodelo){
        mmodeloJuego=Juegomodelo;
    }

    public void iniciar(){
        mmodeloJuego.inicio();
        mvistajuego.iniciar(mmodeloJuego.obtenerEspacioJuego());
        mmodeloJuego.enviarJuegoterminado(()-> enviarEstado(EstadoJuego.TERMINADO));
        mmodeloJuego.enviarActualizacionPuntaje((mvistajuego::enviarPuntaje));
        enviarEstado(EstadoJuego.INICIO);
    }

    public void voltear(TurnoJuego voltear){
        mmodeloJuego.voltear(voltear);
    }

    public void cambiarEstado(){
        if (mEstado == EstadoJuego.JUGANDO){
            juegoPausado();
        }else {
            juegoIniciado();
        }
    }

    private void juegoPausado(){
        enviarEstado(EstadoJuego.PAUSA);
        mmodeloJuego.pausarJuego();
    }

    private void juegoIniciado(){
        enviarEstado(EstadoJuego.INICIO);
        mmodeloJuego.iniciarJuego(mvistajuego::dibujar);
    }
    private void enviarEstado(EstadoJuego estado){
        if(mEstado == EstadoJuego.TERMINADO || mEstado ==EstadoJuego.INICIO){
            mmodeloJuego.nuevoJuego();
        }
        mEstado = estado;
        mvistajuego.enviarEstado(estado);

    }
}
