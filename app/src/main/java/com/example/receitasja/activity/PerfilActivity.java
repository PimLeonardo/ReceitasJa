package com.example.receitasja.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.receitasja.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilActivity extends AppCompatActivity {

    private TextView nomeUsuario,emailUsuario;
    private ImageView iconLogout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().hide();

        iniciarCoponentes();

        iconLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("usuarios").document(usuarioID);
        documentReference.addSnapshotListener((value, error) -> {
            if (value != null){
                nomeUsuario.setText(value.getString("nome"));
                emailUsuario.setText(email);
            }
        });
    }

    private void iniciarCoponentes() {
        nomeUsuario = findViewById(R.id.nomeUsuario);
        emailUsuario = findViewById(R.id.emailUsuario);
        iconLogout = findViewById(R.id.iconLogout);
    }

}