package com.example.mediaio.mediaio.modelo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Marcos on 15/05/2017.
 */

public class MostrarResultadoCanciones extends ProcesarResultado {

    SharedServer sharedServer;
    String IDUsuario;

    public MostrarResultadoCanciones(LinearLayout lista, ActividadPrincipal actividad, int idVistaAInflar, SharedServer sharedServer, String IDUsuario) {
        super(lista, actividad, idVistaAInflar);
        this.sharedServer = sharedServer;
        this.IDUsuario = IDUsuario;
    }

    @Override
    public void llenarVista(View view, final JSONObject json)
    {
        TextView nombreCancion = (TextView) view.findViewById(R.id.NombreCancion);
        TextView nombreBanda = (TextView) view.findViewById(R.id.NombreBanda);
        TextView nombreAlbum = (TextView) view.findViewById(R.id.NombreAlbum);
        RelativeLayout play = (RelativeLayout) view.findViewById(R.id.ReproducirCancion);
        final ImageView botonLike = (ImageView) view.findViewById(R.id.LikeCancion);

        ImageView botonMenu = (ImageView) view.findViewById(R.id.CancionMenu);
        botonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mostrarMenuCancion(v, json.getString("Nombre"));
                }
                catch (JSONException e)
                {

                }
            }
        });



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
                    }
                    catch (JSONException e)
                    {

                    }
                }
            });

            final LikeCallback likeCallback;

            try {
                likeCallback = new LikeCallback(sharedServer, botonLike, json.getString("ID"), json.getBoolean("LeGusta"));
            }
            catch (JSONException e)
            {
                return;
            }

            botonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    likeCallback.ejecutar();
                }
            });

        }catch (JSONException e)
        {

        }
    }

    private void mostrarMenuCancion(final View boton, final String IDCancion)
    {
        PopupMenu popupMenu = new PopupMenu(actividad, boton);
        popupMenu.getMenuInflater().inflate(R.menu.menu_cancion, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == actividad.getResources().getString(R.string.menuCancionAgregarCancionPlaylist)) {
                    JSONCallback callback = new JSONCallback() {
                        @Override
                        public void ejecutar(JSONObject respuesta, long codigoServidor) {
                            mostrarDialogoSeleccionarPlaylist(IDCancion, respuesta);
                        }
                    };

                    sharedServer.buscarPlaylists(callback,IDUsuario);

                }

                return true;
            }

        });

        popupMenu.show();
    }

    void mostrarDialogoSeleccionarPlaylist(final String IDCancion, JSONObject respuesta)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle(R.string.CancionAgregarAPlaylist);

        ArrayList<String> opciones = new ArrayList<String>();
        final ArrayList<String> idOpciones = new ArrayList<String>();

        Iterator<String> i = respuesta.keys();
        while(i.hasNext())
        {
            try {
                JSONObject elemento = (JSONObject) respuesta.get(i.next());
                opciones.add(elemento.getString("Nombre"));
                idOpciones.add(elemento.getString("ID"));
            }
            catch (JSONException e)
            {
            }
        }

        builder.setItems(opciones.toArray(new CharSequence[opciones.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                JSONCallback procesarResultado = new JSONCallback() {
                    @Override
                    public void ejecutar(JSONObject respuesta, long codigoServidor) {
                        if(codigoServidor == 200)
                            Toast.makeText(actividad.getApplicationContext(),"Cancion agregada con exito", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(actividad.getApplicationContext(),"No se pudo agregar la cancion", Toast.LENGTH_SHORT).show();

                    }
                };
                sharedServer.agregarCancionAPlaylist(procesarResultado,IDCancion,idOpciones.get(which));
            }
        });

        builder.setNegativeButton(R.string.CancionCancelarAgregarAPlaylist, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
