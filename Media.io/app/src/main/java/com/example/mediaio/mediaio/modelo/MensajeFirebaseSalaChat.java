package com.example.mediaio.mediaio.modelo;

import java.util.Date;

/**
 * Created by Marcos on 27/05/2017.
 */

/*Es un struct ya que no necesito ocultar informacion*/

public class MensajeFirebaseSalaChat {

    public String texto;
    public String usuario;
    public String usuarioAjeno;
    public String idAjeno;
    public long timestamp;

    public MensajeFirebaseSalaChat(){};

    public MensajeFirebaseSalaChat(String texto, String usuario, String usuarioAjeno, String idAjeno) {
        this.texto = texto;
        this.usuario = usuario;
        this.usuarioAjeno = usuarioAjeno;
        this.idAjeno = idAjeno;


        timestamp= new Date().getTime();
    }


}