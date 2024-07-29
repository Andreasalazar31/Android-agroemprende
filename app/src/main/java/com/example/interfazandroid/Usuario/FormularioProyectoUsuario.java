package com.example.interfazandroid.Usuario;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import android.provider.DocumentsContract;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.interfazandroid.MenuRegistro.MainActivity;
import com.example.interfazandroid.R;
import com.example.interfazandroid.UsuarioMenu;
import com.example.interfazandroid.UsuarioPerfil;

public class FormularioProyectoUsuario extends AppCompatActivity {


    private static final int PICK_PDF_FILE = 2;
    private TextView tvNombreArchivoAdjunto;
    private Button btnEnviarProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_proyecto_usuario);
        //////////////////////////////////TOOLBAR///////////////////////////////////////////////////

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView icon1 = findViewById(R.id.icon1);
        ImageView icon2 = findViewById(R.id.icon2);
        ImageView icon3 = findViewById(R.id.icon3);

        icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega a la vista deseada para icon1
                Intent intent = new Intent(FormularioProyectoUsuario.this, UsuarioPerfil.class);
                startActivity(intent);
            }
        });
        icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega a la vista deseada para icon2
                Intent intent = new Intent(FormularioProyectoUsuario.this, UsuarioMenu.class);
                startActivity(intent);
            }
        });
        icon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega a la vista deseada para icon3
                Intent intent = new Intent(FormularioProyectoUsuario.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        btnEnviarProyecto = findViewById(R.id.btnEnviarProyecto);





        btnEnviarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes mostrar un mensaje o realizar cualquier acción
                Toast.makeText(FormularioProyectoUsuario.this, "¡Proyecto enviado correctamente!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirSelectorDeArchivos() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String nombreArchivo = obtenerNombreArchivo(uri);
                tvNombreArchivoAdjunto.setText(nombreArchivo);
            }
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        String nombreArchivo = "";
        if (uri.getScheme().equals("content")) {
            String[] projection = {DocumentsContract.Document.COLUMN_DISPLAY_NAME};
            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME);
                    nombreArchivo = cursor.getString(index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nombreArchivo;
    }
}