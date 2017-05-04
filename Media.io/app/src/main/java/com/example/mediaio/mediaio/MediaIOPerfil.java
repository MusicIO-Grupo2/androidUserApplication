package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
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

public class MediaIOPerfil extends ActividadPrincipal {

    final int OBTENER_IMAGEN = 0;
    SharedServer sharedServer;
    String imagenEnBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioperfil);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Crea la interfaz con el Shared Server.
        sharedServer = new SharedServer();

        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        sharedServer.configurarTokenEID(sharedPref.getString("token",""),sharedPref.getLong("id",0));


        final EditTextComun nombreUsuario = (EditTextComun) findViewById(R.id.perfilTextoNombreUsuario);
        final EditTextComun nombre = (EditTextComun) findViewById(R.id.perfilTextoNombre);
        final EditTextComun apellido = (EditTextComun) findViewById(R.id.perfilTextoApellido);
        final EditTextEmail email = (EditTextEmail) findViewById(R.id.perfilTextoEmail);
        final EditTextFecha fechaNacimiento = (EditTextFecha) findViewById(R.id.perfilTextoFechaNacimiento);
        final EditTextComun pais = (EditTextComun) findViewById(R.id.perfilTextoPais);
        final EditTextPassword contrasena = (EditTextPassword) findViewById(R.id.perfilTextoContrasena);
        final TextView error = (TextView) findViewById(R.id.perfilTextoError);
        final Button guardarCambios = (Button) findViewById(R.id.perfilBotonGuardarCambios);

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



        //Se rellenan los campos
        nombreUsuario.setText(sharedPref.getString("nombreUsuario",""));
        nombre.setText(sharedPref.getString("nombre",""));
        apellido.setText(sharedPref.getString("apellido",""));
        email.setText(sharedPref.getString("email",""));
        fechaNacimiento.setText(sharedPref.getString("fechaNacimiento",""));
        pais.setText(sharedPref.getString("pais",""));
        contrasena.setText(sharedPref.getString("contrasena",""));

        //Callback que recibe la confirmacion de modificacion.
        class ModificarPerfilCallback extends JSONCallback
        {

            @Override
            public void ejecutar(JSONObject respuesta, long codigoServidor) {
                Log.e("Mensaje",respuesta.toString()+" " + Long.toString(codigoServidor));
                if(codigoServidor == 200)
                {
                    try {
                        //Si el server lo aprueba se guardan los cambios.

                        sharedPref.edit().putString("nombre", nombre.obtenerTexto()).commit();
                        sharedPref.edit().putString("apellido", apellido.obtenerTexto()).commit();
                        sharedPref.edit().putString("email", email.obtenerTexto()).commit();
                        sharedPref.edit().putString("fechaNacimiento", fechaNacimiento.obtenerTexto()).commit();
                        sharedPref.edit().putString("contrasena", contrasena.obtenerTexto()).commit();

                        error.setText("¡Perfil modificado exitosamente!");
                    }
                    catch(InputErronea e)
                    {
                        error.setText("Hubo un error. Revise los datos ingresados y vuelva a intentarlo.");
                    }
                }
                else
                    error.setText("Hubo un error. Revise los datos ingresados y vuelva a intentarlo.");

                //Se reactiva el boton para poder reenviar los datos.
                guardarCambios.setEnabled(true);

            }
        };

        //Handle del boton
        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Desactiva el boton para no admitir repeticiones.
                    sharedServer.ModificarPerfilUsuario(nombre.obtenerTexto(), apellido.obtenerTexto(), email.obtenerTexto(), fechaNacimiento.obtenerTexto(), contrasena.obtenerTexto(), pais.obtenerTexto(), imagenEnBase64, nombreUsuario.obtenerTexto(),  new ModificarPerfilCallback());
                    guardarCambios.setEnabled(false);
                }
                catch(InputErronea e)
                {

                }
            }
        });
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
