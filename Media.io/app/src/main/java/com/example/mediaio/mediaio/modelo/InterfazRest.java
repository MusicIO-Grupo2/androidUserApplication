package com.example.mediaio.mediaio.modelo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Marcos on 01/04/2017.
 */

abstract public class InterfazRest {

    InterfazRest()
    {

    }

    protected void enviarPOST(final String URL, final JSONObject json, final JSONCallback callback)
    {
        class POST extends AsyncTask<String,Integer,JSONObject> {

            long codigoServidor;

            protected JSONObject doInBackground(String... params) {

                JSONObject result;

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost post = new HttpPost(URL);

                post.setHeader("content-type", "application/json");

                try
                {
                    //Configura post para que envie el json.
                    StringEntity entidad = new StringEntity(json.toString());
                    post.setEntity(entidad);

                    //Envio y espero la peticion.
                    HttpResponse resp = httpClient.execute(post);
                    String respStr = EntityUtils.toString(resp.getEntity());
                    codigoServidor = resp.getStatusLine().getStatusCode();

                    result = new JSONObject(respStr);
                }
                catch(Exception ex)
                {
                    result = new JSONObject();
                }

                return result;
            }

            protected void onPostExecute(JSONObject result) {
                callback.ejecutar(result, codigoServidor);
            }
        }

        POST peticion = new POST();

        peticion.execute();
    }

    protected void enviarGET(final String URL, final JSONCallback callback)
    {
        class GET extends AsyncTask<String,Integer,JSONObject> {

            long codigoServidor;

            protected JSONObject doInBackground(String... params) {

                JSONObject result;

                HttpClient httpClient = new DefaultHttpClient();

                HttpGet get = new HttpGet(URL);

                get.setHeader("content-type", "application/json");

                try
                {
                    //Envio y espero la peticion.
                    HttpResponse resp = httpClient.execute(get);
                    String respStr = EntityUtils.toString(resp.getEntity());
                    codigoServidor = resp.getStatusLine().getStatusCode();

                    result = new JSONObject(respStr);
                }
                catch(Exception ex)
                {
                    result = new JSONObject();
                }

                return result;
            }

            protected void onPostExecute(JSONObject result) {
                callback.ejecutar(result, codigoServidor);
            }
        }

        GET peticion = new GET();

        peticion.execute();
    }
}
