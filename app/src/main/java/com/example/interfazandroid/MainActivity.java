package com.example.interfazandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.modelLogin.ApiLogin;
import com.example.interfazandroid.modelLogin.ApiService;
import com.example.interfazandroid.modelLogin.Token;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    Button btnLogin, btnPrueba;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnPrueba = findViewById(R.id.btnPrueba);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPruebaClickend();
            }
        });
    }
    private void saveToken(String token) {
        // Guardar el token en SharedPreferences
        getSharedPreferences("MyApp", MODE_PRIVATE)
                .edit()
                .putString("UserToken", token)
                .apply();
    }

    private void startMainActivity() {
        // Iniciar la actividad principal de tu aplicación
        Intent intent = new Intent(MainActivity.this, Menu.class);
        startActivity(intent);
        finish(); // Cierra la actividad de login
    }

    /////
    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<Token> call = apiService.getlogin(email, password);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    if (token != null && token.getToken() != null) {
                        // Guardar el token para uso futuro
                        saveToken(token.getToken());
                        // Iniciar la siguiente actividad
                        startMainActivity();
                    } else {
                        Toast.makeText(MainActivity.this, "Error: Token inválido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(MainActivity.this, "Error en el login: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, "Error en el login", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


////////mirar token
    private void btnPruebaClickend() {
        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);

        Call<Token> call = apiService.getlogin("kevinA@gmail.com", "1234");

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    if (token != null) {
                        Log.d(TAG, "onResponse: Token: " + token.getToken());
                        Toast.makeText(MainActivity.this, "Login exitoso: " + token.getToken(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "onResponse: Token object is null");
                        Toast.makeText(MainActivity.this, "Error: Token object is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "onResponse: Error body: " + errorBody);
                        Toast.makeText(MainActivity.this, "Error en la solicitud: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: Error parsing error body", e);
                        Toast.makeText(MainActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Fallo en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}





/*



        btnLogin.setOnClickListener(this::onLoginClick);
    }

    public void onLoginClick(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        login(email, password);
    }

    private void login(String email, String contrasena) {
        ApiService apiService = ApiLogin.getRetrofitInstance().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, contrasena);

        // Log del cuerpo de la solicitud
        Gson gson = new Gson();
        String requestBody = gson.toJson(loginRequest);
        Log.d("LoginActivity", "Request body: " + requestBody);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getToken() != null) {
                        Toast.makeText(MainActivity.this, "Login exitoso: " + loginResponse.getToken(), Toast.LENGTH_SHORT).show();
                        // Aquí puedes manejar el token, guardarlo en SharedPreferences, etc.
                    } else {
                        Toast.makeText(MainActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("LoginActivity", "Error body: " + errorBody);
                        Toast.makeText(MainActivity.this, "Error en la solicitud: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("LoginActivity", "Error parsing error body", e);
                        Toast.makeText(MainActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "onFailure: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Fallo en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}*/