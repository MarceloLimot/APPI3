package com.marcelolimot.appi3.model;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class produtos {
    private String cod;
    private String nome;
    private Integer qtd;
    private String valor;
    private String imgUrl;
    private String desc;
    private String userCad;

    public produtos(int cod, String nome, int qtd, String valor
            , String imgUrl, String desc
            , String userCad) {
    }

    public String getUserCad() {
        return userCad;
    }

    public void setUserCad(String userCad) {
        this.userCad = userCad;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public static class ProdutoBuilder{
        private String nome;
        private String quantidade;
        private String valor;
        private String imgUrl;
    }

    public boolean validaCod(){
        return this.cod != null;
    }

    public boolean validaNome(){
        return this.nome != null;
    }

    public boolean validaQtd(){
        return this.qtd != null;
    }

    public boolean validaValor(){
        return this.valor != null;
    }

    public boolean validaUserCad(){
        return this.userCad != null;
    }

}
