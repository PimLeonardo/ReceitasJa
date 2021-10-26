package com.example.receitasja.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.receitasja.R;
import com.example.receitasja.fragment.FeedFragment;
import com.example.receitasja.fragment.PerfilFragment;
import com.example.receitasja.fragment.PesquisaFragment;
import com.example.receitasja.fragment.PostFragment;
import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Receitas Já");

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        configurarBottomNavigationView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();

    }

    private void configurarBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavegation);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()){
                case R.id.navHome:
                    fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
                    return true;
                case R.id.navPesquisar:
                    fragmentTransaction.replace(R.id.viewPager, new PesquisaFragment()).commit();
                    return true;
                case R.id.navAddPost:
                    fragmentTransaction.replace(R.id.viewPager, new PostFragment()).commit();
                    return true;
                case R.id.navPerfil:
                    fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}