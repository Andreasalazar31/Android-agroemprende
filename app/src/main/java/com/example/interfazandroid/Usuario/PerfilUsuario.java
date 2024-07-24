package com.example.interfazandroid.Usuario;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.example.interfazandroid.MenuRegistro.MainActivity;
import com.example.interfazandroid.R;
import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.UserDetails;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilUsuario extends AppCompatActivity {

    private ImageView imageView, editarusuario;
    private TextView tvNombre, tvEmail, tvTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        tvNombre = findViewById(R.id.tvNombre);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);
        imageView = findViewById(R.id.imagenPerfil);
        editarusuario= findViewById(R.id.editarusuario);

        editarusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilUsuario.this, EditarPerfilUsuario.class);
                startActivity(intent);
            }
        });
        Toolbar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchUserDetails();
        loadDataFromSharedPreferences();
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String userName = sharedPreferences.getString("UserName", "");
        String userEmail = sharedPreferences.getString("UserEmail", "");
        String userPhone = sharedPreferences.getString("UserPhone", "");

        tvNombre.setText(userName);
        tvEmail.setText(userEmail);
        tvTelefono.setText(userPhone);
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
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("UserName",sub.getNombre() + " "+ sub.getApellido());
                                editor.putString("UserEmail", sub.getEmail());
                                editor.putString("UserPhone", sub.getTelefono());
                                editor.apply();
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
    private void Toolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        ImageView icon3 = findViewById(R.id.icon3);

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUsuario.this, PerfilUsuario.class);
                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUsuario.this, MenuUsuario.class);
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUsuario.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}