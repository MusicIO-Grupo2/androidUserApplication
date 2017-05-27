package com.example.mediaio.mediaio.modelo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.MediaIOAlbum;
import com.example.mediaio.mediaio.MediaIOListPlaylist;
import com.example.mediaio.mediaio.MediaIOPlaylist;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 15/05/2017.
 */

public class MostrarResultadoPlaylists extends ProcesarResultado {

    SharedServer sharedServer;

    public MostrarResultadoPlaylists(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar, SharedServer sharedServer) {
        super(lista, actividad, idVistaAInflar);
        this.sharedServer = sharedServer;
    }

    @Override
    public void llenarVista(View view, final JSONObject json) {
        TextView nombre = (TextView) view.findViewById(R.id.NombrePlaylist);
        ImageView informacion = (ImageView) view.findViewById(R.id.InformacionPlaylist);
        ImageView menu = (ImageView) view.findViewById(R.id.playlistMenuPopup);
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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                try {
                    mostrarMenuPlaylist(v, json.getString("ID"));
                }
                catch (JSONException e)
                {

                }
            }
        });


    }

    private void irAInformacionPlaylist(String id)
    {
        Intent intent = new Intent(actividad, MediaIOPlaylist.class);
        intent.putExtra("idPlaylist", id);
        actividad.startActivity(intent);
    }

    private void mostrarMenuPlaylist(View view, final String ID)
    {
        PopupMenu popupMenu = new PopupMenu(actividad, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_playlist, popupMenu.getMenu());



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == actividad.getResources().getString(R.string.menuPlaylistEliminarPlaylist))
                    mostrarDialogoAceptarEliminar(ID);

                return true;
            }

        });

        popupMenu.show();
    }

    void recargar()
    {
        Intent intent = new Intent(actividad, MediaIOListPlaylist.class);
        actividad.startActivity(intent);
    }

    void mostrarDialogoAceptarEliminar(final String ID)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle(R.string.PlayListEliminarTitulo);
        builder.setMessage(R.string.PlayListEliminarMensaje);

        final JSONCallback jsonCallback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                recargar();
            }
        };

        builder.setPositiveButton(R.string.PlaylistEliminarContinuar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedServer.eliminarPlaylist(jsonCallback, ID);
            }
        });

        builder.setNegativeButton(R.string.playlistEliminarCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });



        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
