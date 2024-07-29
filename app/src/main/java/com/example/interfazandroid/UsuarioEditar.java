package com.example.interfazandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.UserDetails;
import com.example.interfazandroid.modelApi.UserUpdate;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioEditar extends AppCompatActivity {
    private EditText edtNombre, edtApellido, edtEmail, edtNumIdentificacion, edtTelefono, edtNacimiento, edtCaracterizacion;
    private Button btnguardardatos, btnEditEmail;
    private ApiService apiService;
    private boolean isEmailEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_editar);
        initializeViews();
        setupListeners();

    }
    private void initializeViews() {
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtEmail = findViewById(R.id.edtEmail);
        edtNumIdentificacion = findViewById(R.id.edtNumIdentificacion);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtNacimiento = findViewById(R.id.edtNacimiento);
        btnguardardatos = findViewById(R.id.btnguardardatos);
        btnEditEmail = findViewById(R.id.btnEditarEmail);

        // Verificar si las vistas se inicializaron correctamente
        if (edtNombre == null || edtApellido == null || edtEmail == null ||
                edtNumIdentificacion == null || edtTelefono == null || edtNacimiento == null ||
                edtCaracterizacion == null || btnguardardatos == null || btnEditEmail == null) {
            Log.e("UsuarioEditar", "Una o más vistas no se inicializaron correctamente");
        }
    }
    private void setupListeners() {
        btnEditEmail.setOnClickListener(v -> toggleEmailEditable());
        btnguardardatos.setOnClickListener(v -> actualizarDatosUsuario());
    }
    private void toggleEmailEditable() {
        isEmailEditable = !isEmailEditable;
        edtEmail.setEnabled(isEmailEditable);
        btnEditEmail.setText(isEmailEditable ? "Ocultar" : "Editar");
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosUsuario();
    }
    private void cargarDatosUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token == null) {
            Log.e("EditarPerfilUsuario", "Token no encontrado");
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<UserDetails> call = apiService.getUserDetails("Bearer " + token);

        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getSub() != null) {
                    UserDetails.Sub sub = response.body().getSub();
                    runOnUiThread(() -> mostrarDatosEnUI(sub));
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Log.e("EditarPerfilUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(UsuarioEditar.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarDatosEnUI(UserDetails.Sub sub) {
        if (edtNombre != null) edtNombre.setText(sub.getNombre());
        if (edtApellido != null) edtApellido.setText(sub.getApellido());
        if (edtEmail != null) edtEmail.setText(sub.getEmail());
        if (edtNumIdentificacion != null) edtNumIdentificacion.setText(sub.getNumIdentificacion());
        if (edtTelefono != null) edtTelefono.setText(sub.getTelefono());
        if (edtNacimiento != null) edtNacimiento.setText(sub.getFechaNacimieto());
        if (edtCaracterizacion != null) edtCaracterizacion.setText(sub.getCaracterizacion());
    }

    private void actualizarDatosUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);
        String userId = sharedPreferences.getString("UserId", null);

        if (token == null || userId == null) {
            Toast.makeText(this, "Información de usuario no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        UserUpdate userUpdate = createUserUpdateObject();

        apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.updateUserProfile(userId, "Bearer " + token, userUpdate);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    updateSharedPreferences(userUpdate);
                    Toast.makeText(UsuarioEditar.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a PerfilUsuario
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EditarPerfilUsuario", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(UsuarioEditar.this, "Falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private UserUpdate createUserUpdateObject() {
        UserUpdate userUpdate = new UserUpdate();
        userUpdate.setNombre(edtNombre.getText().toString());
        userUpdate.setApellido(edtApellido.getText().toString());
        userUpdate.setNumIdentificacion(edtNumIdentificacion.getText().toString());
        userUpdate.setTelefono(edtTelefono.getText().toString());
        userUpdate.setFechaNacimiento(edtNacimiento.getText().toString());
        if (isEmailEditable) {
            userUpdate.setEmail(edtEmail.getText().toString());
        }
        return userUpdate;
    }
    private void updateSharedPreferences(UserUpdate updatedUser) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", updatedUser.getNombre() + " " + updatedUser.getApellido());
        if (isEmailEditable) {
            editor.putString("UserEmail", updatedUser.getEmail());
        }
        editor.putString("UserPhone", updatedUser.getTelefono());
        editor.putLong("LastUpdateTime", System.currentTimeMillis());
        editor.apply();
    }
    private void handleApiError(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
            Log.e("EditarPerfilUsuario", "Error API. Código: " + response.code() + ", Cuerpo: " + errorBody);
            Toast.makeText(this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("EditarPerfilUsuario", "Error al leer el cuerpo del error", e);
        }
    }

}