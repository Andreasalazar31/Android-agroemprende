package com.example.interfazandroid.Usuario;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.R;

public class FormularioProyectoUsuario extends AppCompatActivity {


    private Button btnEnviarProyecto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_proyecto_usuario);

        btnEnviarProyecto = findViewById(R.id.btnEnviarProyecto);
        btnEnviarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes mostrar un mensaje o realizar cualquier acción
                Toast.makeText(FormularioProyectoUsuario.this, "¡Proyecto enviado correctamente!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}