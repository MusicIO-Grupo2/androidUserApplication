package com.example.mediaio.mediaio.Formularios;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.R;

/**
 * Created by Marcos on 05/04/2017.
 */

public class EditTextComun extends android.support.v7.widget.AppCompatEditText {

    boolean requerido;
    int longitudMaxima;

    public EditTextComun(Context context) {
        super(context);
        contructor();
    }

    public EditTextComun(Context context, AttributeSet attrs) {
        super(context, attrs);
        contructor(attrs);
    }

    public EditTextComun(Context context, AttributeSet attrs, int defStyle) {
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
        requerido = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto", "requerido", false);
        longitudMaxima = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "longitud", 0);
    }

    private boolean verificar()
    {
        boolean valido = true;
        boolean vacio = getText().toString().isEmpty();
        boolean matchea = getText().toString().matches("^[a-zA-Z]+$");

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

    //Permite obtener el valor escrito solo si es valido. Sino lanza una excepcion.
    //Si se especifico que es obligatorio no pude estar vacio.
    public String obtenerTexto() throws InputErronea
    {
        if(!verificar()) {
            if(getText().toString().isEmpty())
                setError("La entrada no puede estar vacia");
            else if(longitudMaxima != 0 && getText().toString().length() > longitudMaxima)
                setError("Entrada demasiado larga, debe tener un maximo de "+ Integer.toString(longitudMaxima)+" caracteres");
            else
                setError("Entrada no valida");
            throw new InputErronea();
        }
        else
            return getText().toString();
    }
}

