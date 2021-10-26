package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.receitasja.R;
import com.example.receitasja.adapter.ComentarioAdapter;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.UsuarioFirebase;
import com.example.receitasja.model.Comentario;
import com.example.receitasja.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private EditText editTextTextComentario;
    private Button buttonEnviarComentario;
    private RecyclerView recyclerComentario;
    private String idPostagem;
    private Usuario usuario;
    private ComentarioAdapter comentarioAdapter;
    private List<Comentario> comentarioList = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private DatabaseReference comentariosRef;
    private ValueEventListener valueEventListenerComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        iniciarComponentes();

        usuario = UsuarioFirebase.getDadosUsuarioLogado();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Comentários");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        comentarioAdapter = new ComentarioAdapter(comentarioList, getApplicationContext());
        recyclerComentario.setHasFixedSize(true);
        recyclerComentario.setLayoutManager(new LinearLayoutManager(this));
        recyclerComentario.setAdapter(comentarioAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idPostagem = bundle.getString("idPostagem");
        }

        buttonEnviarComentario.setOnClickListener(v -> {
            salvarComentario();
        });
    }

    private void recuperarComentarios() {

        comentariosRef = firebaseRef.child("comentarios").child(idPostagem);

        valueEventListenerComentarios = comentariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comentarioList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    comentarioList.add(dataSnapshot.getValue(Comentario.class));
                }
                comentarioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentariosRef.removeEventListener(valueEventListenerComentarios);
    }

    public void salvarComentario() {

        String textoComentario = editTextTextComentario.getText().toString();
        if (textoComentario != null && !textoComentario.equals("")) {

            Comentario comentario = new Comentario();
            comentario.setIdPostagem(idPostagem);
            comentario.setIdUsuario(usuario.getId());
            comentario.setNomeUsuario(usuario.getNome());
            comentario.setCaminhoFoto(usuario.getCaminhoFoto());
            comentario.setComentario(textoComentario);
            if (comentario.salvarComentario()) {
                Toast.makeText(this,"Comentário feito com sucesso",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this,"Insira um comentário",Toast.LENGTH_SHORT).show();
        }
        editTextTextComentario.setText("");
    }

    private void iniciarComponentes() {
        editTextTextComentario = findViewById(R.id.editTextTextComentario);
        buttonEnviarComentario = findViewById(R.id.buttonEnviarComentario);
        recyclerComentario = findViewById(R.id.recyclerComentario);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}