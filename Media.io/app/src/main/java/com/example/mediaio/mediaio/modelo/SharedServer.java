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
        enviarPOST(URLAPIREST+"token/",json,callback);
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
                json.put("Email", email);
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

    public void buscarCanciones(JSONCallback callback, String nombre, int cantidad)
    {
        JSONObject objeto = new JSONObject();
        //enviarGET(URLAPIREST+"tracks", callback);

        try {
            JSONObject art1 = new JSONObject();
            art1.put("Banda", "Queens of the stone age");
            art1.put("Album", "Rated R");
            art1.put("Nombre","Infinity");
            art1.put("URL","http://www.certifiedpowercoach.com/3.mp3");
            art1.put("Duracion",100);
            objeto.put("1",art1);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art2 = new JSONObject();
            art2.put("Banda", "Queens of the stone age");
            art2.put("Nombre","Infinity");
            art2.put("Album", "Rated R");
            art2.put("URL","http://www.certifiedpowercoach.com/2.mp3");
            art2.put("Duracion",100);
            objeto.put("2",art2);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art3 = new JSONObject();
            art3.put("Banda", "Queens of the stone age");
            art3.put("Nombre","Infinity");
            art3.put("Album", "Rated R");
            art3.put("URL","http://www.certifiedpowercoach.com/1.mp3");
            art3.put("Duracion",100);
            objeto.put("3",art3);

        }
        catch (JSONException e)
        {

        }

        if(cantidad > 3) {
            try {
                JSONObject art4 = new JSONObject();
                art4.put("Banda", "Queens of the stone age");
                art4.put("Nombre", "Infinity");
                art4.put("Album", "Rated R");
                art4.put("URL", "http://www.certifiedpowercoach.com/3.mp3");
                art4.put("Duracion", 100);
                objeto.put("4", art4);
            } catch (JSONException e) {

            }

            try {
                JSONObject art5 = new JSONObject();
                art5.put("Banda", "Queens of the stone age");
                art5.put("Nombre", "Infinity");
                art5.put("Album", "Rated R");
                art5.put("URL", "http://www.certifiedpowercoach.com/2.mp3");
                art5.put("Duracion", 100);
                objeto.put("5", art5);
            } catch (JSONException e) {

            }

            try {
                JSONObject art6 = new JSONObject();
                art6.put("Banda", "Queens of the stone age");
                art6.put("Nombre", "Infinity");
                art6.put("Album", "Rated R");
                art6.put("URL", "http://www.certifiedpowercoach.com/1.mp3");
                art6.put("Duracion", 100);
                objeto.put("6", art6);
            } catch (JSONException e) {

            }
        }

        callback.ejecutar(objeto, 200);
    }

    public void buscarArtistas(JSONCallback callback, String nombre, int cantidad)
    {
        //enviarGET(URLAPIREST+"tracks", callback);

        JSONObject objeto = new JSONObject();

        try {
            JSONObject art1 = new JSONObject();
            art1.put("Nombre", "Queens of the stone age");
            art1.put("ID","1");
            objeto.put("1",art1);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art2 = new JSONObject();
            art2.put("Nombre", "Queens of the stone age");
            art2.put("ID","2");
            objeto.put("2",art2);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art3 = new JSONObject();
            art3.put("Nombre", "Queens of the stone age");
            art3.put("ID","3");
            objeto.put("3",art3);

        }
        catch (JSONException e)
        {

        }

        if(cantidad > 3) {
            try {
                JSONObject art4 = new JSONObject();
                art4.put("Nombre", "Queens of the stone age");
                art4.put("ID","4");
                objeto.put("4", art4);
            } catch (JSONException e) {

            }

            try {
                JSONObject art5 = new JSONObject();
                art5.put("Nombre", "Queens of the stone age");
                art5.put("ID","5");
                objeto.put("5", art5);
            } catch (JSONException e) {

            }

            try {
                JSONObject art6 = new JSONObject();
                art6.put("Nombre", "Queens of the stone age");
                art6.put("ID","6");
                objeto.put("6", art6);
            } catch (JSONException e) {

            }
        }

        callback.ejecutar(objeto, 200);
    }

    public void buscarAlbumes(JSONCallback callback, String nombre, int cantidad)
    {
        //enviarGET(URLAPIREST+"tracks", callback);

        JSONObject objeto = new JSONObject();

        try {
            JSONObject art1 = new JSONObject();
            art1.put("Artista", "Queens of the stone age");
            art1.put("nombre", "Rated R");
            art1.put("ID","1");
            objeto.put("1",art1);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art2 = new JSONObject();
            art2.put("Artista", "Queens of the stone age");
            art2.put("nombre", "Rated R");
            art2.put("ID","2");
            objeto.put("2",art2);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art3 = new JSONObject();
            art3.put("Artista", "Queens of the stone age");
            art3.put("nombre", "Rated R");
            art3.put("ID","3");
            objeto.put("3",art3);

        }
        catch (JSONException e)
        {

        }

        if(cantidad > 3) {
            try {
                JSONObject art4 = new JSONObject();
                art4.put("Artista", "Queens of the stone age");
                art4.put("nombre", "Rated R");
                art4.put("ID","4");
                objeto.put("4", art4);
            } catch (JSONException e) {

            }

            try {
                JSONObject art5 = new JSONObject();
                art5.put("Artista", "Queens of the stone age");
                art5.put("nombre", "Rated R");
                art5.put("ID","5");
                objeto.put("5", art5);
            } catch (JSONException e) {

            }

            try {
                JSONObject art6 = new JSONObject();
                art6.put("Artista", "Queens of the stone age");
                art6.put("nombre", "Rated R");
                art6.put("ID","6");
                objeto.put("6", art6);
            } catch (JSONException e) {

            }
        }

        callback.ejecutar(objeto, 200);
    }

    public void buscarInformacionAlbum(JSONCallback callback, String id)
    {
        JSONObject objeto = new JSONObject();
        //enviarGET(URLAPIREST+"tracks", callback);

        try {
            JSONObject art1 = new JSONObject();
            art1.put("Banda", "Queens of the stone age");
            art1.put("Album", "Rated R");
            art1.put("Nombre","Infinity");
            art1.put("URL","http://www.certifiedpowercoach.com/3.mp3");
            art1.put("Duracion",100);
            objeto.put("1",art1);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art2 = new JSONObject();
            art2.put("Banda", "Queens of the stone age");
            art2.put("Nombre","Infinity");
            art2.put("Album", "Rated R");
            art2.put("URL","http://www.certifiedpowercoach.com/2.mp3");
            art2.put("Duracion",100);
            objeto.put("2",art2);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art3 = new JSONObject();
            art3.put("Banda", "Queens of the stone age");
            art3.put("Nombre","Infinity");
            art3.put("Album", "Rated R");
            art3.put("URL","http://www.certifiedpowercoach.com/1.mp3");
            art3.put("Duracion",100);
            objeto.put("3",art3);

        }
        catch (JSONException e)
        {

        }


            try {
                JSONObject art4 = new JSONObject();
                art4.put("Banda", "Queens of the stone age");
                art4.put("Nombre", "Infinity");
                art4.put("Album", "Rated R");
                art4.put("URL", "http://www.certifiedpowercoach.com/3.mp3");
                art4.put("Duracion", 100);
                objeto.put("4", art4);
            } catch (JSONException e) {

            }

            try {
                JSONObject art5 = new JSONObject();
                art5.put("Banda", "Queens of the stone age");
                art5.put("Nombre", "Infinity");
                art5.put("Album", "Rated R");
                art5.put("URL", "http://www.certifiedpowercoach.com/2.mp3");
                art5.put("Duracion", 100);
                objeto.put("5", art5);
            } catch (JSONException e) {

            }

            try {
                JSONObject art6 = new JSONObject();
                art6.put("Banda", "Queens of the stone age");
                art6.put("Nombre", "Infinity");
                art6.put("Album", "Rated R");
                art6.put("URL", "http://www.certifiedpowercoach.com/1.mp3");
                art6.put("Duracion", 100);
                objeto.put("6", art6);
            } catch (JSONException e) {

            }

            JSONObject album = new JSONObject();

        try {
            album.put("artista", "Queens of the stone age");
            album.put("genero", "Stoner Rock");
            album.put("nombre","Unrelease Dopes");
            album.put("fecha","12/12/2000");
            album.put("tracks",objeto);
        }
        catch (JSONException e)
        {

        }


        callback.ejecutar(album, 200);
    }

    public void buscarInformacionPlaylist(JSONCallback callback, String id)
    {
        JSONObject objeto = new JSONObject();
        //enviarGET(URLAPIREST+"tracks", callback);

        try {
            JSONObject art1 = new JSONObject();
            art1.put("Banda", "Queens of the stone age");
            art1.put("Album", "Rated R");
            art1.put("Nombre","Infinity");
            art1.put("URL","http://www.certifiedpowercoach.com/3.mp3");
            art1.put("Duracion",100);
            objeto.put("1",art1);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art2 = new JSONObject();
            art2.put("Banda", "Queens of the stone age");
            art2.put("Nombre","Infinity");
            art2.put("Album", "Rated R");
            art2.put("URL","http://www.certifiedpowercoach.com/2.mp3");
            art2.put("Duracion",100);
            objeto.put("2",art2);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art3 = new JSONObject();
            art3.put("Banda", "Queens of the stone age");
            art3.put("Nombre","Infinity");
            art3.put("Album", "Rated R");
            art3.put("URL","http://www.certifiedpowercoach.com/1.mp3");
            art3.put("Duracion",100);
            objeto.put("3",art3);

        }
        catch (JSONException e)
        {

        }


        try {
            JSONObject art4 = new JSONObject();
            art4.put("Banda", "Queens of the stone age");
            art4.put("Nombre", "Infinity");
            art4.put("Album", "Rated R");
            art4.put("URL", "http://www.certifiedpowercoach.com/3.mp3");
            art4.put("Duracion", 100);
            objeto.put("4", art4);
        } catch (JSONException e) {

        }

        try {
            JSONObject art5 = new JSONObject();
            art5.put("Banda", "Queens of the stone age");
            art5.put("Nombre", "Infinity");
            art5.put("Album", "Rated R");
            art5.put("URL", "http://www.certifiedpowercoach.com/2.mp3");
            art5.put("Duracion", 100);
            objeto.put("5", art5);
        } catch (JSONException e) {

        }

        try {
            JSONObject art6 = new JSONObject();
            art6.put("Banda", "Queens of the stone age");
            art6.put("Nombre", "Infinity");
            art6.put("Album", "Rated R");
            art6.put("URL", "http://www.certifiedpowercoach.com/1.mp3");
            art6.put("Duracion", 100);
            objeto.put("6", art6);
        } catch (JSONException e) {

        }

        JSONObject album = new JSONObject();

        try {
            album.put("nombre", "Mi playlist");
            album.put("descripcion","Esta la tengo para la fiesta de fin de año.");
            album.put("tracks",objeto);
        }
        catch (JSONException e)
        {

        }


        callback.ejecutar(album, 200);
    }

    public void buscarPlaylists(JSONCallback callback, String id)
    {
        JSONObject objeto = new JSONObject();
        //enviarGET(URLAPIREST+"tracks", callback);

        try {
            JSONObject art1 = new JSONObject();
            art1.put("Nombre", "Mi playlist");
            art1.put("ID", "2");
            objeto.put("1",art1);
        }
        catch (JSONException e)
        {

        }

        try {
            JSONObject art2 = new JSONObject();
            art2.put("Nombre", "Mi playlist 2");
            art2.put("ID", "3");
            objeto.put("2",art2);
        }
        catch (JSONException e) {

        }


        callback.ejecutar(objeto, 200);
    }


}
