package com.example.mediaio.mediaio.modelo;

import java.util.Date;

/**
 * Created by Marcos on 26/04/2017.
 */

/*Es un struct ya que no necesito ocultar informacion*/

public class MensajeFirebase {

    public String texto;
    public String usuario;
    public long timestamp;

    public MensajeFirebase(){};

    public MensajeFirebase(String texto, String usuario) {
        this.texto = texto;
        this.usuario = usuario;

        // Initialize to current time
       timestamp= new Date().getTime();
    }


}
