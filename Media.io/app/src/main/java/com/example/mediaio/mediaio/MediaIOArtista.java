package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.MostrarResultadoAlbumes;
import com.example.mediaio.mediaio.modelo.MostrarResultadoCanciones;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MediaIOArtista extends ActividadPrincipal {
    private SharedServer sharedServer;
    private LinearLayout albumes;
    private TextView nombre;
    private TextView generos;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioartista);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        albumes = (LinearLayout) findViewById(R.id.ListaAlbumes);
        nombre = (TextView) findViewById(R.id.NombreArtista);
        generos = (TextView) findViewById(R.id.GeneroArtista);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarInformacionArtista(getIntent().getStringExtra("idArtista"));
    }

    private void buscarInformacionArtista(final String id)
    {
        final ProcesarResultado mostrarAlbumes = new MostrarResultadoAlbumes(albumes,this, R.layout.vista_album);

        JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                try {
                    nombre.setText(respuesta.getString("Nombre"));
                    generos.setText(respuesta.getString("Genero"));

                    JSONObject albumes = respuesta.getJSONObject("Albumes");

                    mostrarAlbumes.ejecutar(albumes, codigoServidor);
                }
                catch (JSONException e) {
                }
            }
        };

        sharedServer.buscarInformacionArtista(callback, id);
    }
}
