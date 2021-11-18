package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.receitasja.R;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.UsuarioFirebase;
import com.example.receitasja.model.PostagemReceita;
import com.example.receitasja.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PostagemActivity extends AppCompatActivity {

    private ImageView fotoPostEscolhida;
    private Bitmap imagem;
    private String idUsuarioLogado;
    private Usuario usuarioLogado;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference firebaseRef;
    private DataSnapshot seguidoresSnapshot;
    private AlertDialog dialog;
    private TextInputEditText textNomeReceita,textReceita,textIngredientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        iniciarComponentes();

        firebaseRef = ConfiguracaoFirebase.getFirebase();
        usuarioRef = ConfiguracaoFirebase.getFirebase().child("usuarios");
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        recuperarDadosLogado();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Postagem");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            byte[] dadosImagem = bundle.getByteArray("fotoSelecionada");
            imagem = BitmapFactory.decodeByteArray(dadosImagem,0,dadosImagem.length);
            fotoPostEscolhida.setImageBitmap(imagem);
        }
    }

    private void alertDialog (String titulo) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titulo);
        alert.setCancelable(false);
        alert.setView(R.layout.dialog_carregando);
        dialog = alert.create();
        dialog.show();
    }

    private void recuperarDadosLogado() {

        alertDialog ("Carregando");
        usuarioLogadoRef = usuarioRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usuarioLogado = snapshot.getValue(Usuario.class);

                DatabaseReference seguidoresRef = firebaseRef.child("seguidores").child(idUsuarioLogado);
                seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        seguidoresSnapshot = snapshot;
                        dialog.cancel();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fazerPostagem() {

        PostagemReceita postagem = new PostagemReceita();
        postagem.setIdUsuario(idUsuarioLogado);

        String titulo = textNomeReceita.getText().toString();
        String ingredientes = textIngredientes.getText().toString();
        String receita = textReceita.getText().toString();

        if (titulo.isEmpty() || ingredientes.isEmpty() || receita.isEmpty()) {

            Toast.makeText(PostagemActivity.this,"Preencha todos os campos",Toast.LENGTH_SHORT).show();
        } else {
        alertDialog ("Salvando postagem");

        postagem.setTextReceita(receita);
        postagem.setTextNomeReceita(ingredientes);
        postagem.setTextIngredientes(titulo);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] dadosimagem = byteArrayOutputStream.toByteArray();

        StorageReference storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        StorageReference imagemRef = storageRef.child("imagens").child("postagens").child(postagem.getId() + ".jpeg");

        UploadTask uploadTask = imagemRef.putBytes(dadosimagem);
        uploadTask.addOnFailureListener(e -> {
            Toast.makeText(PostagemActivity.this,"Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {

            imagemRef.getDownloadUrl().addOnCompleteListener(task -> {

                Uri uri = task.getResult();
                postagem.setCaminhoFoto(uri.toString());

                if (postagem.salvarImagem(seguidoresSnapshot)) {

                    Toast.makeText(PostagemActivity.this,"Sucesso ao salvar imagem", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    finish();
                }
            });
        });
        }
    }

    private  void iniciarComponentes() {
        fotoPostEscolhida = findViewById(R.id.fotoPostEscolhida);
        textNomeReceita = findViewById(R.id.textNomeReceita);
        textReceita = findViewById(R.id.textReceita);
        textIngredientes = findViewById(R.id.textIngredientes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_publicar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_postagem:
                fazerPostagem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}