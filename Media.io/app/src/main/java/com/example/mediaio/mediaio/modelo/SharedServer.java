package com.example.mediaio.mediaio.modelo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marcos on 01/04/2017.
 */

public class SharedServer extends InterfazRest{

    private long id;

    private static final String URLAPIREST = "https://musiciogrupo2.herokuapp.com/";

    public SharedServer()
    {
        id = 0;
    }

    public void existeUsuarioEmail(String email, JSONCallback callback)
    {
        enviarGET(URLAPIREST+"users/mail/"+email,callback);
    }

    public void configurarTokenEID(String token, long id)
    {
        configurarTokenAutenticacion(token);
        this.id = id;
    }


    public void obtenerToken(String email, String contrasena, JSONCallback callback)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("email", email);
            json.put("password",contrasena);
        }
        catch(JSONException e)
        {

        }
        enviarPOST(URLAPIREST+"token",json,callback);
    }

    public void darAltaUsuario(String nombre, String apellido, String email, String fechaNacimiento, String contrasena, String pais, String imagen, String nombreUsuario, JSONCallback callback)
    {
        JSONObject json = new JSONObject();

        try {
                json.put("userName", nombreUsuario);
                json.put("password",contrasena);
                json.put("firstName", nombre);
                json.put("lastName", apellido);
                json.put("country", pais);
                json.put("email", email);
                json.put("birthdate", fechaNacimiento);
                json.put("images",imagen);
        }
        catch(JSONException e)
        {

        }

        Log.e("Mensaje",json.toString());
        enviarPOST(URLAPIREST+"users",json,callback);
    }

    public void ModificarPerfilUsuario(String nombre, String apellido, String email, String fechaNacimiento, String contrasena, String pais, String imagen, String nombreUsuario, JSONCallback callback)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("userName", nombreUsuario);
            json.put("password",contrasena);
            json.put("firstName", nombre);
            json.put("lastName", apellido);
            json.put("country", pais);
            json.put("email", email);
            json.put("birthdate", fechaNacimiento);
            json.put("images",imagen);
        }
        catch(JSONException e)
        {

        }

        enviarPUT(URLAPIREST+"users/"+Long.toString(id),json,callback);
    }

    public void ObtenerCanciones(JSONCallback callback)
    {
        enviarGET(URLAPIREST+"tracks", callback);
    }
}
