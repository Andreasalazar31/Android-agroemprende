package com.example.interfazandroid.Usuario;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.interfazandroid.MenuRegistro.MainActivity;
import com.example.interfazandroid.R;

public class NotificacionesUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notificaciones_usuario);
        //////////////////////////////////TOOLBAR///////////////////////////////////////////////////

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        ImageView icon3 = findViewById(R.id.icon3);

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega a la vista deseada para icon1
                Intent intent = new Intent(NotificacionesUsuario.this, PerfilUsuario.class);
                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega a la vista deseada para icon2
                Intent intent = new Intent(NotificacionesUsuario.this, MenuUsuario.class);
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega a la vista deseada para icon3
                Intent intent = new Intent(NotificacionesUsuario.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

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