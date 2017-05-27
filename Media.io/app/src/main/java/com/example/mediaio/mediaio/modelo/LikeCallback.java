package com.example.mediaio.mediaio.modelo;

import android.util.Log;
import android.widget.ImageView;

import com.example.mediaio.mediaio.R;

import org.json.JSONObject;

/**
 * Created by Marcos on 24/05/2017.
 */

public class LikeCallback extends Callback {

    SharedServer sharedServer;
    ImageView botonLike;
    String ID;
    boolean leGusta;

    public LikeCallback(SharedServer sharedServer, ImageView boton, String ID, boolean leGusta)
    {
        this.sharedServer = sharedServer;

        this.botonLike = boton;
        this.ID = ID;
        this.leGusta = leGusta;

        if(leGusta)
            botonLike.setImageResource(R.drawable.like);
    }

    @Override
    public void ejecutar() {

        JSONCallback callbacklike = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                if(codigoServidor == 200)
                {
                    botonLike.setImageResource(R.drawable.like);
                    leGusta = true;
                }
            }
        };

        JSONCallback callbackdislike = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                if(codigoServidor == 200)
                {
                    botonLike.setImageResource(R.drawable.like2);
                    leGusta = false;
                }
            }
        };

        if(leGusta)
            sharedServer.dislikeCancion(callbackdislike, ID);
        else
            sharedServer.likeCancion(callbacklike,ID);

    }
}
