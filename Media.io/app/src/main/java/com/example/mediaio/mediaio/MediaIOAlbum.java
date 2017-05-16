package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.MostrarResultadoCanciones;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MediaIOAlbum extends ActividadPrincipal {
    private SharedServer sharedServer;
    private LinearLayout canciones;
    private TextView nombre;
    private TextView artistas;
    private TextView generos;
    private Button escuchar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioalbum);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        canciones = (LinearLayout) findViewById(R.id.ListaCanciones);
        nombre = (TextView) findViewById(R.id.TituloAlbum);
        artistas = (TextView) findViewById(R.id.ArtistasAlbum);
        generos = (TextView) findViewById(R.id.GeneroAlbum);
        escuchar = (Button) findViewById(R.id.EscucharAlbum);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarInformacionAlbum(getIntent().getStringExtra("idAlbum"));
    }

    private void buscarInformacionAlbum(final String id)
    {
        final ProcesarResultado mostrarCanciones = new MostrarResultadoCanciones(canciones,this, R.layout.vista_cancion);

        JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                try {
                    nombre.setText(respuesta.getString("nombre") + " - " + respuesta.getString("fecha"));
                    artistas.setText(respuesta.getString("artista"));
                    generos.setText(respuesta.getString("genero"));

                    JSONObject tracks = respuesta.getJSONObject("tracks");

                    mostrarCanciones.ejecutar(tracks, codigoServidor);

                    Iterator<String> i = tracks.keys();
                    final ArrayList<String> temas = new ArrayList<String>();

                    while(i.hasNext())
                    {
                        temas.add(tracks.getJSONObject(i.next()).getString("URL"));
                    }

                    escuchar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reproducirPlaylist(temas);
                        }
                    });

                }
                catch (JSONException e) {
                }
                }
            };

        sharedServer.buscarInformacionAlbum(callback, id);
    }
}
