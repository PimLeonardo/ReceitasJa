package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.receitasja.R;
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
    String[] alertas = {"Preencha todos os campos","Cadastro realizado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().hide();

        iniciarComponentes();

        botaoCadastrar.setOnClickListener(v -> {

            String nome = editNome.getText().toString();
            String email = editEmail.getText().toString();
            String senha = editSenha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v,alertas[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.BLACK);
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }else {
                cadastrarUsuario(v);
            }
        });
    }

    private void cadastrarUsuario(View v) {

        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                salvarDadosUsuario();

                Snackbar snackbar = Snackbar.make(v,alertas[1],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.BLACK);
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
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

                Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.BLACK);
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
        });
    }

    private void salvarDadosUsuario() {
        String nome = editNome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(unused -> {
            Log.d("db", "Salvou os dado com sucesso");
        })
        .addOnFailureListener(e -> {
            Log.d("dbError", "Erro ao salvar os dados" + e.toString());
        });
    }

    private  void iniciarComponentes() {

        editNome = findViewById(R.id.nomeCadastro);
        editEmail = findViewById(R.id.emailCadastro);
        editSenha = findViewById(R.id.senhaCadastro);
        botaoCadastrar = findViewById(R.id.confirmarCadastro);
    }
}