package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    final static long MILISEGUNDOSSPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setea un fondo aleatorio para la pantalla principal.

        RelativeLayout fondo = (RelativeLayout) findViewById(R.id.ventana);
        Random generadorNumeros = new Random();

        TypedArray fondos = getResources().obtainTypedArray(R.array.fondos);
        int idFondo = fondos.getResourceId( (int)(generadorNumeros.nextDouble()* fondos.length() + 1), R.drawable.background1);

        //Guarda el id de fondo para que lo usen otras actividades
        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
        sharedPref.edit().putInt("idFondo", idFondo).commit();

        fondo.setBackgroundResource(idFondo);
        fondos.recycle();

        //Lanza un timer que al terminar pasa a la pantalla de login o la principal si esta logeado.

        TimerTask tareaTimer = new TimerTask() {
            @Override
            public void run() {

                if(sharedPref.getString("token","0") != "0")
                    irAMain();
                else
                    irALogin();
            }
        };
        Timer timer = new Timer();

        timer.schedule(tareaTimer, MILISEGUNDOSSPLASH);
    }
    void irAMain()
    {
        Intent intent = new Intent(this, MediaIOBusqueda.class);
        startActivity(intent);
    }
    void irALogin()
    {
        Intent intent = new Intent(this, LoginFirstActivity.class);
        startActivity(intent);
    }
}
