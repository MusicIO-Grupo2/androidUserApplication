package com.example.mediaio.mediaio.Formularios;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;

import com.example.mediaio.mediaio.Excepciones.InputErronea;
import com.example.mediaio.mediaio.R;

/**
 * Created by Marcos on 05/04/2017.
 */

public class EditTextPassword extends android.support.v7.widget.AppCompatEditText {

    int longitudMaxima;

    public EditTextPassword(Context context) {
        super(context);
        contructor();
    }

    public EditTextPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        contructor();
    }

    public EditTextPassword(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        contructor();
    }

    private void contructor()
    {
        longitudMaxima = 0;
        setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void contructor(AttributeSet attrs)
    {
        longitudMaxima = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "longitud",0);
        setTransformationMethod(PasswordTransformationMethod.getInstance());
    }


    public boolean verificar()
    {
        boolean matchea = getText().toString().matches("^[a-zA-Z0-9!#$%&'*+-/=?^_`{|}~\\@.]+$");

        if(longitudMaxima != 0 && getText().toString().length() > longitudMaxima)
            return false;

        return matchea;
    };

    //Permite obtener el valor escrito solo si es valido como contraseña. Sino lanza una excepcion.
    //Nunca puede ser vacio.
    public String obtenerTexto() throws InputErronea
    {
        if(!verificar()) {
            if(getText().toString().isEmpty())
                setError("Debe ingresar una contraseña");
            else if(longitudMaxima != 0 && getText().toString().length() > longitudMaxima)
                setError("Contraseña demasiado larga, debe tener un maximo de "+ Integer.toString(longitudMaxima)+" caracteres");
            else
                setError("Contraseña no valida");
            throw new InputErronea();
        }
        else
            return getText().toString();
    }
}
