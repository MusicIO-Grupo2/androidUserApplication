package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.MostrarResultadoCanciones;
import com.example.mediaio.mediaio.modelo.MostrarResultadoPlaylists;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MediaIOListPlaylist extends ActividadPrincipal {
    private SharedServer sharedServer;
    private LinearLayout canciones;
    private Button crear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iolist_playlist);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        canciones = (LinearLayout) findViewById(R.id.ListaCanciones);
        crear = (Button) findViewById(R.id.CrearPlaylist);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarPlaylists(getIntent().getStringExtra("idPlaylist"));
    }

    private void buscarPlaylists(final String id)
    {
        final ProcesarResultado mostrarPlaylists = new MostrarResultadoPlaylists(canciones,this, R.layout.vista_playlists);

        JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                    mostrarPlaylists.ejecutar(respuesta, codigoServidor);

            }
        };

        sharedServer.buscarPlaylists(callback, id);
    }
}
