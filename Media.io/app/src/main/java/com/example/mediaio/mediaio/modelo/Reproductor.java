package com.example.mediaio.mediaio.modelo;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Marcos on 30/04/2017.
 */

public class Reproductor extends AsyncTask<String, Void, Boolean> {

    private MediaPlayer mediaPlayer;
    private String URL;
    private Boolean reproduciendo;
    private Boolean termino;

    public Reproductor(MediaPlayer mediaPlayer, String URL)
    {
        termino = true;
        reproduciendo = false;
        this.mediaPlayer = mediaPlayer;
        this.URL = URL;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Boolean ret = false;
        try {
            mediaPlayer.setDataSource(URL);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                Boolean ret = false;

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    termino = true;
                    reproduciendo = false;
                }
            });
            ret = true;
            mediaPlayer.prepare();
        } catch (Exception e) {
        }

        return ret;
    }

    public void pausar()
    {
        if(reproduciendo) {
            mediaPlayer.pause();
            reproduciendo = false;
        }
    }

    public  void reproducir()
    {
        if(!reproduciendo || termino)
            reproduciendo = true;
            mediaPlayer.start();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        mediaPlayer.start();
        reproduciendo = true;
    }
}
