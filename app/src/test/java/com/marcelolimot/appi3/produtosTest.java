package com.marcelolimot.appi3;

import com.marcelolimot.appi3.model.produtos;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


public class produtosTest {

    produtos p = new produtos(
            32, "ProdutoTeste"
            , 120, "332,00", ""
            , "Produto para teste"
            , "IbqXYlDVeYMm4FHJHcx4xdbFprt2");

    @Test
    public void codValido(){

        boolean resultadoCod = p.validaCod();
        Assert.assertFalse(resultadoCod);


    }

    @Test
    public void nomeValido(){
        boolean resultadoNome = p.validaNome();
        Assert.assertFalse(resultadoNome);
    }

    @Test
    public void qtdValido(){
        boolean resultadoQtd = p.validaQtd();
        Assert.assertFalse(resultadoQtd);
    }

    @Test
    public void valorValido(){
        boolean resultadoValor = p.validaValor();
        Assert.assertFalse(resultadoValor);
    }
}
