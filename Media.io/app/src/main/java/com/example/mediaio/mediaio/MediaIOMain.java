package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mediaio.mediaio.modelo.InterfazRest;
import com.example.mediaio.mediaio.modelo.JSONCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MediaIOMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iomain);

        TextView nombre = (TextView) findViewById(R.id.nombre);
        TextView apellido = (TextView) findViewById(R.id.apellido);
        TextView email = (TextView) findViewById(R.id.email);
        TextView fechaNacimeinto = (TextView) findViewById(R.id.fechaNacimiento);
        TextView token = (TextView) findViewById(R.id.token);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        nombre.setText(sharedPref.getString("nombre","NULL"));
        apellido.setText(sharedPref.getString("apellido","NULL"));
        email.setText(sharedPref.getString("email","NULL"));
        fechaNacimeinto.setText(sharedPref.getString("fechaNacimiento","NULL"));
        token.setText(Long.toString(sharedPref.getLong("token",0)));
    }
}
