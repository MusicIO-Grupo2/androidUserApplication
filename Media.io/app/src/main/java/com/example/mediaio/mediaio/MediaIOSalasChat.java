package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.MensajeFirebase;
import com.example.mediaio.mediaio.modelo.MensajeFirebaseSalaChat;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MediaIOSalasChat extends ActividadPrincipal{

    FirebaseListAdapter<MensajeFirebaseSalaChat> mensajes;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iosalas_chat);

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        //Inicializa el reproductor

        inicializarReproductor();

        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        mostrarChats();

    }

    private void mostrarChats() {
        ListView ListadoMensajes = (ListView) findViewById(R.id.ListadoChats);

        mensajes = new FirebaseListAdapter<MensajeFirebaseSalaChat>(this,
                MensajeFirebaseSalaChat.class,
                R.layout.medio_io_mensajessalachat,
                FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+sharedPref.getLong("id",0)+"/chats")) {

            @Override
            protected void populateView(View v, final MensajeFirebaseSalaChat model, int position) {

                TextView messageText = (TextView) v.findViewById(R.id.TextoMensaje);
                TextView messageUser = (TextView) v.findViewById(R.id.UsuarioMensaje);
                TextView messageTime = (TextView) v.findViewById(R.id.TiempoMensaje);
                ImageView fotoPerfil = (ImageView) v.findViewById(R.id.FotoPerfilUsuarioAjeno);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        irAChat(model.idAjeno,model.usuarioAjeno);
                    }
                });

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

    void irAChat(String id, String nombreUsuario)
    {
        Intent intent = new Intent(this, MediaIOChat.class);
        intent.putExtra("idUsuario",id);
        intent.putExtra("NombreUsuario", nombreUsuario);
        startActivity(intent);
    }
}

