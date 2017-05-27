package com.example.mediaio.mediaio.modelo;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Marcos on 15/05/2017.
 */

public class MostrarResultadoCancionesPlaylist extends ProcesarResultado {

    SharedServer sharedServer;
    String ID;

    public MostrarResultadoCancionesPlaylist(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar, SharedServer sharedServer, String ID) {
        super(lista, actividad, idVistaAInflar);
        this.sharedServer = sharedServer;
        this.ID = ID;
    }

    @Override
    public void llenarVista(final View view, final JSONObject json) {
        TextView nombreCancion = (TextView) view.findViewById(R.id.NombreCancion);
        TextView nombreBanda = (TextView) view.findViewById(R.id.NombreBanda);
        TextView nombreAlbum = (TextView) view.findViewById(R.id.NombreAlbum);
        RelativeLayout play = (RelativeLayout) view.findViewById(R.id.ReproducirCancion);
        final ImageView botonLike = (ImageView) view.findViewById(R.id.LikeCancion);
        ImageView menu = (ImageView) view.findViewById(R.id.CancionMenu);

        try {
            nombreCancion.setText(json.getString("Nombre"));
            nombreBanda.setText(json.getString("Banda"));
            nombreAlbum.setText(json.getString("Album"));

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ArrayList<String> playlist = new ArrayList<String>();
                        playlist.add(json.getString("URL"));

                        actividad.reproducirPlaylist(playlist);
                    } catch (JSONException e) {

                    }
                }
            });

            final LikeCallback likeCallback;

            try {
                likeCallback = new LikeCallback(sharedServer, botonLike, json.getString("ID"), json.getBoolean("LeGusta"));
            } catch (JSONException e) {
                return;
            }

            botonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    likeCallback.ejecutar();
                }
            });

        } catch (JSONException e) {

        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mostrarMenuCancion(v, view,  json.getString("ID"), ID);
                }
                catch (JSONException e)
                {

                }
            }
        });
    }

    private void mostrarMenuCancion(final View boton, final View cancion, final String IDCancion, final String IDPlaylist)
    {
        PopupMenu popupMenu = new PopupMenu(actividad, boton);
        popupMenu.getMenuInflater().inflate(R.menu.menu_cancion_playlist, popupMenu.getMenu());

        final JSONCallback jsonCallback = new JSONCallback() {
            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                ((ViewGroup) cancion.getParent()).removeView(cancion);
            }
        };

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == actividad.getResources().getString(R.string.menuCancionEliminarCancionPlaylist))
                    sharedServer.eliminarCancionPlaylist(jsonCallback,IDPlaylist,IDCancion);

                return true;
            }

        });

        popupMenu.show();
    }
}
