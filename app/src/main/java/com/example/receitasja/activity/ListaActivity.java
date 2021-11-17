package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.receitasja.R;
import com.example.receitasja.adapter.ListaAdapter;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.UsuarioFirebase;
import com.example.receitasja.model.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private RecyclerView recyclerLista;
    private ListaAdapter listaAdapter;
    private List<Lista> minhaLista = new ArrayList<>();
    private ValueEventListener valueEventListenerLista;
    private DatabaseReference listaRef;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
        listaRef = ConfiguracaoFirebase.getFirebase().child("lista").child(idUsuarioLogado);

        iniciarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Minha lista");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaAdapter = new ListaAdapter(minhaLista, this);
        recyclerLista.setHasFixedSize(true);
        recyclerLista.setLayoutManager(new LinearLayoutManager(this));
        recyclerLista.setAdapter(listaAdapter);
    }

    private void listarReceitas() {

        valueEventListenerLista = listaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                minhaLista.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    minhaLista.add(dataSnapshot.getValue(Lista.class));
                }
                Collections.reverse(minhaLista);
                listaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void iniciarComponentes() {
        recyclerLista = findViewById(R.id.recyclerLista);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listarReceitas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listaRef.removeEventListener(valueEventListenerLista);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}