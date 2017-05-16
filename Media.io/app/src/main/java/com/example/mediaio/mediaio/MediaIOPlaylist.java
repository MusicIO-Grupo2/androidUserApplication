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
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MediaIOPlaylist extends ActividadPrincipal {
    private SharedServer sharedServer;
    private LinearLayout canciones;
    private TextView nombre;
    private TextView descripcion;
    private Button escuchar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioplaylist);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        nombre = (TextView) findViewById(R.id.NombrePlaylist);
        descripcion = (TextView) findViewById(R.id.DescripcionPlaylist);
        escuchar = (Button) findViewById(R.id.EscucharPlaylist);
        canciones = (LinearLayout) findViewById(R.id.ListaCanciones);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarInformacionPlaylist(getIntent().getStringExtra("idPlaylist"));
    }

    private void buscarInformacionPlaylist(final String id)
    {
        final ProcesarResultado mostrarPlaylist = new MostrarResultadoCanciones(canciones,this, R.layout.vista_cancion);

        JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                try {
                    nombre.setText(respuesta.getString("nombre"));
                    descripcion.setText(respuesta.getString("descripcion"));

                    JSONObject tracks = respuesta.getJSONObject("tracks");

                    mostrarPlaylist.ejecutar(tracks, codigoServidor);

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
