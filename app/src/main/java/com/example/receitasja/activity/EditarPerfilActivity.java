package com.example.receitasja.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.receitasja.R;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.Permissao;
import com.example.receitasja.helper.UsuarioFirebase;
import com.example.receitasja.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imagePerfilFotoEditar;
    private EditText editUsuarioEditarPerfil;
    private TextView emailEditarPerfil, nomeEditarperfil, textAlterarFoto;
    private Button buttonSalvarEditarPerfil;
    private Usuario usuarioLogado;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private StorageReference storageRef;
    private String idUsuario;
    private String[] permissoesPost = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Permissao.validarPermissoes(permissoesPost, this, 1);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        idUsuario = UsuarioFirebase.getIdUsuario();

        iniciarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuario();
        nomeEditarperfil.setText(usuarioPerfil.getDisplayName());
        editUsuarioEditarPerfil.setText(usuarioPerfil.getDisplayName());
        emailEditarPerfil.setText(usuarioPerfil.getEmail());

        Uri url = usuarioPerfil.getPhotoUrl();
        if (url != null) {
            Glide.with(EditarPerfilActivity.this).load(url).into(imagePerfilFotoEditar);
        }else {
            imagePerfilFotoEditar.setImageResource(R.drawable.avatar);
        }

        buttonSalvarEditarPerfil.setOnClickListener(v -> {

            String nomeAtualizado = editUsuarioEditarPerfil.getText().toString();
            UsuarioFirebase.atualizarNome(nomeAtualizado);

            usuarioLogado.setNome(nomeAtualizado);
            usuarioLogado.atualizar();
            Toast.makeText(EditarPerfilActivity.this,"Dados atualizados",Toast.LENGTH_SHORT).show();
        });


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            if ( result.getResultCode() == RESULT_OK){
                Bitmap imagem = null;
                try {
                    Uri localImageSelecionada = result.getData().getData();
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImageSelecionada);
                    if (imagem != null) {
                        imagePerfilFotoEditar.setImageBitmap(imagem);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        imagem.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                        byte[] dadosimagem = byteArrayOutputStream.toByteArray();

                        StorageReference imagemRef = storageRef.child("imagens").child("perfil").child(idUsuario + ".jpef");
                        UploadTask uploadTask = imagemRef.putBytes(dadosimagem);

                        uploadTask.addOnFailureListener(e -> {
                            Toast.makeText(EditarPerfilActivity.this,"Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }).addOnSuccessListener(taskSnapshot -> {

                            imagemRef.getDownloadUrl().addOnCompleteListener(task -> {

                                Uri uri = task.getResult();
                                atualizarFotoUsuario(uri);
                            });

                            Toast.makeText(EditarPerfilActivity.this,"Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        textAlterarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if (intent.resolveActivity(getPackageManager()) != null){
                activityResultLauncher.launch(intent);
            }else {
                Toast.makeText(EditarPerfilActivity.this,"Esse app não tem suporte para essa ação", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarFotoUsuario(Uri url) {

        UsuarioFirebase.atualizarFoto(url);
        usuarioLogado.setCaminhoFoto(url.toString());
        usuarioLogado.atualizar();
    }

    private void iniciarComponentes() {

        imagePerfilFotoEditar = findViewById(R.id.imagePerfilFotoEditar);
        textAlterarFoto = findViewById(R.id.textAlterarFoto);
        editUsuarioEditarPerfil = findViewById(R.id.editUsuarioEditarPerfil);
        emailEditarPerfil = findViewById(R.id.emailEditarPerfil);
        nomeEditarperfil= findViewById(R.id.nomeEditarperfil);
        buttonSalvarEditarPerfil = findViewById(R.id.buttonSalvarEditarPerfil);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;
    }
}