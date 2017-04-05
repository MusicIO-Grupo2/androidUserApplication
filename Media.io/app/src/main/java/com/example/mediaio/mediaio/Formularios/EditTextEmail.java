package com.example.mediaio.mediaio.Formularios;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.R;

/**
 * Created by Marcos on 05/04/2017.
 */

public class EditTextEmail extends android.support.v7.widget.AppCompatEditText {

    boolean requerido;
    int longitudMaxima;

    public EditTextEmail(Context context) {
        super(context);
        contructor();
    }

    public EditTextEmail(Context context, AttributeSet attrs) {
        super(context, attrs);
        contructor(attrs);
    }

    public EditTextEmail(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        contructor(attrs);
    }

    private void contructor()
    {
        requerido = false;
        longitudMaxima = 0;
    }

    private void contructor(AttributeSet attrs)
    {
        requerido = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto","requerido",false);
        longitudMaxima = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "longitud" ,0);
    }

    public boolean verificar()
    {
        boolean valido = true;
        boolean vacio = getText().toString().isEmpty();
        boolean matchea = getText().toString().matches("^[a-zA-Z0-9!#$%&'*+-/=?^_`{|}~\\.]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$");

        if(longitudMaxima != 0 && getText().toString().length() > longitudMaxima)
            return false;

        if(requerido)
            if(vacio)
                valido = false;
            else
                valido = matchea;
        else if(!vacio)
            valido = matchea;

        return valido;
    };

    //Permite obtener el valor escrito solo si es valido como email. Sino lanza una excepcion.
    //Si se especifico que es obligatorio no pude estar vacio.
    public String obtenerTexto() throws InputErronea
    {
        if(!verificar()) {
            if(getText().toString().isEmpty())
                setError("Debe ingresar un email");
            else if(longitudMaxima != 0 && getText().toString().length() > longitudMaxima)
                setError("Email demasiado largo, debe tener un maximo de "+ Integer.toString(longitudMaxima)+" caracteres");
            else
                setError("Email no valido");
            throw new InputErronea();
        }
        else
            return getText().toString();
    }
}
