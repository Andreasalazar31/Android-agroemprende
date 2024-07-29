package com.example.interfazandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.interfazandroid.MenuRegistro.MainActivity;
import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.UserDetails;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioPerfil extends AppCompatActivity {

    private ImageView imageView, editarusuario;
    private ImageButton btnEditar;
    private TextView tvNombre, tvEmail, tvTelefono;
    private static final long CACHE_DURATION = 5 * 60 * 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        tvNombre = findViewById(R.id.tvNombre);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);

        btnEditar= findViewById(R.id.btnEditar);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioPerfil.this, UsuarioEditar.class);
                startActivity(intent);
            }
        });
        Toolbar();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUserInterface();

    }

    private  void updateUserInterface(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp",MODE_PRIVATE);
        long lasUpdateTime = sharedPreferences.getLong("LastUpdateTime",0);
        boolean shouldFetchFromApi = System.currentTimeMillis() - lasUpdateTime > CACHE_DURATION;

        if (shouldFetchFromApi) {
            fetchUserDetails();
        }else{
            loadDataFromSharedPreferences();
        }
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String userName = sharedPreferences.getString("UserName", "");
        String userEmail = sharedPreferences.getString("UserEmail", "");
        String userPhone = sharedPreferences.getString("UserPhone", "");

        updateUI(userName,userEmail,userPhone);
    }


    //////////MOSTRAR DATOS EN EL PERFIL/////////////////
    private void fetchUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token == null) {
            Log.e("PerfilUsuario", "Token no encontrado");
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            loadDataFromSharedPreferences();
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
                    if(userDetails != null && userDetails.getSub() != null) {
                        UserDetails.Sub sub = userDetails.getSub();
                        updateSharedPreferences(sub);
                        updateUI(sub.getNombre() + " " + sub.getApellido(), sub.getEmail(), sub.getTelefono());
                    } else {
                        Log.e("PerfilUsuario", "UserDetails o Sub es null");
                        loadDataFromSharedPreferences();
                    }
                } else {
                    handleApiError(response);
                    loadDataFromSharedPreferences();
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e("PerfilUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(UsuarioPerfil.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadDataFromSharedPreferences();
            }
        });
    }
    private void updateSharedPreferences(UserDetails.Sub sub) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", sub.getNombre() + " " + sub.getApellido());
        editor.putString("UserEmail", sub.getEmail());
        editor.putString("UserPhone", sub.getTelefono());
        editor.putLong("LastUpdateTime", System.currentTimeMillis());
        editor.apply();
    }

    private void updateUI(String name, String email, String phone) {
        tvNombre.setText(name);
        tvEmail.setText(email);
        tvTelefono.setText(phone);
    }
    private void handleApiError(Response<UserDetails> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("PerfilUsuario", "Error al obtener detalles del usuario. Código: " + response.code() + ", Cuerpo: " + errorBody);
            Toast.makeText(UsuarioPerfil.this, "Error al obtener detalles de usuario: " + response.code(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("PerfilUsuario", "Error al leer el cuerpo del error", e);
        }
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
                Intent intent = new Intent(UsuarioPerfil.this, UsuarioPerfil.class);
                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsuarioPerfil.this, UsuarioMenu.class);
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsuarioPerfil.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}