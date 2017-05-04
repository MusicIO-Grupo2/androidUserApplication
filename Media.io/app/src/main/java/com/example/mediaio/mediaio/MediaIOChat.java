package com.example.mediaio.mediaio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mediaio.mediaio.modelo.MensajeFirebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MediaIOChat extends AppCompatActivity {

    FirebaseListAdapter<MensajeFirebase> mensajes;
    SharedPreferences sharedPref;
    long idMenor;
    long idMayor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_iochat);


        sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

        idMenor = getIntent().getExtras().getInt("idUsuarioDestino");
        idMayor =  sharedPref.getLong("id",1);

        if(idMenor > idMayor)
        {
            long aux = idMenor;
            idMenor = idMayor;
            idMayor = aux;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.IngresoMensaje);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);

                // Agrega el mensaje a firebase en la conversacion entre esos dos usuarios

                DatabaseReference baseDeDatos = FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+ idMenor+"-"+idMayor);
                baseDeDatos.push().setValue(new MensajeFirebase(input.getText().toString(), sharedPref.getString("nombre", "NULL") + " " + sharedPref.getString("apellido", "NULL")));

                // Clear the input
                input.setText("");
            }
        });

        mostrarChat();
    }


    private void mostrarChat() {
        ListView ListadoMensajes = (ListView) findViewById(R.id.ListadoMensajes);

        mensajes = new FirebaseListAdapter<MensajeFirebase>(this,
                                                            MensajeFirebase.class,
                                                            R.layout.medio_io_mensajes,
                                                            FirebaseDatabase.getInstance().getReferenceFromUrl("https://musicio-9a709.firebaseio.com/"+ idMenor+"-"+idMayor)) {

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
