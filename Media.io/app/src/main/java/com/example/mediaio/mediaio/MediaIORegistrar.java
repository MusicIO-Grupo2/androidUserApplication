package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONObject;

public class MediaIORegistrar extends AppCompatActivity {

    SharedServer sharedServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioregistrar);
        ponerFondo();

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        final EditText nombre = (EditText) findViewById(R.id.registroTextoNombre);
        final EditText apellido = (EditText) findViewById(R.id.registroTextoApellido);
        final EditText email = (EditText) findViewById(R.id.registroTextoEmail);
        final EditText fechaNacimiento = (EditText) findViewById(R.id.registroTextoFechaNacimiento);
        final EditText contrasena = (EditText) findViewById(R.id.registroTextoContrasena);
        final TextView error = (TextView) findViewById(R.id.registroTextoError);
        final Button registrarse = (Button) findViewById(R.id.registroBotonRegistrar);

        //Si se esta logueando con Facebook se tendran estos datos y se llenan los campos.
        nombre.setText(sharedPref.getString("nombre",""));
        apellido.setText(sharedPref.getString("apellido",""));
        email.setText(sharedPref.getString("email",""));
        fechaNacimiento.setText(sharedPref.getString("fechaNacimiento",""));

        //Callback que recibe la confirmacion de alta.
        class DarAltaCallback extends JSONCallback
        {

            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                if(codigoServidor == 200)
                {
                    //Se guardan por ultima vez los datos ya que esto es Server Mandatory y tiene
                    //la ultima palabra de aprobacion.
                    sharedPref.edit().putString("nombre", nombre.getText().toString());
                    sharedPref.edit().putString("apellido", apellido.getText().toString());
                    sharedPref.edit().putString("email", email.getText().toString());
                    sharedPref.edit().putString("fechaNacimiento", fechaNacimiento.getText().toString());
                    sharedPref.edit().putString("contrasena", contrasena.getText().toString());
                    sharedPref.edit().commit();

                    irALoginSecond();
                }
                else
                    error.setText("Hubo un error. Revise los datos ingresados y vuelva a intentarlo.");

                //Se reativa el boton para poder reenviar los datos.
                registrarse.setEnabled(true);

            }
        };

        //Handle del boton
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Desactiva el boton para no admitir repeticiones.
                registrarse.setEnabled(false);
                sharedServer.darAltaUsuario(nombre.getText().toString(), apellido.getText().toString(), email.getText().toString(), fechaNacimiento.getText().toString(), contrasena.getText().toString(), new DarAltaCallback());

            }
        });
    }

    void irALoginSecond()
    {
        Intent intent = new Intent(this, LoginSecondActivity.class);
        startActivity(intent);
    }

    //El fondo se mantiene entre las diferentes pantallas.
    void ponerFondo()
    {
        LinearLayout fondo = (LinearLayout) findViewById(R.id.ventanaLoginFirst);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
        fondo.setBackgroundResource(sharedPref.getInt("idFondo", R.drawable.background1));
    }
}
