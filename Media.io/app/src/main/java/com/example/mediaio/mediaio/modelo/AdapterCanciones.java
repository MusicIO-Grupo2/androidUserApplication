package com.example.mediaio.mediaio.modelo;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Marcos on 29/04/2017.
 */

public class AdapterCanciones implements ListAdapter {

    private List<JSONObject> ListaJson;
    private ActividadPrincipal contexto;

    public AdapterCanciones(ActividadPrincipal contexto, List<JSONObject> ListaJson)
    {
        this.contexto = contexto;
        this.ListaJson = ListaJson;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return ListaJson.size();
    }

    @Override
    public Object getItem(int position) {
        return ListaJson.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vista;

        LayoutInflater inflater = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vista = inflater.inflate(R.layout.vista_cancion, parent, false);

        llenarCancion(vista, ListaJson.get(position));

        return vista;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    void llenarCancion(View view, JSONObject json)
    {
        TextView nombreCancion = (TextView) view.findViewById(R.id.NombreCancion);
        TextView nombreBanda = (TextView) view.findViewById(R.id.NombreBanda);
        ImageView ImagenCancion = (ImageView) view.findViewById(R.id.ImagenCancion);
        ImageView botonPlay = (ImageView) view.findViewById(R.id.ReproducirCancion);
        try {
            nombreCancion.setText(json.getString("Nombre"));
            nombreBanda.setText(json.getString("Descripcion"));
            ImagenCancion.setImageResource(R.drawable.clavesol);

       }catch (JSONException e)
        {

        }
    }
}
