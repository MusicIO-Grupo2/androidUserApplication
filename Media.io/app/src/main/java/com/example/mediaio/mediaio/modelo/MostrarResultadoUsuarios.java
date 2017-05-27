package com.example.mediaio.mediaio.modelo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.MediaIOArtista;
import com.example.mediaio.mediaio.MediaIOUsuario;
import com.example.mediaio.mediaio.R;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 16/05/2017.
 */

public class MostrarResultadoUsuarios extends ProcesarResultado {
    SharedServer sharedServer;

    public MostrarResultadoUsuarios(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar, SharedServer sharedServer) {
        super(lista, actividad, idVistaAInflar);
        this.sharedServer = sharedServer;
    }

    @Override
    public void llenarVista(View view, final JSONObject json) {
        TextView nombre = (TextView) view.findViewById(R.id.NombreUsuario);
        RelativeLayout informacion = (RelativeLayout) view.findViewById(R.id.VerInformacionUsuario);
        ImageView seguir = (ImageView) view.findViewById(R.id.SeguirUsuario);

        try {
            nombre.setText(json.getString("NombreUsuario") + " - " + json.getString("Nombre") + " " + json.getString("Apellido"));

            informacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        irAInformacionUsuario(json.getString("ID"));
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

        final JSONCallback callback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                Toast.makeText(actividad.getApplicationContext(),"Listo",Toast.LENGTH_SHORT).show();
            }
        };

        seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sharedServer.seguirUsuario(callback,json.getString("ID"));
                }
                catch (JSONException e)
                {
                }

            }
        });
    }

    private void irAInformacionUsuario(String id)
    {
        Intent intent = new Intent(actividad, MediaIOUsuario.class);
        intent.putExtra("idUsuario", id);
        actividad.startActivity(intent);
    }
}