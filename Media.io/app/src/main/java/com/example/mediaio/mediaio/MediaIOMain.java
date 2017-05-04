package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.AdapterCanciones;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Esta es la que consideramos la pantalla Main por ser el inicio de la aplicacion realmente

public class MediaIOMain extends ActividadPrincipal {
    SharedServer sharedServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iomain);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        ListView lista = (ListView) findViewById(R.id.ListaCanciones);
        MostrarCanciones canciones = new MostrarCanciones(lista, this);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));
        sharedServer.ObtenerCanciones(canciones);
    }

    class MostrarCanciones extends JSONCallback
    {
        ListView lista;
        ActividadPrincipal actividad;


        MostrarCanciones(ListView lista, ActividadPrincipal actividad) {
            this.actividad = actividad;
            this.lista = lista;
        }

        @Override
        public void ejecutar(JSONObject respuesta, long codigoServidor) {
            List<JSONObject> listaJson = new ArrayList<JSONObject>();
            if(codigoServidor == 200) {
                try {


                    Iterator<String> claves = respuesta.keys();
                    while(claves.hasNext())
                    {
                        JSONObject elemento = (JSONObject) respuesta.get(claves.next());
                        listaJson.add(elemento);
                    }
                    lista.setAdapter(new AdapterCanciones(actividad, listaJson));
                } catch (JSONException e) {
                }
            }
        }
    }
}
