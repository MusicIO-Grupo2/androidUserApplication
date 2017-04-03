package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.SharedServer;
import com.facebook.AccessToken;
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

import java.util.Random;


public class LoginFirstActivity extends AppCompatActivity {

    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;
    SharedServer sharedServer;
    EditText email;

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

        email = (EditText) findViewById(R.id.email);
        email.setText(sharedPref.getString("email",""));


        //Handle del boton.

        Button boton = (Button) findViewById(R.id.enviarEmail);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

                sharedPref.edit().putString("email", email.getText().toString()).commit();

                sharedServer.existeUsuarioEmail(email.getText().toString(), new VerificarEmail());
            }
        });

        manejarLoginFacebook();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void manejarLoginFacebook() {

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

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
                                    String email = object.getString("email");
                                    String nombre = object.getString("first_name");
                                    String apellido = object.getString("last_name");

                                    sharedPref.edit().putString("email", email).commit();
                                    sharedPref.edit().putString("nombre", nombre).commit();
                                    sharedPref.edit().putString("apellido", apellido).commit();

                                    sharedServer.existeUsuarioEmail(email, new VerificarEmail());

                                } catch (org.json.JSONException e) {
                                }
                            }
                        });

                Bundle parametros = new Bundle();
                parametros.putString("fields", "id, first_name, last_name, email, age_range");
                request.setParameters(parametros);
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

    public class VerificarEmail extends JSONCallback
    {
        public void ejecutar(JSONObject json, long codigoServidor)
        {
            if(codigoServidor == 200) {

                try {
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
                    sharedPref.edit().putString("email", json.getString("Email"));
                    sharedPref.edit().putString("nombre", json.getString("Name"));
                    sharedPref.edit().putString("apellido", json.getString("LastName"));
                    sharedPref.edit().putString("fechaNacimiento", json.getString("FechaNacimiento"));

                    sharedPref.edit().commit();
                }
                catch(JSONException e)
                {

                }

                irALoginSecond();
            }
            else {
                irARegistrar();
            }

        }
    }
}
