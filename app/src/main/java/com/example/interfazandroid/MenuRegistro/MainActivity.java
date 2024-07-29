package com.example.interfazandroid.MenuRegistro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.R;
import com.example.interfazandroid.Registro;
import com.example.interfazandroid.UsuarioMenu;
import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.Token;
import com.example.interfazandroid.modelApi.UserDetails;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView registrarse;
    private ProgressBar progressBar;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        registrarse = findViewById(R.id.Registrarse);
        progressBar = findViewById(R.id.progressBar);

        registrarse.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> performLogin());
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserToken", token);
        editor.apply();
    }

    private void saveUserId(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserId", userId);
        editor.apply();
        Log.d("GuardarIdUsuario", "ID del usuario guardado: " + userId);
    }

    private void startMainActivity() {
        Intent intent = new Intent(MainActivity.this, UsuarioMenu.class);
        startActivity(intent);
        finish(); // Cierra la actividad de login
    }

    private void obtenerDetallesUsuario(String token) {
        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);

        Call<UserDetails> call = apiService.getUserDetails("Bearer " + token);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful()) {
                    UserDetails userDetails = response.body();
                    if (userDetails != null && userDetails.getSub() != null) {
                        String userId = userDetails.getSub().get_id();
                        saveUserId(userId); // Guardar ID en SharedPreferences
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e(TAG, "Fallo en la llamada a la API para obtener detalles del usuario", t);
            }
        });
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Ocultar teclado
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Validar campos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Crear una instancia del servicio API
        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);

        // Realizar el llamado de inicio de sesión
        Call<Token> call = apiService.getlogin(email, password);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                // Ocultar ProgressBar
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Token token = response.body();
                    if (token != null && token.getToken() != null) {
                        Log.d(TAG, "Token recibido: " + token.getToken());
                        saveToken(token.getToken());

                        // Obtener detalles del usuario usando el token
                        obtenerDetallesUsuario(token.getToken());

                        Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        startMainActivity();
                    } else {
                        Log.e(TAG, "Error: Token inválido o nulo");
                        Toast.makeText(MainActivity.this, "Error: Token inválido o nulo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Error en el login: " + errorBody);
                        if (response.code() == 404) {
                            Toast.makeText(MainActivity.this, "Contraseña incorrecta. Intentelo de nuevo", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Error en el login: " + errorBody, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error al analizar el cuerpo del error", e);
                        Toast.makeText(MainActivity.this, "Error en el login", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                // Ocultar ProgressBar
                progressBar.setVisibility(View.GONE);

                Log.e(TAG, "Fallo en la conexión: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
