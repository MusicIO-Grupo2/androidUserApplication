package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

public class MediaIORegistrar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioregistrar);

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        final EditText nombre = (EditText) findViewById(R.id.registroTextoNombre);
        final EditText apellido = (EditText) findViewById(R.id.registroTextoApellido);
        final EditText email = (EditText) findViewById(R.id.registroTextoEmail);
        final EditText fechaNacimiento = (EditText) findViewById(R.id.registroTextoFechaNacimiento);
        final EditText contrasena = (EditText) findViewById(R.id.registroTextoContrasena);
        final TextView error = (TextView) findViewById(R.id.registroTextoError);
        final Button registrarse = (Button) findViewById(R.id.registroBotonRegistrar);

        nombre.setText(sharedPref.getString("nombre",""));
        apellido.setText(sharedPref.getString("apellido",""));
        email.setText(sharedPref.getString("email",""));
        fechaNacimiento.setText(sharedPref.getString("fechaNacimiento",""));

       class DarAlta extends JSONCallback
        {

            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                if(codigoServidor == 200)
                {
                    sharedPref.edit().putString("nombre", nombre.getText().toString());
                    sharedPref.edit().putString("apellido", apellido.getText().toString());
                    sharedPref.edit().putString("email", email.getText().toString());
                    sharedPref.edit().putString("fechaNacimiento", fechaNacimiento.getText().toString());
                    sharedPref.edit().putString("contrasena", contrasena.getText().toString());
                    sharedPref.edit().commit();

                    irAMain();
                }
                else
                    error.setText("Hubo un error. Revise los datos ingresados y vuelva a intentarlo.");

                registrarse.setEnabled(true);

            }
        };

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarse.setEnabled(false);
                SharedServer sharedServer = new SharedServer();

                sharedServer.darAltaUsuario(nombre.getText().toString(), apellido.getText().toString(), email.getText().toString(), fechaNacimiento.getText().toString(), contrasena.getText().toString(), new DarAlta());

            }
        });
    }

    void irAMain()
    {
        Intent intent = new Intent(this, MediaIOMain.class);
        startActivity(intent);
    }
}
