package com.example.interfazandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.modelLogin.ApiLogin;
import com.example.interfazandroid.modelLogin.ApiService;
import com.example.interfazandroid.modelLogin.Token;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    Button btnLogin;
    TextView Resgistrarse;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        Resgistrarse = findViewById(R.id.Registrarse);

        Resgistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    // GUARDAR EL TOKEN EN SharedPreferences
    private void saveToken(String token) {
        getSharedPreferences("MyApp", MODE_PRIVATE)
                .edit()
                .putString("UserToken", token)
                .apply();
    }

    // INICIAR LA ACTIVIDAD PRINCIPAL DE TU APLICACION
    private void startMainActivity() {
        Intent intent = new Intent(MainActivity.this, MenuUsuario.class);
        startActivity(intent);
        finish(); // Cierra la actividad de login
    }

    //METODO DE INICIAR SESION
    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //OCULTAR TECLADO
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //VALIDAR LOS CAMPOS QUE NO ESTEN VACIOS
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        //Crear una instancia del servidor Api
        ApiService apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        //Realizar el llamado de inicio de sesion
        Call<Token> call = apiService.getlogin(email, password);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    if (token != null && token.getToken() != null) {
                        Log.d(TAG, "Token recibido: " + token.getToken());
                        saveToken(token.getToken());
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
                        Toast.makeText(MainActivity.this, "Error en el login: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e(TAG, "Error parsing error body", e);
                        Toast.makeText(MainActivity.this, "Error en el login", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e(TAG, "Fallo en la conexión: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
