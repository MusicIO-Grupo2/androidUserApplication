package com.example.mediaio.mediaio.modelo;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Marcos on 14/05/2017.
 */

public abstract class ProcesarResultado extends JSONCallback
{
    protected LinearLayout lista;
    protected ActividadPrincipal actividad;
    private  int idVistaAInflar;


    public ProcesarResultado(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar) {
        this.actividad = actividad;
        this.lista = lista;
        this.idVistaAInflar = idVistaAInflar;
    }

    @Override
    public void ejecutar(JSONObject respuesta, long codigoServidor) {
        if(codigoServidor == 200) {
            try {
                Iterator<String> i = respuesta.keys();

                while(i.hasNext())
                {
                    JSONObject elemento = (JSONObject) respuesta.get(i.next());

                    View vista;

                    vista = actividad.getLayoutInflater().inflate(idVistaAInflar, lista, false);


                    llenarVista(vista, elemento);



                    lista.addView(vista);
                }


            } catch (JSONException e) {
            }
        }
    }

    public abstract void llenarVista(View view, final JSONObject json);

}