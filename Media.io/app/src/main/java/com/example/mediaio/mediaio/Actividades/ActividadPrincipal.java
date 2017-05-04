package com.example.mediaio.mediaio.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mediaio.mediaio.MainActivity;
import com.example.mediaio.mediaio.MediaIOPerfil;
import com.example.mediaio.mediaio.MediaIOPlay;
import com.example.mediaio.mediaio.R;

/**
 * Created by Marcos on 15/04/2017.
 */

public class ActividadPrincipal extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barraaplicacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:

                final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.datos), Context.MODE_PRIVATE);

                String email = sharedPref.getString("email","");
                sharedPref.edit().clear().commit();
                sharedPref.edit().putString("email",email);

                irASplash();
                return true;
            case R.id.menuPerfil:
                irAModificarPerfil();
                return true;
            case R.id.menuPlaylist:
                irAPlayList();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    void irASplash()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void irAModificarPerfil()
    {
        Intent intent = new Intent(this, MediaIOPerfil.class);
        startActivity(intent);
    }

    void irAPlayList()
    {
        Intent intent = new Intent(this, MediaIOPlay.class);
        startActivity(intent);
    }
}
