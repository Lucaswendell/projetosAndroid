package com.example.cadastrodeusuario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BD {
    private SQLiteDatabase bd;
    public BD(Context ctx){
        //faz a conexao com o banco
        BDClass auxBd = new BDClass(ctx);
        bd = auxBd.getWritableDatabase();
    }
    //inserir no banco
    public boolean inserir(Pessoa pessoa){
        boolean resul = false;
        ContentValues  valores = new ContentValues();
        valores.put("nome", pessoa.getNome());
        valores.put("email", pessoa.getEmail());
        valores.put("ende", pessoa.getEnd());
        valores.put("telefone", pessoa.getTel());

        if(bd.insert("user", null, valores) > 0){
            resul = true;
        }
        return resul;

    }

    public boolean atualizar(Pessoa pessoa){
        boolean resul = false;
        ContentValues  valores = new ContentValues();

        valores.put("nome", pessoa.getNome());
        valores.put("email", pessoa.getEmail());
        valores.put("ende", pessoa.getEnd());
        valores.put("telefone", pessoa.getTel());

        if(bd.update("user", valores, "_id = ? ", new String[]{"" + pessoa.getId()}) == 1) {
            resul = true;
        }
        return resul;
    }
    public boolean deletar(Pessoa pessoa){
        boolean resul = false;
        if(bd.delete("user", "_id = ? ", new String[]{"" + pessoa.getId()}) == 1){
            resul = true;
        }
        return resul;
    }
    public List<Pessoa> selecinar(){
        List<Pessoa> pessoaArrayList = new ArrayList<Pessoa>();
        String[] colunas = new String[]{"_id", "nome", "email", "telefone", "ende"}; //colunas que quero consultar no banco
        Cursor cursor = bd.query("user", colunas, null, null, null, null, "nome ASC");

        if(cursor.getCount() > 0){
            cursor.moveToFirst(); //move para a primerira linha
            do{ //percorre a lista do curso
                Pessoa p = new Pessoa();
                p.setId(cursor.getLong(0));
                p.setNome(cursor.getString(1));
                p.setEmail(cursor.getString(2));
                p.setTel(cursor.getString(3));
                p.setEnd(cursor.getString(4));
                pessoaArrayList.add(p);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return pessoaArrayList;

    }

}
