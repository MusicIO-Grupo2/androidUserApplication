package com.example.mediaio.mediaio;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.SharedServer;

//Esta es la que consideramos la pantalla Main por ser el inicio de la aplicacion realmente

public class MediaIOMain extends ActividadPrincipal {
    SharedServer sharedServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iomain);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();
        habilitarLoop();

    }
}
