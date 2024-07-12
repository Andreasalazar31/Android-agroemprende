package com.example.interfazandroid.Usuario;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.interfazandroid.EditarPerfilUsuario;
import com.example.interfazandroid.R;
import com.example.interfazandroid.EditarPerfilUsuario;
import com.example.interfazandroid.R;

public class PerfilUsuario extends AppCompatActivity {


    private static final int REQUEST_CODE_GALLERY = 100;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_usuario);

        imageView = findViewById(R.id.imagenPerfil);

        // Agregar OnClickListener a la ImageView para abrir la galería
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermisosGaleria();
            }
        });

        // Agregar OnClickListener a la ImageView 'editarusuario'
        ImageView editarUsuarioImageView = findViewById(R.id.editarusuario);
        editarUsuarioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para navegar a otra actividad
                Intent intent = new Intent(PerfilUsuario.this, EditarPerfilUsuario.class);
                startActivity(intent);
            }
        });
    }

    // Verificar y solicitar permisos para acceder a la galería
    private void verificarPermisosGaleria() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Si el permiso no está concedido, solicitarlo al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        } else {
            // Si el permiso está concedido, abrir la galería
            abrirGaleria();
        }
    }

    // Manejar la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abrir la galería
                abrirGaleria();
            } else {
                // Permiso denegado, mostrar un mensaje o tomar otra acción si lo deseas
                Toast.makeText(this, "Permiso denegado para acceder a la galería", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para abrir la galería y seleccionar una imagen
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    // Manejar el resultado de la selección de la imagen desde la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
            if (data != null) {
                Uri imagenSeleccionada = data.getData();

                // Aquí puedes implementar la lógica para guardar la imagen seleccionada
                // Por ahora, mostraremos un mensaje Toast con la URI de la imagen seleccionada
                Toast.makeText(this, "Imagen seleccionada: " + imagenSeleccionada.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}