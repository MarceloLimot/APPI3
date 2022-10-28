package com.marcelolimot.appi3;

public class User {

    private final String uid;
    private final String nome;
    private final String email;
    private final String imgUrl;

    public User(String uid, String nome, String email, String imgUrl) {
        this.uid = uid;
        this.nome = nome;
        this.email = email;
        this.imgUrl = imgUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
