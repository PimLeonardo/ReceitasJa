package com.example.receitasja.model;

import com.example.receitasja.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class PostagemReceita {

    private String id;
    private String idUsuario;
    private String textNomeReceita;
    private String textReceita;
    private String caminhoFoto;

    public PostagemReceita() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference postagemRef = firebaseRef.child("postagens");
        String idPostagem = postagemRef.push().getKey();
        setId(idPostagem);
    }

    public boolean salvarImagem (){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference postagensRef = firebaseRef.child("postagens").child(getIdUsuario()).child(getId());
        postagensRef.setValue(this);
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

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
