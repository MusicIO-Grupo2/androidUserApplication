package com.example.mediaio.mediaio.Formularios;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;

import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.R;

/**
 * Created by Marcos on 05/04/2017.
 */

public class EditTextFecha extends android.support.v7.widget.AppCompatEditText {

    boolean requerido;

    public EditTextFecha(Context context) {
        super(context);
        contructor();
    }

    public EditTextFecha(Context context, AttributeSet attrs) {
        super(context, attrs);
        contructor(attrs);
    }

    public EditTextFecha(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        contructor(attrs);
    }

    private void contructor()
    {
        requerido = false;
        setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
    }

    private void contructor(AttributeSet attrs)
    {
        requerido = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto","requerido", false);
        setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
    }

    public boolean verificar()
    {
        boolean valido = true;
        boolean vacio = getText().toString().isEmpty();
        boolean matchea = getText().toString().matches("^[0-9]+(-[0-9]{2})$");

        if(requerido)
            if(vacio)
                valido = false;
            else
                valido = matchea;
        else if(!vacio)
            valido = matchea;

        return valido;
    };

    //Permite obtener el valor escrito solo si es valido como fecha. Sino lanza una excepcion.
    //Si se especifico que es obligatorio no pude estar vacio.
    public String obtenerTexto() throws InputErronea
    {
        if(!verificar()) {
            if(getText().toString().isEmpty())
                setError("Debe ingresar una fecha");
            else
                setError("Fecha no valida");
            throw new InputErronea();
        }
        else
            return getText().toString();
    }
}
