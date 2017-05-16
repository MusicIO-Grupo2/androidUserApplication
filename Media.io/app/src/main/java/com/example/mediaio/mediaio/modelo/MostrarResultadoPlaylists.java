package com.example.mediaio.mediaio.modelo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.MediaIOAlbum;
import com.example.mediaio.mediaio.MediaIOPlaylist;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 15/05/2017.
 */

public class MostrarResultadoPlaylists extends ProcesarResultado {


    public MostrarResultadoPlaylists(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar) {
        super(lista, actividad, idVistaAInflar);
    }

    @Override
    public void llenarVista(View view, final JSONObject json) {
        TextView nombre = (TextView) view.findViewById(R.id.NombrePlaylist);
        ImageView informacion = (ImageView) view.findViewById(R.id.InformacionPlaylist);
        try {
            nombre.setText(json.getString("Nombre"));

            informacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        irAInformacionPlaylist(json.getString("ID"));
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

    private void irAInformacionPlaylist(String id)
    {
        Intent intent = new Intent(actividad, MediaIOPlaylist.class);
        intent.putExtra("idPlaylist", id);
        actividad.startActivity(intent);
    }
}
