package com.example.mediaio.mediaio.modelo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 01/04/2017.
 */

public class SharedServer extends InterfazRest{

    private static final String URLAPIREST = "http://demo9499910.mockable.io/";

    public void existeUsuarioEmail(String email, JSONCallback callback)
    {
        enviarGET(URLAPIREST+"users?email="+email,callback);
    }

    public void obtenerToken(String email, String contrasena, JSONCallback callback)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("email", email);
            json.put("contrasena",contrasena);
        }
        catch(JSONException e)
        {

        }

        enviarPOST(URLAPIREST+"tokens",json,callback);
    }

    public void darAltaUsuario(String nombre, String apellido, String email, String fechaNacimiento, String contrasena, JSONCallback callback)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("nombre", nombre);
            json.put("apellido", apellido);
            json.put("fechaNacimiento", fechaNacimiento);
            json.put("email", email);
            json.put("contrasena",contrasena);
        }
        catch(JSONException e)
        {

        }

        enviarPOST(URLAPIREST+"tokens",json,callback);
    }
}
