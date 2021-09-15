package com.example.receitasja.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.receitasja.R;
import com.example.receitasja.adapter.PostagemAdapter;
import com.example.receitasja.model.PostagemReceita;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView icPerfil;
    private RecyclerView recyclerPost;
    private List<PostagemReceita> postagens;

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerPost.setLayoutManager( layoutManager );

        PostagemAdapter adapter = new PostagemAdapter();
        recyclerPost.setAdapter( adapter );

    }

    private void iniciarComponentes() {
        icPerfil = findViewById(R.id.iconMenu);
        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerPost = findViewById(R.id.recyclerPost);
    }
}