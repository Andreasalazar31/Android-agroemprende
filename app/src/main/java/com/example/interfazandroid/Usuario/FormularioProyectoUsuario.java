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

import com.example.interfazandroid.R;

public class FormularioProyectoUsuario extends AppCompatActivity {


    private static final int PICK_PDF_FILE = 2;
    private TextView tvNombreArchivoAdjunto;
    private Button btnEnviarProyecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_proyecto_usuario);

        tvNombreArchivoAdjunto = findViewById(R.id.tvNombreArchivoAdjunto);
        btnEnviarProyecto = findViewById(R.id.btnEnviarProyecto);

        // Configurar el icono para adjuntar PDF
        ImageView iconAdjuntarPdf = findViewById(R.id.iconAdjuntarPdf);
        iconAdjuntarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSelectorDeArchivos();
            }
        });

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