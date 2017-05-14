package com.example.mediaio.mediaio.Actividades;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mediaio.mediaio.MainActivity;
import com.example.mediaio.mediaio.MediaIOPerfil;
import com.example.mediaio.mediaio.MediaIOPlay;
import com.example.mediaio.mediaio.R;
import com.example.mediaio.mediaio.modelo.Callback;
import com.example.mediaio.mediaio.modelo.ControlReproduccion;
import com.example.mediaio.mediaio.modelo.IControlesReproduccion;
import com.example.mediaio.mediaio.modelo.Reproductor;

import java.util.ArrayList;

/**
 * Created by Marcos on 15/04/2017.
 */

public class ActividadPrincipal extends AppCompatActivity implements IControlesReproduccion {
    private ControlReproduccion controles;
    private Reproductor reproductor;
    private Boolean conectadoAServicio;
    private ArrayList<String> playlist;
    private ArrayList<Callback> tareasPendientes;

    private ServiceConnection conexion = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Reproductor.BinderReproductor binder = (Reproductor.BinderReproductor) service;
            reproductor = binder.getService();

            conectadoAServicio = true;

            for(int i=0;i<tareasPendientes.size();i++)
                tareasPendientes.get(i).ejecutar();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conectadoAServicio = false;
        tareasPendientes = new ArrayList<Callback>();

        Intent intent = new Intent(this, Reproductor.class);
        bindService(intent, conexion, Context.BIND_AUTO_CREATE );
    }

    private void inicializarReproductorPrivado()
    {
        controles = new ControlReproduccion((LinearLayout) findViewById(R.id.LugarReproductor), true, this);
    }


    public void inicializarReproductor()
    {
        if(conectadoAServicio)
            inicializarReproductorPrivado();
        else {
            tareasPendientes.add(new Callback() {
                @Override
                public void ejecutar() {
                    inicializarReproductorPrivado();
                }
            });
        }

    }

    public void habilitarLoop()
    {
        if(conectadoAServicio)
            reproductor.setLoop();
        else {
            tareasPendientes.add(new Callback() {
                @Override
                public void ejecutar() {
                    reproductor.setLoop();
                }
            });
        }
    }

    public void deshabilitarLoop()
    {
        if(conectadoAServicio)
            reproductor.unsetLoop();
        else {
            tareasPendientes.add(new Callback() {
                @Override
                public void ejecutar() {
                    reproductor.unsetLoop();
                }
            });
        }
    }

    public void reproducirPlaylist(final ArrayList<String> playlist) {

        if(conectadoAServicio)
            reproductor.inicializar(playlist);
        else {
            tareasPendientes.add(new Callback() {
                @Override
                public void ejecutar() {
                    reproductor.inicializar(playlist);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barraaplicacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:

                final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

                String email = sharedPref.getString("email","");
                sharedPref.edit().clear().commit();
                sharedPref.edit().putString("email",email);

                irASplash();
                return true;
            case R.id.menuPerfil:
                irAModificarPerfil();
                return true;
            case R.id.menuPlaylist:
                irAPlayList();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void irASplash()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void irAModificarPerfil()
    {
        Intent intent = new Intent(this, MediaIOPerfil.class);
        startActivity(intent);
    }

    void irAPlayList()
    {
        Intent intent = new Intent(this, MediaIOPlay.class);
        startActivity(intent);
    }

    @Override
    public void start() {
        reproductor.play();
    }

    @Override
    public void pause() {
        reproductor.pausar();
    }

    @Override
    public int getDuration() {
        return reproductor.obtenerDuracion();
    }

    @Override
    public int getCurrentPosition() {
        return reproductor.obtenerPosicionCancion();
    }

    @Override
    public boolean isPlaying() {
        return reproductor.estaReproduciendo();
    }

    @Override
    public void next() {
        reproductor.siguienteCancion();
    }

    @Override
    public void prev() {
        reproductor.anteriorCancion();
    }

}
