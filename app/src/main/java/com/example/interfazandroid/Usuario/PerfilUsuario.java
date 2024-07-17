package com.example.interfazandroid.Usuario;

import android.content.Context;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PerfilUsuario extends AppCompatActivity {


    private static final int REQUEST_CODE_GALLERY = 100;

    private ImageView imageView;
    private Uri selectedImageUri;  // Variable para almacenar la URI seleccionada
    private String imageName = "selected_image.jpg";  // Nombre del archivo donde se guardará la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        imageView = findViewById(R.id.imagenPerfil);

        // Restaurar la imagen seleccionada si está guardada
        selectedImageUri = loadImageFromStorage(imageName);
        if (selectedImageUri != null) {
            imageView.setImageURI(selectedImageUri);
        }

        // Agregar OnClickListener a la ImageView para abrir la galería
        imageView.setOnClickListener(v -> verificarPermisosGaleria());

        // Agregar OnClickListener a la ImageView 'editarusuario'
        ImageView editarUsuarioImageView = findViewById(R.id.editarusuario);
        editarUsuarioImageView.setOnClickListener(v -> {
            // Crear un Intent para navegar a otra actividad
            Intent intent = new Intent(PerfilUsuario.this, EditarPerfilUsuario.class);
            startActivity(intent);
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
                selectedImageUri = data.getData();
                // Establecer la imagen seleccionada en el ImageView
                imageView.setImageURI(selectedImageUri);

                // Guardar la imagen seleccionada en el almacenamiento interno
                saveImageToInternalStorage(selectedImageUri);
            }
        }
    }

    // Guardar la imagen seleccionada en el almacenamiento interno
    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            FileOutputStream outputStream = openFileOutput(imageName, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cargar la imagen seleccionada desde el almacenamiento interno
    private Uri loadImageFromStorage(String imageName) {
        try {
            File file = new File(getFilesDir(), imageName);
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}