package com.example.mediaio.mediaio.modelo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Marcos on 30/04/2017.
 */

public class Reproductor extends Service implements MediaPlayer.OnCompletionListener {

    private ArrayList<String> playlist;
    private MediaPlayer mediaPlayer;
    private Boolean loop;
    private Boolean termino;
    private int cancion;
    private Boolean inicializado;
    private final IBinder mBinder = new BinderReproductor();
    private Callback play;
    private Callback pausa;

    public class BinderReproductor extends Binder {
        public Reproductor getService() {
            return Reproductor.this;
        }
    }

    public void onCreate()
    {
    }

    public void configurarCallbacks(Callback play, Callback pausa)
    {
        this.play = play;
        this.pausa = pausa;
    }


    public void inicializar(ArrayList<String> playlist)
    {
        if(!inicializado)
            mediaPlayer = new MediaPlayer();

            termino = false;
            inicializado = true;
            this.playlist = playlist;
            cancion = 0;
            mediaPlayer.setOnCompletionListener(this);
            configurarCancion();
            play();
    }

    public void inicializar()
    {
        if(inicializado == null)
            inicializado = false;

        if(!inicializado) {
            mediaPlayer = new MediaPlayer();
            termino = false;
            inicializado = true;
            loop = false;
            cancion = 0;
            playlist = new ArrayList<String>();
            mediaPlayer.setOnCompletionListener(this);
        }
    }

    private void configurarCancion()
    {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playlist.get(cancion));
            mediaPlayer.prepare();
        }
        catch (IOException e)
        {

        }
    }

    public void play() {
        if(mediaPlayer != null && playlist.size() > 0)
        {
            mediaPlayer.start();
            this.play.ejecutar();
        }
    }

    public int obtenerDuracion()
    {
        if(mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return  0;
    }

    public int obtenerPosicionCancion()
    {
        if(mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public Boolean estaReproduciendo()
    {
        if(mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return  false;
    }

    public void setLoop()
    {
        loop = true;
    }

    public void unsetLoop()
    {
        loop = false;
    }

    public void pausar(){
        if(mediaPlayer != null) {
            pausa.ejecutar();
            mediaPlayer.pause();
        }
    }

    public void siguienteCancion()
    {
        if(cancion < (playlist.size()-1))
        {
            cancion++;
        }
        else if(loop && playlist.size() != 0)
        {
            cancion = 0;
        }
        else
            return;

        configurarCancion();
        play();
    }

    public void anteriorCancion()
    {
        if(cancion > 0)
        {
            cancion--;
        }
        else if(loop && playlist.size() != 0)
        {
            cancion = playlist.size()-1;
        }
        else
            return;

        configurarCancion();
        play();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        inicializar();
        return mBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        siguienteCancion();
    }

}
