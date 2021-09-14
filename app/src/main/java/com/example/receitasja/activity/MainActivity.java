package com.example.receitasja.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.receitasja.R;

public class MainActivity extends AppCompatActivity {

    private ImageView icPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        iniciarComponentes();

        icPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarComponentes() {
        icPerfil = findViewById(R.id.iconMenu);
    }
}