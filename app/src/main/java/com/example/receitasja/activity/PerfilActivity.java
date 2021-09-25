package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Button buttonCriarLista,buttonEditarSeguirPerfil;
    private CircleImageView imagePerfilFoto;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioSeguindoRef;
    private ValueEventListener valueEventListener;
    private TextView textSeguidores,textSeguindo,nomePerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iniciarComponentes();

        usuarioRef = ConfiguracaoFirebase.getFirebase().child("usuario");

        buttonCriarLista.setVisibility(View.INVISIBLE);
        buttonEditarSeguirPerfil.setText("Seguir");

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioClick");

            getSupportActionBar().setTitle(usuarioSelecionado.getNome());
            nomePerfil.setText(usuarioSelecionado.getNome());

            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if (caminhoFoto != null) {
                Uri uri = Uri.parse(caminhoFoto);
                Glide.with(PerfilActivity.this).load(uri).into(imagePerfilFoto);
            }else {
                imagePerfilFoto.getResources().getDrawable(R.drawable.avatar);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarSeguidores();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioSeguindoRef.removeEventListener(valueEventListener);
    }

    private void recuperarSeguidores() {

        usuarioSeguindoRef = usuarioRef.child(usuarioSelecionado.getId());
        valueEventListener = usuarioSeguindoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue(Usuario.class);

                String seguindo = String.valueOf(usuario.getSeguindo());
                String seguidores = String.valueOf(usuario.getSeguidores());

                textSeguidores.setText(seguidores);
                textSeguindo.setText(seguindo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void iniciarComponentes() {
        buttonCriarLista = findViewById(R.id.buttonCriarLista);
        buttonEditarSeguirPerfil = findViewById(R.id.buttonEditarSeguirPerfil);
        imagePerfilFoto = findViewById(R.id.imagePerfilFoto);
        textSeguidores = findViewById(R.id.numeroSeguidoresPerfil);
        textSeguindo = findViewById(R.id.numeroSeguindoPerfil);
        nomePerfil = findViewById(R.id.nomePerfil);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}