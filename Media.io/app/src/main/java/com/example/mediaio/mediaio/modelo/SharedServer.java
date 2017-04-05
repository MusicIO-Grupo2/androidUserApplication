package com.example.mediaio.mediaio.modelo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 01/04/2017.
 */

public class SharedServer extends InterfazRest{

    private static final String URLAPIREST = "https://musiciogrupo2.herokuapp.com/";

    public void existeUsuarioEmail(String email, JSONCallback callback)
    {
        enviarGET(URLAPIREST+"registrado?email="+email,callback);
    }

    public void obtenerToken(String email, String contrasena, JSONCallback callback)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("Email", email);
            json.put("Password",contrasena);
        }
        catch(JSONException e)
        {

        }

        enviarPOST(URLAPIREST+"auth/login",json,callback);
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
