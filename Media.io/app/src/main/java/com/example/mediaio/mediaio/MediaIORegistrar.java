package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.Formularios.EditTextComun;
import com.example.mediaio.mediaio.Formularios.EditTextEmail;
import com.example.mediaio.mediaio.Formularios.EditTextFecha;
import com.example.mediaio.mediaio.Formularios.EditTextPassword;
import com.example.mediaio.mediaio.modelo.JSONCallback;
import com.example.mediaio.mediaio.modelo.SharedServer;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;

public class MediaIORegistrar extends AppCompatActivity {

    final int OBTENER_IMAGEN = 0;
    SharedServer sharedServer;
    String imagenEnBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioregistrar);
        ponerFondo();

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();
        imagenEnBase64 = new String();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        final EditTextComun nombre = (EditTextComun) findViewById(R.id.registroTextoNombre);
        final EditTextComun apellido = (EditTextComun) findViewById(R.id.registroTextoApellido);
        final EditTextComun nombreUsuario = (EditTextComun) findViewById(R.id.registroTextoNombreUsuario);
        final EditTextComun pais = (EditTextComun) findViewById(R.id.registroTextoPais);
        final EditTextEmail email = (EditTextEmail) findViewById(R.id.registroTextoEmail);
        final EditTextFecha fechaNacimiento = (EditTextFecha) findViewById(R.id.registroTextoFechaNacimiento);
        final EditTextPassword contrasena = (EditTextPassword) findViewById(R.id.registroTextoContrasena);
        final TextView error = (TextView) findViewById(R.id.registroTextoError);
        final Button registrarse = (Button) findViewById(R.id.registroBotonRegistrar);

        final ImageView avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerImagenGalleria();
            }
        });


        final String imagenBase64 = sharedPref.getString("avatar", "");
        byte[] imagenArreglo;
        if (imagenBase64 != "") {
            imagenArreglo = Base64.decode(imagenBase64, Base64.DEFAULT);
            imagenEnBase64 = imagenBase64;
            avatar.setImageBitmap(BitmapFactory.decodeByteArray(imagenArreglo, 0, imagenArreglo.length));
        }

        //Si se esta logueando con Facebook se tendran estos datos y se llenan los campos.
        nombre.setText(sharedPref.getString("nombre", ""));
        apellido.setText(sharedPref.getString("apellido", ""));
        email.setText(sharedPref.getString("email", ""));
        fechaNacimiento.setText(sharedPref.getString("fechaNacimiento", ""));

        //Callback que recibe la confirmacion de alta.
        class DarAltaCallback extends JSONCallback {

            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                Log.e("Mensaje",respuesta+" : " + codigoServidor);
                if (codigoServidor == 200) {
                    try {
                        sharedPref.edit().putString("email", email.obtenerTexto());
                    }
                    catch (InputErronea e)
                    {

                    }
                        irALoginSecond();
                } else
                    error.setText("Hubo un error. Revise los datos ingresados y vuelva a intentarlo.");

                //Se reactiva el boton para poder reenviar los datos.
                registrarse.setEnabled(true);

            }
        } ;

        //Handle del boton
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Desactiva el boton para no admitir repeticiones.
                    sharedServer.darAltaUsuario(nombre.obtenerTexto(), apellido.obtenerTexto(), email.obtenerTexto(), fechaNacimiento.obtenerTexto(), contrasena.obtenerTexto(), pais.obtenerTexto(), imagenEnBase64, nombreUsuario.obtenerTexto(), new DarAltaCallback());
                    registrarse.setEnabled(false);
                } catch (InputErronea e) {

                }
            }
        });
    }


    void irALoginSecond() {
        Intent intent = new Intent(this, LoginSecondActivity.class);
        startActivity(intent);
    }

    //El fondo se mantiene entre las diferentes pantallas.
    void ponerFondo() {
        LinearLayout fondo = (LinearLayout) findViewById(R.id.ventana);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);
        fondo.setBackgroundResource(sharedPref.getInt("idFondo", R.drawable.background1));
    }

    String obtenerImagenEnBase64(Bitmap imagen) {
        String imagenBase64 = new String();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        imagenBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return imagenBase64;
    }

    void obtenerImagenGalleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), OBTENER_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OBTENER_IMAGEN) {

            if (resultCode == RESULT_OK) {
                Uri UriSeleccionada = data.getData();
                try {
                    Bitmap imagen = MediaStore.Images.Media.getBitmap(this.getContentResolver(), UriSeleccionada);
                    imagenEnBase64 = obtenerImagenEnBase64(imagen);

                    ImageView avatar = (ImageView) findViewById(R.id.avatar);
                    avatar.setImageBitmap(imagen);

                }
                catch (IOException e)
                {

                }
            }
        }
    }
}

