package com.marcelolimot.appi3.database;

import android.database.sqlite.SQLiteDatabase;

public class ScriptDLL {
    public static final String Estoque = "bdEmpregados";

    SQLiteDatabase meuBancoDeDados;

    meuBancoDeDados = openOrCreateDatabase(Estoque, MODE_PRIVATE,  factory:null);

}
