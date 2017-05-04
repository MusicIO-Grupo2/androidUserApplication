package com.example.mediaio.mediaio;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.mediaio.mediaio.Actividades.ActividadPrincipal;
import com.example.mediaio.mediaio.modelo.Reproductor;

public class MediaIOPlay extends ActividadPrincipal {
    private Reproductor reproductor;
    private Boolean mostrandoImagenPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_ioplay);
        mostrandoImagenPlay = false;

        //Inicializa la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.BarraMenu);
        setSupportActionBar(myToolbar);

        String URLTema = /*getIntent().getExtras().getString("URLTema")*/"http://www.certifiedpowercoach.com/1.mp3";
        MediaPlayer mediaPlayer = new MediaPlayer();

        reproductor = new Reproductor(mediaPlayer, URLTema);
        reproductor.execute();

        final ImageView play = (ImageView) findViewById(R.id.BotonReproducir);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mostrandoImagenPlay)
                {
                    play.setImageResource(R.drawable.boton_reproductor_play);
                    mostrandoImagenPlay = false;
                    reproductor.pausar();
                }
                else
                {
                    play.setImageResource(R.drawable.noton_reproductor_pausa);
                    mostrandoImagenPlay = true;
                    reproductor.reproducir();
                }

            }
        });
    }
}
