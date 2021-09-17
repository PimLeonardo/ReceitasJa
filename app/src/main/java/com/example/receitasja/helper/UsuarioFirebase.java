package com.example.receitasja.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.receitasja.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

    public static FirebaseUser getUsuario(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static String getIdUsuario(){
        return  getUsuario().getUid();
    }

    public static void atualizarNome(String nome) {

        try {

            FirebaseUser user = getUsuario();

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            user.updateProfile(profile).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("User","Atualizou o nome de perfil com sucesso");
                }else {
                    Log.d("User","Erro ao atualizar o nome de perfil");
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void atualizarFoto(Uri url) {

        try {

            FirebaseUser user = getUsuario();

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();
            user.updateProfile(profile).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("User","Atualizou a foto de perfil com sucesso");
                }else {
                    Log.d("User","Erro ao atualizar a foto de perfil");
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Usuario getDadosUsuarioLogado() {

        FirebaseUser firebaseUser = getUsuario();

        Usuario usuario = new Usuario();
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setId(firebaseUser.getUid());

        if (firebaseUser.getPhotoUrl() == null) {
            usuario.setCaminhoFoto("");
        }else {
            usuario.setCaminhoFoto(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;
    }

}
