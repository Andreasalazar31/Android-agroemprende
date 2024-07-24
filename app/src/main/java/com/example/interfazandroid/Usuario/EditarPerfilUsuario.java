package com.example.interfazandroid.Usuario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;

import com.example.interfazandroid.MenuRegistro.MainActivity;
import com.example.interfazandroid.R;
import com.example.interfazandroid.databinding.ActivityEditarPerfilUsuarioBinding;
import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.UserDetails;
import com.example.interfazandroid.modelApi.UserUpdate;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilUsuario extends AppCompatActivity {

    private TextInputEditText edtNombre, edtApellido,edtEmail, edtNumIdentificacion,edtTelefono,edtNacimiento,edtCaracterizacion;
    private Button btnguardardatos,btnEditEmail;
    private ApiService apiService;
    private boolean isEmailEditable= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_perfil_usuario);

        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtEmail = findViewById(R.id.edtEmail);
        edtNumIdentificacion = findViewById(R.id.edtNumIdentificacion);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtNacimiento = findViewById(R.id.edtNacimiento);
        edtCaracterizacion = findViewById(R.id.edtCaracterizacion);
        btnguardardatos = findViewById(R.id.btnguardardatos);
        btnEditEmail = findViewById(R.id.btnEditarEmail);

        Toolbar();
        /////BOTON EDITAR SOLO CORREO HABILITAR/////////////
        btnEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmailEditable = !isEmailEditable;
                edtEmail.setEnabled(isEmailEditable);
                btnEditEmail.setText(isEmailEditable ? "editar":"ocultar");
            }
        });
        btnguardardatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatosUsuario();
            }
        });

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }
    @Override
    protected void onResume(){
        super.onResume();
        MostrarDatos();
    }

    private void MostrarDatos() {
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
                    if (userDetails != null && userDetails.getSub() != null) {
                        UserDetails.Sub sub = userDetails.getSub();

                        // Guardar el ID del usuario en SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserId", sub.get_id()); // Asegúrate de que `getUserId()` es el método correcto
                        editor.apply();

                        // Mostrar un Toast confirmando que el ID está guardado
                        Toast.makeText(EditarPerfilUsuario.this, "ID del usuario guardado", Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edtNombre.setText(sub.getNombre());
                                edtApellido.setText(sub.getApellido());
                                edtEmail.setText(sub.getEmail());
                                edtNumIdentificacion.setText(sub.getNumIdentificacion());
                                edtTelefono.setText(sub.getTelefono());
                                edtNacimiento.setText(sub.getFechaNacimieto());
                                edtCaracterizacion.setText(sub.getCaracterizacion());
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
                        if (response.code() == 503) {
                            Toast.makeText(EditarPerfilUsuario.this, "Servidor no disponible. Inténtalo más tarde.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditarPerfilUsuario.this, "Error al obtener detalles de usuario: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e("PerfilUsuario", "Error al leer el cuerpo del error", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e("PerfilUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(EditarPerfilUsuario.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarDatosUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);
        String userId = sharedPreferences.getString("UserId", null);

        if (token == null) {
            Log.e("EditarPerfilUsuario", "Token no encontrado");
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == null) {
            Log.e("EditarPerfilUsuario", "UserId no encontrado");
            Toast.makeText(this, "UserId no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        UserUpdate userUpdate = new UserUpdate();
        String nombre = edtNombre.getText().toString();
        String apellido = edtApellido.getText().toString();
        String numIdentificacion = edtNumIdentificacion.getText().toString();
        String telefono = edtTelefono.getText().toString();
        String fechaNacimiento = edtNacimiento.getText().toString();
        String caracterizacion = edtCaracterizacion.getText().toString();

        userUpdate.setNombre(nombre);
        userUpdate.setApellido(apellido);
        userUpdate.setNumIdentificacion(numIdentificacion);
        userUpdate.setTelefono(telefono);
        userUpdate.setFechaNacimiento(fechaNacimiento);
        userUpdate.setCaracterizacion(caracterizacion);

        if (isEmailEditable) {
            userUpdate.setEmail(edtEmail.getText().toString());
        }

        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.updateUserProfile(userId, "Bearer " + token, userUpdate);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarPerfilUsuario.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserName", nombre + " " + apellido);
                    editor.putString("UserEmail", isEmailEditable ? edtEmail.getText().toString() : edtEmail.getText().toString());
                    editor.putString("UserPhone", telefono);
                    editor.apply();

                    // Navegar de vuelta a PerfilUsuario
                    Intent intent = new Intent(EditarPerfilUsuario.this, PerfilUsuario.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("EditarPerfilUsuario", "Error al actualizar datos del usuario. Código: " + response.code() + ", Cuerpo: " + errorBody);
                        Toast.makeText(EditarPerfilUsuario.this, "Error al actualizar datos: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("EditarPerfilUsuario", "Error al leer el cuerpo del error", e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EditarPerfilUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(EditarPerfilUsuario.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(EditarPerfilUsuario.this, PerfilUsuario.class);
                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfilUsuario.this, MenuUsuario.class);
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfilUsuario.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}


