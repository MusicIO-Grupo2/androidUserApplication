package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.MostrarResultadoAlbumes;
import com.example.mediaio.mediaio.modelo.MostrarResultadoArtistas;
import com.example.mediaio.mediaio.modelo.MostrarResultadoCanciones;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

public class MediaIOBusqueda extends ActividadPrincipal {

    private SharedServer sharedServer;
    private LinearLayout canciones;
    private LinearLayout albumes;
    private LinearLayout artistas;
    private ListView usuarios;
    private TextView masCanciones;
    private TextView masAlbumes;
    private TextView masArtistas;
    private TextView masUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iobusqueda);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        canciones = (LinearLayout) findViewById(R.id.ListaCanciones);
        albumes = (LinearLayout) findViewById(R.id.ListaAlbumes);
        artistas = (LinearLayout) findViewById(R.id.ListaArtistas);
        usuarios = (ListView) findViewById(R.id.ListaUsuarios);
        masUsuarios = (TextView) findViewById(R.id.BotonMasCanciones);
        masAlbumes = (TextView) findViewById(R.id.BotonMasAlbumes);
        masArtistas = (TextView) findViewById(R.id.BotonMasArtistas);
        masUsuarios = (TextView) findViewById(R.id.BotonMasUsuarios);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarCancion(getIntent().getStringExtra("nombre"), 3);
        buscarAlbum(getIntent().getStringExtra("nombre"),3);
        buscarArtista(getIntent().getStringExtra("nombre"),3);
    }

    void buscarCancion(String nombre, int cantidad)
    {
        ProcesarResultado mostrarCanciones = new MostrarResultadoCanciones(canciones,this,R.layout.vista_cancion);
        sharedServer.buscarCanciones(mostrarCanciones, nombre,cantidad);
    }

    void buscarAlbum(String nombre, int cantidad)
    {
        ProcesarResultado mostrarAlbumes = new MostrarResultadoAlbumes(albumes,this,R.layout.vista_album);
        sharedServer.buscarAlbumes(mostrarAlbumes, nombre,cantidad);
    }

    void buscarArtista(String nombre, int cantidad)
    {
        ProcesarResultado mostrarArtistas = new MostrarResultadoArtistas(artistas,this,R.layout.vista_album);
        sharedServer.buscarArtistas(mostrarArtistas, nombre,cantidad);
    }
}
