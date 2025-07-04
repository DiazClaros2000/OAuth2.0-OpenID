package com.example.tareasoauthapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TareasActivity extends AppCompatActivity {
    String token;
    TextView txtTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        token = getIntent().getStringExtra("token");
        txtTareas = findViewById(R.id.txtTareas);

        findViewById(R.id.btnCargar).setOnClickListener(v -> cargarTareas());
    }

    void cargarTareas() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/api_tareas/get_tareas.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "Bearer " + token);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                runOnUiThread(() -> txtTareas.setText(sb.toString()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

