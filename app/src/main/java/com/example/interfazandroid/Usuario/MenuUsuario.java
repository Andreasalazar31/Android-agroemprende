package com.example.interfazandroid.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.NotificacionesUsuario;
import com.example.interfazandroid.R;

public class MenuUsuario extends AppCompatActivity {
    private Button btnCrear;
    private ImageView icon1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_usuario);

        btnCrear = findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUsuario.this, FormularioProyectoUsuario.class);
                startActivity(intent);
            }
        });

        icon1 = findViewById(R.id.icon1);
        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de PerfilUsuario
                Intent intent = new Intent(MenuUsuario.this, PerfilUsuario.class);
                startActivity(intent);
            }
        });

        // Configuración del clic para abrir NotificacionesActivity
        ImageView iconNotificaciones = findViewById(R.id.iconNotificacion);
        iconNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUsuario.this, NotificacionesUsuario.class);
                startActivity(intent);
            }
        });
    }
}