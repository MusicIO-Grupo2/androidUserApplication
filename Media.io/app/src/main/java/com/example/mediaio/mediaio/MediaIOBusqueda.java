package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.MostrarResultadoAlbumes;
import com.example.mediaio.mediaio.modelo.MostrarResultadoArtistas;
import com.example.mediaio.mediaio.modelo.MostrarResultadoCanciones;
import com.example.mediaio.mediaio.modelo.MostrarResultadoUsuarios;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

public class MediaIOBusqueda extends ActividadPrincipal {

    private SharedServer sharedServer;
    private LinearLayout canciones;
    private LinearLayout albumes;
    private LinearLayout artistas;
    private LinearLayout usuarios;
    private TextView masCanciones;
    private TextView masAlbumes;
    private TextView masArtistas;
    private TextView masUsuarios;
    private SharedPreferences sharedPref;
    private int posicionCanciones;
    private int posicionAlbumes;
    private int posicionArtistas;
    private int posicionUsuarios;

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
        usuarios = (LinearLayout) findViewById(R.id.ListaUsuarios);
        masCanciones = (TextView) findViewById(R.id.BotonMasCanciones);
        masAlbumes = (TextView) findViewById(R.id.BotonMasAlbumes);
        masArtistas = (TextView) findViewById(R.id.BotonMasArtistas);
        masUsuarios = (TextView) findViewById(R.id.BotonMasUsuarios);

        posicionAlbumes = 0;
        posicionArtistas = 0;
        posicionCanciones = 0;
        posicionUsuarios = 0;

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarCancion(getIntent().getStringExtra("nombre"), 0, posicionCanciones);
        buscarAlbum(getIntent().getStringExtra("nombre"), 0, posicionAlbumes);
        buscarArtista(getIntent().getStringExtra("nombre"), 0, posicionArtistas);
        buscarUsuario(getIntent().getStringExtra("nombre"), 0, posicionUsuarios);

        masCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buscarCancion(getIntent().getStringExtra("nombre"), posicionCanciones, posicionCanciones +3);
                posicionCanciones +=3;
            }
        });

        masAlbumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarAlbum(getIntent().getStringExtra("nombre"), posicionAlbumes, posicionAlbumes +3);
                posicionAlbumes +=3;
            }
        });

        masArtistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarArtista(getIntent().getStringExtra("nombre"), posicionArtistas, posicionArtistas +3);
                posicionArtistas +=3;
            }
        });

        masUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarUsuario(getIntent().getStringExtra("nombre"), posicionUsuarios, posicionUsuarios +3);
                posicionUsuarios +=3;
            }
        });
    }

    void buscarCancion(String nombre, int posicion, int cantidad)
    {
        ProcesarResultado mostrarCanciones = new MostrarResultadoCanciones(canciones,this,R.layout.vista_cancion, sharedServer,Long.toString(sharedPref.getLong("id",0)));
        sharedServer.buscarCanciones(mostrarCanciones, nombre, posicion, cantidad);
    }

    void buscarAlbum(String nombre, int posicion, int cantidad)
    {
        ProcesarResultado mostrarAlbumes = new MostrarResultadoAlbumes(albumes,this,R.layout.vista_album);
        sharedServer.buscarAlbumes(mostrarAlbumes, nombre, posicion, cantidad);
    }

    void buscarArtista(String nombre, int posicion, int cantidad)
    {
        ProcesarResultado mostrarArtistas = new MostrarResultadoArtistas(artistas,this,R.layout.vista_artista);
        sharedServer.buscarArtistas(mostrarArtistas, nombre, posicion, cantidad);
    }

    void buscarUsuario(String nombre, int posicion, int cantidad)
    {
        ProcesarResultado mostrarUsuarios = new MostrarResultadoUsuarios(usuarios,this,R.layout.vista_usuario, sharedServer);
        sharedServer.buscarUsuarios(mostrarUsuarios, nombre, posicion, cantidad);
    }
}
