package com.example.interfazandroid;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.InputType;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;


public class Register extends AppCompatActivity {
    private EditText etPassword, etConfirmPassword;
    private TextInputLayout tilConfirmPassword;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPasswordd);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    // Mostrar un mensaje indicando que se deben completar todos los campos
                    Toast.makeText(Register.this, "Llena los campos", Toast.LENGTH_SHORT).show();
                } else if (password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Mostrar/Ocultar contraseña al hacer clic en el EditText de contraseña
        setupPasswordVisibilityToggle(etPassword);
        setupPasswordVisibilityToggle(etConfirmPassword);
    }

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

    private void togglePasswordVisibility(EditText editText) {
        if (!passwordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisible = true;
        } else {
            hidePassword(editText);
        }
        editText.setSelection(editText.getText().length());
    }

    private void hidePassword(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordVisible = false;
        editText.setSelection(editText.getText().length());
    }
}