package com.example.receitasja.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.model.PostagemReceita;
import com.example.receitasja.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class AbrirPostagemActivity extends AppCompatActivity {

    private TextView nomeUsuarioAbrirPostagem,nomeReceitaAbrirPostagem,curtidasPostagem,ingredientesAbrirPostagem,receitaAbrirPostagem,visualizarComentarios;
    private CircleImageView fotoPerfilAbrirPostagem;
    private ImageView imageViewFotoPostagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_postagem);

        iniciarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Visualizar receita");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            PostagemReceita postagem = (PostagemReceita) bundle.getSerializable("postagem");
            Usuario usuario = (Usuario) bundle.getSerializable("usuario");

            Uri uriUsuario = Uri.parse(usuario.getCaminhoFoto());
            Glide.with(AbrirPostagemActivity.this).load(uriUsuario).into(fotoPerfilAbrirPostagem);
            nomeUsuarioAbrirPostagem.setText(usuario.getNome());

            Uri uriPostagem = Uri.parse(postagem.getCaminhoFoto());
            Glide.with(AbrirPostagemActivity.this).load(uriPostagem).into(imageViewFotoPostagem);
            nomeReceitaAbrirPostagem.setText(postagem.getTextNomeReceita());
            ingredientesAbrirPostagem.setText(postagem.getTextIngredientes());
            receitaAbrirPostagem.setText(postagem.getTextReceita());
        }
    }

    private  void iniciarComponentes() {
        nomeUsuarioAbrirPostagem = findViewById(R.id.nomeUsuarioAbrirPostagem);
        nomeReceitaAbrirPostagem = findViewById(R.id.nomeReceitaAbrirPostagem);
        curtidasPostagem = findViewById(R.id.curtidasPostagem);
        ingredientesAbrirPostagem = findViewById(R.id.ingredientesAbrirPostagem);
        receitaAbrirPostagem = findViewById(R.id.receitaAbrirPostagem);
        visualizarComentarios = findViewById(R.id.visualizarComentarios);
        fotoPerfilAbrirPostagem = findViewById(R.id.fotoPerfilAbrirPostagem);
        imageViewFotoPostagem = findViewById(R.id.imageViewFotoPostagem);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}