package com.example.interfazandroid.Usuario;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

import com.example.interfazandroid.EditarPerfilUsuario;
import com.example.interfazandroid.R;

public class PerfilUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_usuario);
        ImageView imageView = findViewById(R.id.editarusuario);

        // Agregar OnClickListener a la ImageView
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para navegar a otra actividad
                Intent intent = new Intent(PerfilUsuario.this, EditarPerfilUsuario.class);
                startActivity(intent);
            }
        });
    }
}