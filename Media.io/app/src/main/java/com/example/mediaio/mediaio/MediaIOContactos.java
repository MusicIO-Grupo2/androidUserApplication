package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.MostrarResultadoContactos;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONObject;

public class MediaIOContactos extends ActividadPrincipal {
    private SharedServer sharedServer;

    private LinearLayout contactos;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iousuario);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        contactos = (LinearLayout) findViewById(R.id.ListaContactos);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token", ""), sharedPref.getLong("id", 0));

        buscarContactos(Long.toString(sharedPref.getLong("id", 0)));
    }

    private void buscarContactos(final String id) {
        final ProcesarResultado mostrarContactos = new MostrarResultadoContactos(contactos, this, R.layout.vista_contacto, sharedServer);

        JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(final JSONObject respuesta, long codigoServidor) {
                    mostrarContactos.ejecutar(respuesta, codigoServidor);
            }
        };

        sharedServer.buscarContactos(callback);
    }
}
