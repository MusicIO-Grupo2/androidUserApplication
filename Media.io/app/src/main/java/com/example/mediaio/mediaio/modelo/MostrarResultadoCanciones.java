package com.example.mediaio.mediaio.modelo;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Marcos on 15/05/2017.
 */

public class MostrarResultadoCanciones extends ProcesarResultado {


    public MostrarResultadoCanciones(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar) {
        super(lista, actividad, idVistaAInflar);
    }

    @Override
    public void llenarVista(View view, final JSONObject json)
    {
        TextView nombreCancion = (TextView) view.findViewById(R.id.NombreCancion);
        TextView nombreBanda = (TextView) view.findViewById(R.id.NombreBanda);
        TextView nombreAlbum = (TextView) view.findViewById(R.id.NombreAlbum);
        ImageView botonPlay = (ImageView) view.findViewById(R.id.ReproducirCancion);
        try {
            nombreCancion.setText(json.getString("Nombre"));
            nombreBanda.setText(json.getString("Banda"));
            nombreAlbum.setText(json.getString("Album"));
            botonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ArrayList<String> playlist = new ArrayList<String>();
                        playlist.add(json.getString("URL"));

                        actividad.reproducirPlaylist(playlist);
                    }
                    catch (JSONException e)
                    {

                    }
                }
            });

        }catch (JSONException e)
        {

        }
    }
}
