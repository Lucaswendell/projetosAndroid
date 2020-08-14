package com.example.cadastrodeusuario;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

class NossoAdapter_grid extends RecyclerView.Adapter {
    private List<Pessoa> pessoas;
    private Context context;


    public NossoAdapter_grid(List<Pessoa> pessoa, Context context) {
        this.pessoas = pessoa;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_pessoa_grid, parent, false);
        NossoViewHolder viewHolder = new NossoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        NossoViewHolder nossoViewHolder = (NossoViewHolder) holder;
        final Pessoa pessoa = pessoas.get(position);
        nossoViewHolder.nome.setText(pessoa.getNome().toString().split(" ")[0]);
        nossoViewHolder.telefone.setText(pessoa.getTel());
        //deletar
        nossoViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle(R.string.alerta_titulo)//Alerta
                    .setIcon(R.drawable.ic_warning_black_24dp) //icone warning
                    .setMessage(R.string.alerta_mensagem_delata)//Tem certeza que deseja excluir esta pessoa?
                    .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {//botao excluir
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BD bd = new BD(context);
                            if(bd.deletar(pessoa)){
                                removerPessoa(pessoa);
                                Snackbar.make(view, "Usuario deletado com sucesso.", Snackbar.LENGTH_SHORT).show();
                            }else{
                                Snackbar.make(view, "Erro ao deletar.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", null) //botao cancelar
                    .create()
                    .show();
            }
        });
        //editar
        nossoViewHolder.edita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity(view);
                Intent intent = new Intent(activity.getBaseContext(), CadastrarUser.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pessoa", pessoa);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    private Activity getActivity(View view) {
        Context ctx = view.getContext();
        while (ctx instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)ctx;
            }
            context = ((ContextWrapper)ctx).getBaseContext();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return pessoas != null ? pessoas.size() : 0;
    }
    public void removerPessoa(Pessoa pessoa){
        int position = pessoas.indexOf(pessoa);
        pessoas.remove(position);
        notifyItemRemoved(position); //avisa que foi removido
    }

}
