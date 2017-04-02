package com.example.mediaio.mediaio.modelo;

import org.json.JSONObject;

/**
 * Created by Marcos on 01/04/2017.
 */

public abstract class JSONCallback {
    public abstract void ejecutar(JSONObject respuesta, long codigoServidor);
}
