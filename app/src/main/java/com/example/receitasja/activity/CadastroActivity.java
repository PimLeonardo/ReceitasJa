package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.receitasja.R;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.UsuarioFirebase;
import com.example.receitasja.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome,editEmail,editSenha;
    private Button botaoCadastrar;
    private ProgressBar progressBar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        iniciarComponentes();

        botaoCadastrar.setOnClickListener(v -> {

            String nome = editNome.getText().toString();
            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {

                Toast.makeText(CadastroActivity.this,"Preencha todos os campos",Toast.LENGTH_SHORT).show();
            }else {

                usuario = new Usuario();
                usuario.setNome(nome);
                usuario.setEmail(email);
                usuario.setSenha(senha);
                cadastrarUsuario(usuario);
            }
        });
    }

    private void cadastrarUsuario(Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {

                try {

                    String idUsuario = task.getResult().getUser().getUid();
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    UsuarioFirebase.atualizarNome(usuario.getNome());

                    Toast.makeText(CadastroActivity.this,"Cadastro realizado com sucesso",Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed((Runnable) () -> {
                        telaMain();
                    }, 3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {

                String erro;
                try {
                    throw task.getException();

                }catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Digite uma senha com no mínimo 6 caracteres";
                }catch (FirebaseAuthUserCollisionException e) {
                    erro = "Email já cadastrado";
                }catch (FirebaseAuthInvalidCredentialsException e){
                    erro = "Email inválido";
                }catch (Exception e) {
                    erro = "Erro ao cadastrar usuário";
                }

                Toast.makeText(CadastroActivity.this,erro,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void telaMain() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private  void iniciarComponentes() {
        editNome = findViewById(R.id.nomeCadastro);
        editEmail = findViewById(R.id.emailCadastro);
        editSenha = findViewById(R.id.senhaCadastro);
        botaoCadastrar = findViewById(R.id.confirmarCadastro);
        progressBar = findViewById(R.id.progressBar);
    }
}