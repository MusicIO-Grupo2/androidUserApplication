package com.example.mediaio.mediaio.modelo;

import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.MediaIOChat;
import com.example.mediaio.mediaio.MediaIOContactos;
import com.example.mediaio.mediaio.MediaIOUsuario;
import com.example.mediaio.mediaio.R;
import com.example.mediaio.mediaio.modelo.ProcesarResultado;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 26/05/2017.
 */

public class MostrarResultadoContactos extends ProcesarResultado {
    SharedServer sharedServer;

    public MostrarResultadoContactos(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar, SharedServer sharedServer) {
        super(lista, actividad, idVistaAInflar);
        this.sharedServer = sharedServer;
    }

    @Override
    public void llenarVista(final View view, final JSONObject json) {
        TextView nombre = (TextView) view.findViewById(R.id.NombreUsuario);
        RelativeLayout informacion = (RelativeLayout) view.findViewById(R.id.VerInformacionUsuario);
        ImageView menu = (ImageView) view.findViewById(R.id.DejarSeguirUsuario);
        ImageView enviarMensaje = (ImageView) view.findViewById(R.id.EnviarMensajeContacto);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mostrarMenu(v,json.getString("ID"),view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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

        enviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    irAMensajes(json.getString("ID"),json.getString("NombreUsuario"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void mostrarMenu(final View boton, final String IDUsuario, final View vista)
    {
        PopupMenu popupMenu = new PopupMenu(actividad, boton);
        popupMenu.getMenuInflater().inflate(R.menu.menu_contacto, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == actividad.getResources().getString(R.string.menuCancionAgregarCancionPlaylist)) {
                    JSONCallback callback = new JSONCallback() {
                        @Override
                        public void ejecutar(JSONObject respuesta, long codigoServidor) {
                            ((ViewGroup) vista.getParent()).removeView(vista);
                            Toast.makeText(actividad.getApplicationContext(),"Listo",Toast.LENGTH_SHORT);
                        }
                    };

                    sharedServer.dejarSeguirUsuario(callback,IDUsuario);

                }

                return true;
            }

        });

        popupMenu.show();
    }

    private void irAInformacionUsuario(String id)
    {
        Intent intent = new Intent(actividad, MediaIOUsuario.class);
        intent.putExtra("idUsuario", id);
        actividad.startActivity(intent);
    }

    private void irAMensajes(String id, String nombreUsuario)
    {
        Intent intent = new Intent(actividad, MediaIOChat.class);
        intent.putExtra("idUsuario", id);
        intent.putExtra("NombreUsuario",nombreUsuario);
        actividad.startActivity(intent);
    }
}