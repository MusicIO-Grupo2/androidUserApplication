package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.Formularios.EditTextPassword;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginSecondActivity extends AppCompatActivity {

    EditTextPassword contrasena;
    TextView error;
    Button login;
    SharedServer sharedServer;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_second);
        ponerFondo();

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
        login = (Button) findViewById(R.id.login);
        sharedServer = new SharedServer();
        contrasena = (EditTextPassword) findViewById(R.id.loginContrasena);
        error = (TextView) findViewById(R.id.loginError);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto;
                try
                {
                    texto = contrasena.obtenerTexto();
                    login.setEnabled(false);
                    sharedServer.obtenerToken(sharedPref.getString("email",""),texto,new IngresarUsuarioCallback());
                }
                catch (InputErronea e)
                {
                }
            }
        });
    }

    private class IngresarUsuarioCallback extends JSONCallback
    {
        @Override
        public void ejecutar(JSONObject respuesta, long codigoServidor) {
            Log.e("Mensaje",codigoServidor + ":" + respuesta.toString());
            if(codigoServidor==200)
            {
                try {
                    JSONObject user = respuesta.getJSONObject("user");

                    //Al ser Server Mandatory se guardan los datos que envio el Server como validos.
                    sharedPref.edit().putString("avatar",user.getString("image")).commit();
                    sharedPref.edit().putString("nombreUsuario",user.getString("userName")).commit();
                    sharedPref.edit().putString("nombre",user.getString("firstName")).commit();
                    sharedPref.edit().putString("apellido",user.getString("lastName")).commit();
                    sharedPref.edit().putString("email",user.getString("email")).commit();
                    sharedPref.edit().putString("fechaNacimiento",user.getString("birthdate")).commit();
                    sharedPref.edit().putString("pais",user.getString("country")).commit();
                    sharedPref.edit().putString("token", respuesta.getString("token")).commit();
                    sharedPref.edit().putLong("id",Long.parseLong(user.getString("userID"))).commit();
                }
                catch (JSONException e)
                {
                    //No tiene sentido hacer nada mas si el Server responde un JSON corrupto.
                    return;
                }

                irAMain();
            }
            else
            {
                error.setText(R.string.loginContrasenaInvalida);
                login.setEnabled(true);
            }

        }
    }

    void irALoginFirst()
    {
        Intent intent = new Intent(this, LoginFirstActivity.class);
        startActivity(intent);
    }

    void irAMain()
    {
        Intent intent = new Intent(this, MediaIOMain.class);
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
