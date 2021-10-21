package com.example.receitasja.model;

public class Feed {

    private String id;
    private String nomeUsuario;
    private String nomeReceita;
    private String fotoUsuario;
    private String fotoPostagem;
    private String ingredientes;

    public Feed() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nome) {
        this.nomeUsuario = nome;
    }

    public String getNomeReceita() {
        return nomeReceita;
    }

    public void setNomeReceita(String titulo) {
        this.nomeReceita = titulo;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getFotoPostagem() {
        return fotoPostagem;
    }

    public void setFotoPostagem(String fotoPostagem) {
        this.fotoPostagem = fotoPostagem;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }
}
