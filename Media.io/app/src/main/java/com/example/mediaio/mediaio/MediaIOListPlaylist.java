package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.Formularios.EditTextComun;
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
    private SharedPreferences sharedPref;

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

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarPlaylists(getIntent().getStringExtra("idPlaylist"));

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoCrearPlaylist();
            }
        });
    }

    private void buscarPlaylists(final String id)
    {
        final ProcesarResultado mostrarPlaylists = new MostrarResultadoPlaylists(canciones,this, R.layout.vista_playlists, sharedServer);

        JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                    mostrarPlaylists.ejecutar(respuesta, codigoServidor);

            }
        };

        sharedServer.buscarPlaylists(callback, id);
    }

    void recargar()
    {
        Intent intent = new Intent(this, MediaIOListPlaylist.class);
        startActivity(intent);
    }

    void mostrarDialogoCrearPlaylist()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MediaIOListPlaylist.this);

        builder.setTitle(R.string.NuevaPlaylistDialogoTitulo);

        LayoutInflater inflater = MediaIOListPlaylist.this.getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialogo_creacion_playlist,null);
        final EditText nombrePlaylist = (EditText) vista.findViewById(R.id.NombrePlaylistCrear);
        final EditText descripcionPlaylist = (EditText) vista.findViewById(R.id.DescripcionPlaylistCrear);
        builder.setView(vista);

        builder.setPositiveButton(R.string.NuevaPlaylistDialogoBotonCrear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONCallback callback = new JSONCallback() {
                    @Override
                    public void ejecutar(JSONObject respuesta, long codigoServidor) {
                        recargar();
                    }
                };
                sharedServer.crearPlaylist(callback,nombrePlaylist.getText().toString(),descripcionPlaylist.getText().toString(), Long.toString(sharedPref.getLong("id",0)));
            }
        });

        builder.setNegativeButton(R.string.NuevaPlaylistDialogoBotonCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
