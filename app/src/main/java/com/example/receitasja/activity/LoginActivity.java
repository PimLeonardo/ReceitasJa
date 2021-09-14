package com.example.receitasja.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.receitasja.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button buttonCadastrar,buttonEntrar;
    private EditText editEmail,editSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        iniciarComponentes();

        buttonCadastrar.setOnClickListener(v -> {

            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        buttonEntrar.setOnClickListener(v -> {

            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(v,"Preencha todos os campos",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.BLACK);
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }else {
                autenticarUsuario(v);
            }
        });
    }

    private void autenticarUsuario(View v){

        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                progressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed((Runnable) () -> {
                    telaMain();
                }, 3000);
            }else {
                String erro;

                try {
                    throw task.getException();
                }catch (Exception e){
                    erro = "Erro ao logar";
                }
                Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.BLACK);
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null) {
            telaMain();
        }
    }

    private void telaMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void iniciarComponentes() {
        progressBar = findViewById(R.id.progressBar);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        buttonEntrar = findViewById(R.id.buttonEntrar);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);
    }
}