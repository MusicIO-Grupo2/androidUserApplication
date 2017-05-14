package com.example.mediaio.mediaio;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.Reproductor;

public class MediaIOPlay extends ActividadPrincipal {
    private Reproductor reproductor;
    private Boolean mostrandoImagenPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioplay);

        inicializarReproductor();

    }
}
