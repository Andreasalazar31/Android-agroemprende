package com.example.interfazandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.interfazandroid.modelApi.ApiLogin;
import com.example.interfazandroid.modelApi.ApiService;
import com.example.interfazandroid.modelApi.UserDetails;
import com.example.interfazandroid.modelApi.UserUpdate;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioEditar extends AppCompatActivity {
    private EditText edtNombre, edtApellido, edtEmail, edtNumIdentificacion, edtTelefono, edtNacimiento, edtCaracterizacion;
    private Button btnguardardatos, btnEditEmail;
    private ApiService apiService;
    private boolean isEmailEditable = false;
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutos en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuario_editar);
        initializeViews();
        setupListeners();
        showUserIdToast(); // Verifica si el ID del usuario está guardado
    }

    private void initializeViews() {
        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtEmail = findViewById(R.id.edtEmail);
        edtNumIdentificacion = findViewById(R.id.edtNumIdentificacion);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtNacimiento = findViewById(R.id.edtNacimiento);
        // Asegúrate de que esta vista esté en tu layout
        btnguardardatos = findViewById(R.id.btnguardardatos);
        btnEditEmail = findViewById(R.id.btnEditarEmail);

        Log.d("UsuarioEditar", "Views initialized: edtNombre = " + edtNombre + ", edtApellido = " + edtApellido +
                ", edtEmail = " + edtEmail + ", edtNumIdentificacion = " + edtNumIdentificacion +
                ", edtTelefono = " + edtTelefono + ", edtNacimiento = " + edtNacimiento +
                ", edtCaracterizacion = " + edtCaracterizacion + ", btnguardardatos = " + btnguardardatos +
                ", btnEditEmail = " + btnEditEmail);
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
        updateUserInterface();
    }

    private void updateUserInterface() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        long lastUpdateTime = sharedPreferences.getLong("LastUpdateTime", 0);
        boolean shouldFetchFromApi = System.currentTimeMillis() - lastUpdateTime > CACHE_DURATION;

        if (shouldFetchFromApi) {
            cargarDatosUsuario();
        } else {
            loadDataFromSharedPreferences();
        }
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String userName = sharedPreferences.getString("UserName", "");
        String userEmail = sharedPreferences.getString("UserEmail", "");
        String userPhone = sharedPreferences.getString("UserPhone", "");
        String userNumIdentificacion = sharedPreferences.getString("UserNumIdentificacion", "");
        String userBirthDate = sharedPreferences.getString("UserBirthDate", "");

        if (!userName.isEmpty()) {
            String[] nameParts = userName.split(" ");
            if (nameParts.length >= 2) {
                edtNombre.setText(nameParts[0]);
                edtApellido.setText(nameParts[1]);
            }
        }
        edtEmail.setText(userEmail);
        edtTelefono.setText(userPhone);
        edtNumIdentificacion.setText(userNumIdentificacion);
        edtNacimiento.setText(userBirthDate);
    }

    private void showUserIdToast() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String userId = sharedPreferences.getString("UserId", null);

        if (userId != null) {
            //Toast.makeText(this, "ID del usuario guardado: " + userId, Toast.LENGTH_SHORT).show();
            Log.d("UsuarioEditar", "ID del usuario recuperado: " + userId);
        } else {
            Toast.makeText(this, "ID del usuario no encontrado", Toast.LENGTH_SHORT).show();
            Log.e("UsuarioEditar", "ID del usuario no encontrado en SharedPreferences");
        }
    }

    private void cargarDatosUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);

        if (token == null) {
            Log.e("EditarPerfilUsuario", "Token no encontrado en SharedPreferences");
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<UserDetails> call = apiService.getUserDetails("Bearer " + token);

        Log.d("EditarPerfilUsuario", "Llamada a la API con token: " + token);

        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                Log.d("EditarPerfilUsuario", "Código de respuesta: " + response.code());
                if (response.isSuccessful()) {
                    UserDetails userDetails = response.body();
                    if (userDetails != null) {
                        Log.d("EditarPerfilUsuario", "Respuesta completa: " + new Gson().toJson(userDetails));
                        UserDetails.Sub sub = userDetails.getSub();
                        if (sub != null) {
                            Log.d("EditarPerfilUsuario", "ID del usuario en respuesta: " + sub.get_id());
                            runOnUiThread(() -> mostrarDatosEnUI(sub));
                        } else {
                            Log.e("EditarPerfilUsuario", "Objeto 'sub' es null en la respuesta");
                        }
                    } else {
                        Log.e("EditarPerfilUsuario", "UserDetails es null en la respuesta");
                    }
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
        if (edtNombre != null && sub.getNombre() != null) edtNombre.setText(sub.getNombre());
        if (edtApellido != null && sub.getApellido() != null) edtApellido.setText(sub.getApellido());
        if (edtEmail != null && sub.getEmail() != null) edtEmail.setText(sub.getEmail());
        if (edtNumIdentificacion != null && sub.getNumIdentificacion() != null) edtNumIdentificacion.setText(sub.getNumIdentificacion());
        if (edtTelefono != null && sub.getTelefono() != null) edtTelefono.setText(sub.getTelefono());
        if (edtNacimiento != null && sub.getFechaNacimiento() != null) {
            // Extrae la fecha en el formato deseado
            String fechaNacimiento = sub.getFechaNacimiento().substring(0, 10);
            edtNacimiento.setText(fechaNacimiento);
        }
        if (edtCaracterizacion != null && sub.getCaracterizacion() != null) edtCaracterizacion.setText(sub.getCaracterizacion());
    }

    private void actualizarDatosUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE);
        String token = sharedPreferences.getString("UserToken", null);
        String userId = sharedPreferences.getString("UserId", null);

        if (token == null || userId == null) {
            Toast.makeText(this, "Información de usuario no encontrado", Toast.LENGTH_SHORT).show();
            Log.e("ActualizarDatosUsuario", "Token o ID de usuario no encontrado");
            return;
        }

        UserUpdate userUpdate = createUserUpdateObject();

        apiService = new ApiLogin().getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.updateUserProfile(userId, "Bearer " + token, userUpdate);
        Log.d("ActualizarDatosUsuario", "Llamada a la API para actualizar datos con ID: " + userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    UserUpdate userUpdate = createUserUpdateObject();
                    updateSharedPreferences(userUpdate);
                    Toast.makeText(UsuarioEditar.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a PerfilUsuario
                } else {
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ActualizarDatosUsuario", "Error de conexión: " + t.getMessage(), t);
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
        editor.putString("UserNumIdentificacion", updatedUser.getNumIdentificacion());
        editor.putString("UserBirthDate", updatedUser.getFechaNacimiento());
        editor.putLong("LastUpdateTime", System.currentTimeMillis());
        editor.apply();
    }

    private void handleApiError(Response<?> response) {
        Log.e("ApiError", "Código de respuesta: " + response.code() + ", Mensaje: " + response.message());
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Sin cuerpo de error";
            Log.e("ApiError", "Cuerpo del error: " + errorBody);
        } catch (IOException e) {
            Log.e("ApiError", "Error al leer el cuerpo del error", e);
        }
    }
}
