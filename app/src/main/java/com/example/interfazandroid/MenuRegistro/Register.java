package com.example.interfazandroid.MenuRegistro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;

import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.R;
import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.RegisterRequest;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Register extends AppCompatActivity {
    private EditText etNombre,etApellido,etEmail,etPassword, etConfirmPassword;
    private TextInputLayout tilConfirmPassword;
    private boolean passwordVisible = false;
    private TextView Sesion;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etCorreoElectronico);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPasswordd);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        Sesion = findViewById(R.id.Sesion);
        progressBar=findViewById(R.id.progressBar);

        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        //Configurar los listeners para altenar la visibilidad de la contraseña
        setupPasswordVisibilityToggle(etPassword);
        setupPasswordVisibilityToggle(etConfirmPassword);

        //Configurar el boton para cambiar a la actividad de inicio de sesion
        Sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    ////METODO DE REGISTRARSE
    private void registerUser(){
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contrasena = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(email) || TextUtils.isEmpty(contrasena)){
            Toast.makeText(this, "Por favor, complrts los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //MOSTRAR EL PROGRESSBAR AL INICIO DEL PROCESO DE REGISTRO
        progressBar.setVisibility(View.VISIBLE);

        String numIdentificacion = "Incompleto";
        String telefono = "Incompleto";
        String fechaNacimiento = LocalDate.of(1990, 1, 1).format(DateTimeFormatter.ISO_DATE);
        String caracterizacion = "Incompleto";
        String role = "Usuario";

        ApiLogin apiLogin = new ApiLogin();
        Retrofit retrofit = apiLogin.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);

        RegisterRequest registerRequest = new RegisterRequest(nombre, apellido, email, contrasena, numIdentificacion, telefono, fechaNacimiento, caracterizacion, role);
        Call<Void> call = apiService.registerUser(registerRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                //Ocultar el progressbar
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    Toast.makeText(Register.this,"Registro exitoso",Toast.LENGTH_SHORT).show();
                    //Redirigir al usuario a la pantalla de inicio de sesion o al perfil
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(Register.this,"Error al registro"+ errorBody,Toast.LENGTH_SHORT).show();
                        Log.e("RegisterError","Error body:" +errorBody);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Register.this,"Fallo en la conexion" + t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e ("RegisterError","Error de conexion",t);
            }
        });
    }


    ///MOSTRAR Y OCULTAR TEXTO DE CONTRASEÑAS Y CONFIRMAR CONTRASEÑA
    //Metodo para configurar la alternancia de visibilidad de la contraseña
    private void setupPasswordVisibilityToggle(final EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    togglePasswordVisibility(editText);
                } else {
                    hidePassword(editText);
                }
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(editText);
            }
        });
    }

    //metodo para alternar la visibilidad de la contraseña
    private void togglePasswordVisibility(EditText editText) {
        if (!passwordVisible) {
            //Mostrar la contraseña
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisible = true;
        } else {
            //Ocultar contraseña
            hidePassword(editText);
        }
        //Mantener el cursor al  final del texto
        editText.setSelection(editText.getText().length());
    }
    //Metodo para ocultar la contraseña
    private void hidePassword(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordVisible = false;
        //Mantener el cursor al  final del texto
        editText.setSelection(editText.getText().length());
    }
}