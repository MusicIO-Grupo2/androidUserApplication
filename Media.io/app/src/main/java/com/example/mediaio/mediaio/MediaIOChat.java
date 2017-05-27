package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.MensajeFirebase;
import com.example.mediaio.mediaio.modelo.MensajeFirebaseSalaChat;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MediaIOChat extends ActividadPrincipal {
    private String idUsuario;
    private String nombreDestino;

    FirebaseListAdapter<MensajeFirebase> mensajes;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iochat);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        idUsuario = getIntent().getStringExtra("idUsuario");
        nombreDestino = getIntent().getStringExtra("NombreUsuario");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.IngresoMensaje);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                ponerMensajeEnFirebase(input.getText().toString(),idUsuario, Long.toString(sharedPref.getLong("id",0)),sharedPref.getString("NombreUsuario","ANONIMO"),nombreDestino);
                input.setText("");
            }
        });

        mostrarChat();
    }

    private void ponerMensajeEnFirebase(String mensaje, String idDestino, String idAutor, String firmaAutor, String firmaDestino)
    {
        DatabaseReference baseDeDatos = FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+idDestino+"/mensajes/"+idAutor);
        baseDeDatos.push().setValue(new MensajeFirebase(mensaje, firmaAutor));

        baseDeDatos = FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+idDestino+"/chats/"+idAutor);
        baseDeDatos.setValue(new MensajeFirebaseSalaChat(mensaje, firmaAutor, firmaAutor, idAutor));

        baseDeDatos = FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+idAutor+"/mensajes/"+idDestino);
        baseDeDatos.push().setValue(new MensajeFirebase(mensaje, firmaAutor));

        baseDeDatos = FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+idAutor+"/chats/"+idDestino);
        baseDeDatos.setValue(new MensajeFirebaseSalaChat(mensaje, firmaAutor, firmaDestino, idDestino));
    }


    private void mostrarChat() {
        ListView ListadoMensajes = (ListView) findViewById(R.id.ListadoMensajes);

        mensajes = new FirebaseListAdapter<MensajeFirebase>(this,
                                                            MensajeFirebase.class,
                                                            R.layout.medio_io_mensajes,
                                                            FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+sharedPref.getLong("id",0)+"/mensajes/"+idUsuario)) {

            @Override
            protected void populateView(View v, MensajeFirebase model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.TextoMensaje);
                TextView messageUser = (TextView) v.findViewById(R.id.UsuarioMensaje);
                TextView messageTime = (TextView) v.findViewById(R.id.TiempoMensaje);

                // Set their text
                messageText.setText(model.texto);
                messageUser.setText(model.usuario);

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-mm-yyyy (hh:mm:ss)",
                        model.timestamp));
            }
        };

        ListadoMensajes.setAdapter(mensajes);
    }
}
