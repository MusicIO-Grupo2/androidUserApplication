package com.example.mediaio.mediaio.modelo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.MediaIOArtista;
import com.example.mediaio.mediaio.R;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 16/05/2017.
 */

public class MostrarResultadoArtistas extends ProcesarResultado {

    public MostrarResultadoArtistas(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar) {
        super(lista, actividad, idVistaAInflar);
    }

    @Override
    public void llenarVista(View view, final JSONObject json) {
        TextView nombre = (TextView) view.findViewById(R.id.NombreArtista);
        ImageView informacion = (ImageView) view.findViewById(R.id.VerInformacionArtista);
        try {
            nombre.setText(json.getString("Nombre"));

            informacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        irAInformacionArtista(json.getString("ID"));
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

    private void irAInformacionArtista(String id)
    {
        Intent intent = new Intent(actividad, MediaIOArtista.class);
        intent.putExtra("idArtista", id);
        actividad.startActivity(intent);
    }
}
