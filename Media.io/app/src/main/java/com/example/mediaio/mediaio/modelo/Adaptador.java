package com.example.mediaio.mediaio.modelo;

import android.widget.ListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 14/05/2017.
 */

public abstract interface Adaptador extends ListAdapter {
    public abstract void setLista(List lista);
}
