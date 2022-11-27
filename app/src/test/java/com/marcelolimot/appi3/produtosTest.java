package com.marcelolimot.appi3;

import com.marcelolimot.appi3.model.produtos;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


public class produtosTest {

    private int cod;
    private String nome;
    private int qtd;
    private String valor;
    private String imgUrl;
    private String desc;
    private String userCad;

    produtos p = new produtos(
            32, "ProdutoTeste"
            , 120, "332,00", ""
            , "Produto para teste"
            , "IbqXYlDVeYMm4FHJHcx4xdbFprt2");



    produtos q = new produtos( cod=0, nome=""
            , qtd=120, valor="332,00", imgUrl=""
            , desc="Produto para teste"
            , userCad="IbqXYlDVeYMm4FHJHcx4xdbFprt2");

    @Test
    public void codValido(){
        boolean resultadoCod = q.validaCod();
        Assert.assertFalse(resultadoCod);
    }

    @Test
    public void nomeValido(){
        boolean resultadoNome = q.validaNome();
        Assert.assertFalse(resultadoNome);
    }

    @Test
    public void qtdValido(){
        boolean resultadoQtd = q.validaQtd();
        Assert.assertFalse(resultadoQtd);
    }

    @Test
    public void valorValido(){
        boolean resultadoValor = q.validaValor();
        Assert.assertFalse(resultadoValor);
    }

    @Test
    public void userCadValido(){
        boolean resultadoUserCad = q.validaUserCad();
        Assert.assertFalse(resultadoUserCad);
    }
}
