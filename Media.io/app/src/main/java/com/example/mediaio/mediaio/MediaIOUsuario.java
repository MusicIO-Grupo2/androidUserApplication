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
import android.widget.Toast;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.MostrarResultadoAlbumes;
import com.example.mediaio.mediaio.modelo.MostrarResultadoCanciones;
import com.example.mediaio.mediaio.modelo.MostrarResultadoUsuarios;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MediaIOUsuario extends ActividadPrincipal {
    private SharedServer sharedServer;

    private LinearLayout contactos;
    private TextView nombreUsuario;
    private TextView nombre;
    private TextView apellido;
    private TextView pais;
    private TextView email;
    private TextView fechaNacimiento;
    private Button seguir;
    private boolean siguiendo;

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

        nombreUsuario = (TextView) findViewById(R.id.NombreUsuario);
        nombre = (TextView) findViewById(R.id.Nombre);
        apellido = (TextView) findViewById(R.id.Apellido);
        pais = (TextView) findViewById(R.id.Pais);
        email = (TextView) findViewById(R.id.Email);
        fechaNacimiento = (TextView) findViewById(R.id.FechaDeNacimiento);
        contactos = (LinearLayout) findViewById(R.id.ListaContactos);
        seguir = (Button) findViewById(R.id.BotonSeguirUsuario);


        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));

        buscarInformacionArtista(getIntent().getStringExtra("idUsuario"));
    }

    private void buscarInformacionArtista(final String id)
    {
        final ProcesarResultado mostrarContactos = new MostrarResultadoUsuarios(contactos,this, R.layout.vista_usuario, sharedServer);

        JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(final JSONObject respuesta, long codigoServidor) {
                try {
                    nombre.setText(respuesta.getString("Nombre"));
                    apellido.setText(respuesta.getString("Apellido"));
                    nombreUsuario.setText(respuesta.getString("NombreUsuario"));
                    pais.setText(respuesta.getString("Pais"));
                    email.setText(respuesta.getString("Email"));
                    fechaNacimiento.setText(respuesta.getString("FechaDeNacimiento"));
                    siguiendo = respuesta.getBoolean("Contacto");

                    if(siguiendo)
                    {
                        seguir.setText(R.string.DejarSeguir);
                    }

                    final JSONCallback mostrarToast = new JSONCallback() {
                        @Override
                        public void ejecutar(JSONObject respuesta, long codigoServidor) {
                            Toast.makeText(getApplicationContext(),"Listo",Toast.LENGTH_SHORT);
                        }
                    };

                    seguir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(siguiendo)
                            {
                                try
                                {
                                sharedServer.dejarSeguirUsuario(mostrarToast,respuesta.getString("ID"));
                                }
                                catch (JSONException e)
                                {

                                }
                                seguir.setText(R.string.SeguirUsuario);
                                siguiendo = false;
                            }
                            else
                            {
                                try
                                {
                                    sharedServer.seguirUsuario(mostrarToast,respuesta.getString("ID"));
                                }
                                catch (JSONException e)
                                {

                                }
                                seguir.setText(R.string.DejarSeguir);
                                siguiendo = true;
                            }
                        }
                    });

                    JSONObject contactos = respuesta.getJSONObject("Contactos");

                    mostrarContactos.ejecutar(contactos, codigoServidor);
                }
                catch (JSONException e) {
                }
            }
        };

        sharedServer.buscarInformacionUsuario(callback, id);
    }
}