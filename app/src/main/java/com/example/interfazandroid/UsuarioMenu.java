package com.example.interfazandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.interfazandroid.MenuRegistro.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UsuarioMenu extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_menu);

        ImageButton btnNotificaciones= findViewById(R.id.btnNotificaciones);
        btnNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsuarioMenu.this, UsuarioNotificaciones.class);
                startActivity(intent);
            }
        });


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Ya estamos en Home, no necesitamos hacer nada
                    return true;
                } else if (itemId == R.id.projects) {
                    startActivity(new Intent(UsuarioMenu.this, UsuarioNuevoProyecto.class));
                    return true;
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(UsuarioMenu.this, UsuarioPerfil.class));

                    return true;
                } else if (itemId == R.id.cerrarSesion) {
                    clearSharedPreferences();
                    startActivity(new Intent(UsuarioMenu.this, MainActivity.class));
                    // Implementa la lógica para cerrar sesión aquí
                    return true;
                }
                return false;
            }
        });
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Borra todas las preferencias
        editor.apply();
    }

}