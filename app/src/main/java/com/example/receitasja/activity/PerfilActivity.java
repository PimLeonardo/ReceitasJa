package com.example.receitasja.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Button buttonCriarLista,buttonEditarSeguirPerfil;
    private CircleImageView imagePerfilFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iniciarComponentes();

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

            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if (caminhoFoto != null) {
                Uri uri = Uri.parse(caminhoFoto);
                Glide.with(PerfilActivity.this).load(uri).into(imagePerfilFoto);
            }else {
                imagePerfilFoto.getResources().getDrawable(R.drawable.avatar);
            }
        }
    }

    private  void iniciarComponentes() {
        buttonCriarLista = findViewById(R.id.buttonCriarLista);
        buttonEditarSeguirPerfil = findViewById(R.id.buttonEditarSeguirPerfil);
        imagePerfilFoto = findViewById(R.id.imagePerfilFoto);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}