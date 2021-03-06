package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.Formularios.EditTextEmail;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.SharedServer;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;

import static android.util.Base64.DEFAULT;


public class LoginFirstActivity extends AppCompatActivity {

    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;
    SharedServer sharedServer;
    EditTextEmail email;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedServer = new SharedServer();

        //Si esta logeado va directo a la pagina principal

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
        if(sharedPref.getString("token","0") != "0")
            irAMain();

        //Inicializa el sdk de facebook.
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        setContentView(R.layout.activity_login_first);
        ponerFondo();

        //Setea el comportamiento del boton.

        email = (EditTextEmail) findViewById(R.id.email);
        email.setText(sharedPref.getString("email",""));


        //Handle del boton para ingresar usando el email.

        boton = (Button) findViewById(R.id.enviarEmail);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                String texto = email.obtenerTexto();

                //Solo si el email es valido
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

                    //Guarda el email para futuros usos.
                    sharedPref.edit().putString("email", texto).commit();

                    sharedServer.existeUsuarioEmail(texto, new VerificarEmail());
                    boton.setEnabled(false);
                }
                catch(InputErronea e)
                {
                }
            }
        });

        //Integra el boton que usa la API de Facebook Login
        manejarLoginFacebook();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void manejarLoginFacebook() {

        loginButton = (LoginButton) findViewById(R.id.login_button);

        //Configura los permisos de acceso a informacion que requiere Facebook.
        loginButton.setReadPermissions("email", "public_profile");

        //Crea el Callback en el que la API devuelve el resultado de la consulta.
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
                                try {

                                    //Obtiene la informacion del usuario.
                                    String email = object.getString("email");
                                    String nombre = object.getString("first_name");
                                    String apellido = object.getString("last_name");
                                    String direccionImagen = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    //La almacena.
                                    sharedPref.edit().putString("email", email).commit();
                                    sharedPref.edit().putString("nombre", nombre).commit();
                                    sharedPref.edit().putString("apellido", apellido).commit();
                                    sharedPref.edit().putString("avatar", obtenerImagenEnBase64(direccionImagen)).commit();

                                    sharedServer.existeUsuarioEmail(email, new VerificarEmail());

                                } catch (org.json.JSONException e) {
                                }
                            }
                        });

                //A la API de Facebook hay que pedirle explicitamente la informacion que debe devolover la GraphRequest
                Bundle parametros = new Bundle();
                parametros.putString("fields", "id, first_name, last_name, email, age_range, picture");
                request.setParameters(parametros);

                //Se ejecuta de forma sincronica.
                request.executeAndWait();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    String obtenerImagenEnBase64(String direccion)
    {
        String imagenBase64 = new String();
        try {
            URL url = new URL(direccion);

            Bitmap imagen = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            imagenBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch(MalformedURLException e)
        {
        }
        catch(IOException e)
        {
        }

        return imagenBase64;
    }

    void ponerFondo()
    {
        LinearLayout fondo = (LinearLayout) findViewById(R.id.ventanaLoginFirst);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
        fondo.setBackgroundResource(sharedPref.getInt("idFondo", R.drawable.background1));
    }

    void irAMain()
    {
        Intent intent = new Intent(this, MediaIOMain.class);
        startActivity(intent);
    }

    void irALoginSecond()
    {
        Intent intent = new Intent(this, LoginSecondActivity.class);
        startActivity(intent);
    }

    void irARegistrar()
    {
        Intent intent = new Intent(this, MediaIORegistrar.class);
        startActivity(intent);
    }

    //Clase que implementa la interfaz JSONCallback
    public class VerificarEmail extends JSONCallback
    {
        public void ejecutar(JSONObject json, long codigoServidor)
        {
            boton.setEnabled(true);
            if(codigoServidor == 200) {
                irALoginSecond();
            }
            else if(codigoServidor == 401) {
                //No esta registrado.
                irARegistrar();
            }

        }
    }
}
