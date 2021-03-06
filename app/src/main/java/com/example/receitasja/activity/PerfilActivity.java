package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.adapter.GridAdapter;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.UsuarioFirebase;
import com.example.receitasja.model.PostagemReceita;
import com.example.receitasja.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;
    private Button buttonCriarLista,buttonEditarSeguirPerfil;
    private CircleImageView imagePerfilFoto;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioSeguindoRef;
    private DatabaseReference seguidoresRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference postagensUsuarioRef;
    private GridView gridViewPerfil;
    private GridAdapter gridAdapter;
    private String idUsuarioLogado;
    private ValueEventListener valueEventListener;
    private TextView textSeguidores,textSeguindo,nomePerfil;
    private List<PostagemReceita> postagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        iniciarComponentes();

        usuarioRef = ConfiguracaoFirebase.getFirebase().child("usuarios");
        seguidoresRef = ConfiguracaoFirebase.getFirebase().child("seguidores");

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        buttonCriarLista.setVisibility(View.INVISIBLE);
        buttonEditarSeguirPerfil.setText("Seguir");

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioClick");
            postagensUsuarioRef = ConfiguracaoFirebase.getFirebase().child("postagens").child(usuarioSelecionado.getId());

            getSupportActionBar().setTitle(usuarioSelecionado.getNome());
            nomePerfil.setText(usuarioSelecionado.getNome());

            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if (caminhoFoto != null) {
                Uri uri = Uri.parse(caminhoFoto);
                Glide.with(PerfilActivity.this).load(uri).into(imagePerfilFoto);
            }else {
                imagePerfilFoto.setImageResource(R.drawable.avatar);
            }
        }

        iniciarImageLoader();

        carregarPostagem();

        gridViewPerfil.setOnItemClickListener((parent, view, position, id) -> {

            PostagemReceita postagemReceita = postagens.get(position);
            Intent intent = new Intent(getApplicationContext(), AbrirPostagemActivity.class);
            intent.putExtra("postagem", postagemReceita);
            intent.putExtra("usuario", usuarioSelecionado);
            startActivity(intent);
        });
    }

    public void iniciarImageLoader() {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).memoryCache(new LruMemoryCache(2*1024*1024))
                .memoryCacheSize(2*1024*1024).diskCacheSize(50*1024*1024).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();
        ImageLoader.getInstance().init(configuration);
    }

    private void recuperarDadosLogado() {

        usuarioLogadoRef = usuarioRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usuarioLogado = snapshot.getValue(Usuario.class);
                verificaSegueUsuario();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificaSegueUsuario() {

        DatabaseReference seguidorRef = seguidoresRef.child(usuarioSelecionado.getId()).child(idUsuarioLogado);

        seguidorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    botaoSeguir(true);
                }else {
                    botaoSeguir(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void botaoSeguir (boolean segueUsuario) {

        if (segueUsuario) {
            buttonEditarSeguirPerfil.setText("Seguindo");
        }else {
            buttonEditarSeguirPerfil.setText("Seguir");

            buttonEditarSeguirPerfil.setOnClickListener(v -> {

                armazenarSeguidor(usuarioLogado, usuarioSelecionado);
            });
        }
    }

    private void armazenarSeguidor(Usuario userLogado, Usuario userSelecionado) {

        HashMap<String,Object> dadosUsuarioLogado = new HashMap<>();
        dadosUsuarioLogado.put("nome",userLogado.getNome());
        dadosUsuarioLogado.put("caminhoFoto",userLogado.getCaminhoFoto());
        DatabaseReference seguidorRef = seguidoresRef.child(userSelecionado.getId()).child(userLogado.getId());
        seguidorRef.setValue(dadosUsuarioLogado);

        buttonEditarSeguirPerfil.setOnClickListener(null);
        buttonEditarSeguirPerfil.setText("Seguindo");

        int seguidores = userSelecionado.getSeguidores() + 1;
        int seguindo = userLogado.getSeguindo() + 1;

        HashMap<String,Object> dadosSeguindo = new HashMap<>();
        dadosSeguindo.put("seguindo",seguindo);
        DatabaseReference usuarioSeguindo = usuarioRef.child(userLogado.getId());
        usuarioSeguindo.updateChildren(dadosSeguindo);

        HashMap<String,Object> dadosSeguidores = new HashMap<>();
        dadosSeguidores.put("seguidores",seguidores);
        DatabaseReference usuarioSeguindores = usuarioRef.child(userSelecionado.getId());
        usuarioSeguindores.updateChildren(dadosSeguidores);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarSeguidores();
        recuperarDadosLogado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioSeguindoRef.removeEventListener(valueEventListener);
    }

    public void carregarPostagem() {

        postagens = new ArrayList<>();
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> urlFotos = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    PostagemReceita postagemReceita = dataSnapshot.getValue(PostagemReceita.class);
                    postagens.add(postagemReceita);
                    urlFotos.add(postagemReceita.getCaminhoFoto());
                }
                Collections.reverse(urlFotos);
                Collections.reverse(postagens);
                gridAdapter = new GridAdapter(getApplicationContext(),R.layout.grid_perfil,urlFotos);
                gridViewPerfil.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        gridViewPerfil = findViewById(R.id.gridViewPerfil);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}