package com.example.receitasja.model;

import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.example.receitasja.helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PostagemReceita implements Serializable {

    private String id;
    private String idUsuario;
    private String textNomeReceita;
    private String textReceita;
    private String textIngredientes;
    private String caminhoFoto;

    public PostagemReceita() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference postagemRef = firebaseRef.child("postagens");
        String idPostagem = postagemRef.push().getKey();
        setId(idPostagem);
    }

    public boolean salvarImagem (DataSnapshot seguidoresSnapshot){

        Map objeto = new HashMap();
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();

        String caminhoPost = "/" + getIdUsuario() + "/" + getId();
        objeto.put("/postagens" + caminhoPost, this);
        objeto.put("/lista" + caminhoPost, this);

        for (DataSnapshot seguidores: seguidoresSnapshot.getChildren()) {

            String idSeguidor = seguidores.getKey();

            HashMap<String, Object> dadosSeguidor = new HashMap<>();
            dadosSeguidor.put("fotoPostagem", getCaminhoFoto());
            dadosSeguidor.put("ingredientes", getTextIngredientes());
            dadosSeguidor.put("nomeReceita", getTextNomeReceita());
            dadosSeguidor.put("id", getId());
            dadosSeguidor.put("nomeUsuario", usuarioLogado.getNome());
            dadosSeguidor.put("fotoUsuario", usuarioLogado.getCaminhoFoto());

            String caminhoPostFeed = "/" + idSeguidor + "/" + getId();
            objeto.put("/feed" + caminhoPostFeed, dadosSeguidor);
        }

        firebaseRef.updateChildren(objeto);
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTextNomeReceita() {
        return textNomeReceita;
    }

    public void setTextNomeReceita(String textNomeReceita) {
        this.textNomeReceita = textNomeReceita;
    }

    public String getTextReceita() {
        return textReceita;
    }

    public void setTextReceita(String textReceita) {
        this.textReceita = textReceita;
    }

    public String getTextIngredientes() {
        return textIngredientes;
    }

    public void setTextIngredientes(String textIngredientes) {
        this.textIngredientes = textIngredientes;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
