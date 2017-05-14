package com.example.mediaio.mediaio.modelo;

/**
 * Created by Marcos on 13/05/2017.
 */

public interface IControlesReproduccion {


    abstract public void start();
    abstract public void pause();
    abstract public int getDuration();
    abstract public int getCurrentPosition();
    abstract public boolean isPlaying();
    abstract public void next();
    abstract public void prev();

}
