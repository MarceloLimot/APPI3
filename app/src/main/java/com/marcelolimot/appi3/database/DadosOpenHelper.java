package com.marcelolimot.appi3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DadosOpenHelper extends SQLiteOpenHelper {
        private static final String NOME_DO_BANCO = "BDEmpregados";
        private static int VERSAO_DO_BANCO = 1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DadosOpenHelper(Context context) {
        super(context, NOME_DO_BANCO, VERSAO_DO_BANCO, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
