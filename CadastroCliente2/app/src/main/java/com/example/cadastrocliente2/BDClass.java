package com.example.cadastrocliente2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDClass extends SQLiteOpenHelper {
    private static final String NOME_BD = "teste";
    private static final int VERSAO_BD = 6;

    public BDClass(Context ctx){
        super(ctx, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //cria a tabela caso não exista
        sqLiteDatabase.execSQL("create table user(_id integer primary key autoincrement, nome text not null, email text not null unique, telefone text not null, ende text not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //deleta caso exista e cria caso a versao mude
        sqLiteDatabase.execSQL("drop table user;");
        onCreate(sqLiteDatabase);
    }
}
