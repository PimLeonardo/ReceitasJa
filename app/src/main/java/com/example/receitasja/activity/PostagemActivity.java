package com.example.receitasja.activity;

import androidx.annotation.NonNull;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PostagemActivity extends AppCompatActivity {

    private ImageView fotoPostEscolhida;
    private Bitmap imagem;
    private String idUsuarioLogado;
    private TextInputEditText textNomeReceita,textReceita,textIngredientes;
    private ProgressBar progressBarPostagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        iniciarComponentes();

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

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

    private void fazerPostagem(){

        PostagemReceita postagem = new PostagemReceita();
        postagem.setIdUsuario(idUsuarioLogado);
        postagem.setTextReceita(textReceita.getText().toString());
        postagem.setTextNomeReceita(textNomeReceita.getText().toString());
        postagem.setTextIngredientes(textIngredientes.getText().toString());

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

                if (postagem.salvarImagem()) {

                    progressBarPostagem.setVisibility(View.VISIBLE);
                    new Handler().postDelayed((Runnable) () -> {
                        finish();
                    }, 3000);
                    Toast.makeText(PostagemActivity.this,"Sucesso ao salvar imagem", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private  void iniciarComponentes() {
        fotoPostEscolhida = findViewById(R.id.fotoPostEscolhida);
        textNomeReceita = findViewById(R.id.textNomeReceita);
        textReceita = findViewById(R.id.textReceita);
        textIngredientes = findViewById(R.id.textIngredientes);
        progressBarPostagem = findViewById(R.id.progressBarPostagem);
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