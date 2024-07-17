package com.example.interfazandroid;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NotificacionesUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notificaciones_usuario);

        Button btn1 = findViewById(R.id.notification_button);
        Button btn2 = findViewById(R.id.notification_butto);
        Button btn3 = findViewById(R.id.notification_butt);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirActividadDetalles("Detalles del primer proyecto");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirActividadDetalles("Detalles del segundo proyecto");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirActividadDetalles("Detalles del tercer proyecto");
            }
        });
    }

    private void abrirActividadDetalles(String mensaje) {
        // Aquí configuras la actividad a la que quieres navegar
        Intent intent = new Intent(NotificacionesUsuario.this, DetallesProyecto.class);
        intent.putExtra("mensaje", mensaje); // Puedes enviar datos extras si es necesario
        startActivity(intent);
    }
}