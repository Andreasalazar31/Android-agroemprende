package com.example.interfazandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.Proyecto;
import com.example.interfazandroid.Proyecto.ProyectosAdapter;
import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.UserDetails;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HistorialProyectos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProyectosAdapter adapter;
    private ApiService apiService;
    private static final long CACHE_DURATION = 5 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_proyectos);

        recyclerView = findViewById(R.id.recyclerViewProyectos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token != null) {
            loadProyectosFromProfile("Bearer " + token);
        } else {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
        }
        ExtendedFloatingActionButton agregarProyectos = findViewById(R.id.agregarproyectos);
        agregarProyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistorialProyectos.this, UsuarioNuevoProyecto.class);
                startActivity(intent);
            }
        });
    }


    private void loadProyectosFromProfile(String token) {
        apiService.getUserDetails(token).enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDetails userDetails = response.body();
                    if (userDetails.getSub() != null && userDetails.getSub().getProyectos() != null) {
                        List<Proyecto> proyectos = userDetails.getSub().getProyectos();
                        adapter = new ProyectosAdapter(proyectos);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(HistorialProyectos.this, "No se encontraron proyectos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HistorialProyectos.this, "Error al cargar el perfil: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(HistorialProyectos.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
