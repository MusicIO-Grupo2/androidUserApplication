package com.example.mediaio.mediaio.modelo;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.MediaIOAlbum;
import com.example.mediaio.mediaio.MediaIOBusqueda;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Marcos on 15/05/2017.
 */

public class MostrarResultadoAlbumes extends ProcesarResultado {


    public MostrarResultadoAlbumes(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar) {
        super(lista, actividad, idVistaAInflar);
    }

    @Override
    public void llenarVista(View view, final JSONObject json) {
        TextView nombre = (TextView) view.findViewById(R.id.NombreAlbum);
        TextView banda = (TextView) view.findViewById(R.id.NombreBanda);
        ImageView informacion = (ImageView) view.findViewById(R.id.InformacionAlbum);
        try {
            nombre.setText(json.getString("nombre"));
            banda.setText(json.getString("Artista"));

            informacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        irAInformacionAlbum(json.getString("ID"));
                    }
                    catch (JSONException e)
                    {
                    }
                }
            });
        }
        catch (JSONException e)
        {
        }
    }

    private void irAInformacionAlbum(String id)
    {
        Intent intent = new Intent(actividad, MediaIOAlbum.class);
        intent.putExtra("id", id);
        actividad.startActivity(intent);
    }
}
