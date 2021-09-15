package com.example.receitasja.model;

public class PostagemReceita {

    private String nome;
    private String postagem;

    public PostagemReceita() {
    }

    public PostagemReceita(String nome, String postagem) {
        this.nome = nome;
        this.postagem = postagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPostagem() {
        return postagem;
    }

    public void setPostagem(String postagem) {
        this.postagem = postagem;
    }
}
