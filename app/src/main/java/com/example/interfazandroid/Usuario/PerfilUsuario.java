package com.example.interfazandroid.Usuario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.interfazandroid.EditarPerfilUsuario;
import com.example.interfazandroid.R;
import com.example.interfazandroid.EditarPerfilUsuario;
import com.example.interfazandroid.R;
import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.UserDetails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilUsuario extends AppCompatActivity {

    private static final int REQUEST_CODE_GALLERY = 100;
    private ImageView imageView;
    private Uri selectedImageUri;  // Variable para almacenar la URI seleccionada
    private String imageName = "selected_image.jpg";  // Nombre del archivo donde se guardará la imagen
    private TextView tvNombre, tvEmail, tvTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        tvNombre = findViewById(R.id.tvNombre);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);
        imageView = findViewById(R.id.imagenPerfil);

        fetchUserDetails();

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

    //////////MOSTRAR DATOS EN EL PERFIL/////////////////
    private void fetchUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token == null) {
            Log.e("PerfilUsuario", "Token no encontrado");
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("PerfilUsuario", "Token: " + token);

        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<UserDetails> call = apiService.getUserDetails("Bearer " + token);

        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                Log.d("PerfilUsuario", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    UserDetails userDetails = response.body();
                    if(userDetails != null && userDetails.getSub() != null){
                        UserDetails.Sub sub = userDetails.getSub();
                        Log.d("PerfilUsuario", "Nombre: " + sub.getNombre());
                        Log.d("PerfilUsuario", "Email: " + sub.getEmail());
                        Log.d("PerfilUsuario", "Teléfono: " + sub.getTelefono());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvNombre.setText(sub.getNombre() + " " + sub.getApellido());
                                tvEmail.setText(sub.getEmail());
                                tvTelefono.setText(sub.getTelefono());
                                Log.d("PerfilUsuario", "UI actualizada con los datos del usuario");
                            }
                        });
                    } else {
                        Log.e("PerfilUsuario", "UserDetails o Sub es null");
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("PerfilUsuario", "Error al obtener detalles del usuario. Código: " + response.code() + ", Cuerpo: " + errorBody);
                        Toast.makeText(PerfilUsuario.this, "Error al obtener detalles de usuario: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e){
                        Log.e("PerfilUsuario", "Error al leer el cuerpo del error", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e("PerfilUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(PerfilUsuario.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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