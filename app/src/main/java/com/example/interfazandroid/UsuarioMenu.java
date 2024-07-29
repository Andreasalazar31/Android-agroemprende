package com.example.interfazandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.interfazandroid.MenuRegistro.MainActivity;

public class UsuarioMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_menu);
        Toolbar();

    }
    private void Toolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon3 = findViewById(R.id.icon3);

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsuarioMenu.this, UsuarioPerfil.class);
                startActivity(intent);
            }
        });

        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSharedPreferences();
                Intent intent = new Intent(UsuarioMenu.this, MainActivity.class);
                startActivity(intent);
                finish();
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