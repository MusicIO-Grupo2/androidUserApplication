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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginSecondActivity extends AppCompatActivity {

    EditText contrasena;
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
        contrasena = (EditText) findViewById(R.id.loginContrasena);
        error = (TextView) findViewById(R.id.loginError);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setEnabled(false);
                sharedServer.obtenerToken(sharedPref.getString("email",""),contrasena.getText().toString(),new IngresarUsuarioCallback());
            }
        });
    }

    private class IngresarUsuarioCallback extends JSONCallback
    {
        @Override
        public void ejecutar(JSONObject respuesta, long codigoServidor) {
            if(codigoServidor==200)
            {
                try {
                    sharedPref.edit().putString("nombre",respuesta.getString("Name")).commit();
                    sharedPref.edit().putString("apellido",respuesta.getString("LastName")).commit();
                    sharedPref.edit().putString("email",respuesta.getString("Email")).commit();
                    sharedPref.edit().putString("fechaNacimiento",respuesta.getString("FechaNacimiento")).commit();
                    sharedPref.edit().putString("token", respuesta.getString("Token")).commit();
                    sharedPref.edit().putString("id",respuesta.getString("UserID")).commit();
                }
                catch (JSONException e)
                {

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

    void ponerFondo()
    {
        LinearLayout fondo = (LinearLayout) findViewById(R.id.ventanaLoginFirst);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
        fondo.setBackgroundResource(sharedPref.getInt("idFondo", R.drawable.background1));
    }
}
