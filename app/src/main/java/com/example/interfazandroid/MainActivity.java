package com.example.interfazandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(MainActivity.this, Menu.class);
        startActivity(intent);
    }

    public void onRegisterClick(View view) {

        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }
}