package com.example.interfazandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.ProyectRegister;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsuarioNuevoProyecto extends AppCompatActivity {

    private EditText etTitulo, etFecha,etDescripcion;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_nuevo_proyecto);

        etTitulo= findViewById(R.id.Titulo);
        etFecha= findViewById(R.id.Fecha);
        etDescripcion= findViewById(R.id.Descripcion);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerProyect();
                Intent intent = new Intent(UsuarioNuevoProyecto.this,HistorialProyectos.class);
                startActivity(intent);
            }
        });
    }

    ///METODO DE REGISTRAR PROYECTO
    private void registerProyect(){
        String titulo = etTitulo.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(fecha) || TextUtils.isEmpty(descripcion)) {
            Toast.makeText(this, "Por favor, complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences  sharedPreferences = getSharedPreferences("MyApp",MODE_PRIVATE);
        String usuarioId = sharedPreferences.getString("UserId","");
        String token = sharedPreferences.getString("UserToken","");

        if (TextUtils.isEmpty(usuarioId)) {
            Toast.makeText(this, "Usuario no identificado. Inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProyectRegister proyectRegister = new ProyectRegister(titulo,fecha,"En progreso",descripcion,usuarioId);
        ApiLogin apiLogin = new ApiLogin();
        Retrofit retrofit = apiLogin.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Void> call = apiService.registerProyecto("Bearer " + token, proyectRegister);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(UsuarioNuevoProyecto.this,"Proyecto registrado con exito",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(UsuarioNuevoProyecto.this,"Error al registrar el Proyecto",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UsuarioNuevoProyecto.this,"Fallo de Conexion" +t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


}
