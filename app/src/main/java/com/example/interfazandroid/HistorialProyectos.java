package com.example.interfazandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

public class HistorialProyectos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_proyectos);

        ExtendedFloatingActionButton btnNotificaciones = findViewById(R.id.agregarproyectos);

        btnNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistorialProyectos.this, UsuarioNuevoProyecto.class);
                startActivity(intent);
            }
        });
    }
}