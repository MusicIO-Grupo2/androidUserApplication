package com.example.mediaio.mediaio.modelo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.R;

import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

/**
 * Created by Marcos on 13/05/2017.
 */

public class ControlReproduccion {

    private Boolean enPantalla;
    private View anchorView;
    private LinearLayout reproductor;
    private IControlesReproduccion control;

    private ImageView play;
    private ImageView next;
    private ImageView prev;
    private LinearLayout pos;

    public ControlReproduccion(final LinearLayout anchorView, Boolean mostrar, final IControlesReproduccion control) {


        this.control = control;
        LayoutInflater inflater = (LayoutInflater)  anchorView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        reproductor = (LinearLayout) inflater.inflate(R.layout.reproductor, null);

        play = (ImageView) reproductor.findViewById(R.id.BotonPlay);
        prev = (ImageView) reproductor.findViewById(R.id.BotonCancionAnterior);
        next = (ImageView) reproductor.findViewById(R.id.BotonCancionSiguiente);
        pos = (LinearLayout) reproductor.findViewById(R.id.BarraPosicionCancion);

        if(control.isPlaying())
            play.setImageResource(R.drawable.boton_reproductor_stop);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!control.isPlaying()) {
                    control.start();

                    if(control.isPlaying())
                        play.setImageResource(R.drawable.boton_reproductor_stop);
                }
                else
                {
                    control.pause();
                    play.setImageResource(R.drawable.boton_reproductor_play);
                }

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.prev();

                if(control.isPlaying())
                    play.setImageResource(R.drawable.boton_reproductor_stop);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.next();

                if(control.isPlaying())
                    play.setImageResource(R.drawable.boton_reproductor_stop);
            }
        });

        anchorView.addView(reproductor);

    }

    public void show() {
        reproductor.setVisibility(View.VISIBLE);
    }

    public void hide()
    {
        reproductor.setVisibility(View.GONE);
    }
}
